package com.project.community.ui.life;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.BbsBean;
import com.project.community.bean.CommentsListBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.SearchModel;
import com.project.community.ui.adapter.BbsCommentsApdater;
import com.project.community.ui.adapter.SearchAdapter;
import com.project.community.ui.adapter.SearchBbsAdapter;
import com.project.community.ui.adapter.SearchHistoryAdapter;
import com.project.community.ui.life.minsheng.ArticleDetailsActivity;
import com.project.community.ui.life.minsheng.MerchantDetailActivity;
import com.project.community.util.KeyBoardUtil;
import com.project.community.util.ScreenUtils;
import com.project.community.util.SearchHistoryCacheUtils;
import com.project.community.util.ToastUtil;
import com.project.community.view.CommentPopwindow;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by qizfeng on 17/8/11.
 * 搜索界面
 */

public class SearchActivity extends BaseActivity implements View.OnKeyListener {
    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.lv_search_result)
    RecyclerView mResultRecyclerView;
    @Bind(R.id.lv_search_history)
    ListView mListViewHistory;
    @Bind(R.id.ll_search_history)
    LinearLayout llHistory;
    @Bind(R.id.ll_search_result)
    LinearLayout llResult;
    @Bind(R.id.iv_clear_content)
    ImageView ivClear;
    @Bind(R.id.clear_history_btn)
    TextView mClearHistoryBtn;
    @Bind(R.id.view_lin_top)
    View mViewLineTop;
    private SearchAdapter mAdapter;
    private SearchHistoryAdapter mHistoryAdapter;
    private List<SearchModel> mData = new ArrayList<>();
    private List<String> historyData = new ArrayList<>();
    List<String> historyRecordList = new ArrayList<>();
    
//    private List<CommentModel> comments = new ArrayList<>();//评论列表
//    private CommentsApdater commentsPopwinAdapter;
//    private CommentPopwindow popupWindow;

    private String type = "0";
    private int index = 0;//2:民生点击进来搜索店铺
    private int page = 1;

    private List<BbsBean.ArtListBean> artListBeanList = new ArrayList<>();
    SearchBbsAdapter bbsApdater;


    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        type = bundle.getString("type");
        index = bundle.getInt("index", 0);
        mResultRecyclerView.setHasFixedSize(true);
        mResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        mResultRecyclerView.addItemDecoration(decoration);
        etSearchContent.setOnKeyListener(this);

        initSearchHistory();
        etSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {//相关课程listview隐藏 搜索历史显示
                    llResult.setVisibility(View.GONE);
                    llHistory.setVisibility(View.VISIBLE);
                } else {//相关课程listview显示 搜索历史隐藏
                    if (llHistory.getVisibility() == View.VISIBLE) {
                        llHistory.setVisibility(View.GONE);
                    }
                    if (ivClear.getVisibility() == View.GONE) {
                        ivClear.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivClear.setVisibility(View.GONE);
                }
            }
        });

        mAdapter = new SearchAdapter(mData, index, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                RxView.clicks(view)
                        .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                if (index == 3) {
                                    Intent intent = new Intent(SearchActivity.this, ArticleDetailsActivity.class);
                                    startActivity(intent);
                                }
                                if (index == 2) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("merchant_id", mAdapter.getItem(position).id);
                                    bundle.putString("merchant_distance", mAdapter.getItem(position).distance);
                                    MerchantDetailActivity.startActivity(SearchActivity.this, bundle);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("artId", mAdapter.getItem(position).id);
                                    TopicDetailActivity.startActivity(SearchActivity.this, bundle);
                                }

                            }
                        });

            }

            @Override
            public void onCustomClick(View view, int position) {

                if (index == 3) {
                    switch (view.getId()) {
                        case R.id.bbs_item_like_comment:
                            popAwindow(view, position);
                            break;
                        case R.id.bbs_item_like:
                            if (mData.get(position).id.equals("10")) mData.get(position).id = "0";
                            else mData.get(position).id = "10";
                            mAdapter.notifyItemChanged(position, mData.get(position));
                            break;
                    }
                }
            }
        });

        if (index == 2) {
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    page++;
                    StartSearch();
                }
            });
        }

        mAdapter.bindToRecyclerView(mResultRecyclerView);
        mResultRecyclerView.setAdapter(mAdapter);
        etSearchContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etSearchContent.setCursorVisible(true);
                return false;
            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {
            // 先隐藏键盘
            KeyBoardUtils.closeKeybord(etSearchContent, SearchActivity.this);
            etSearchContent.setCursorVisible(false);
            //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
            RxView.clicks(view)
                    .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
//                            if (index==2)
//                                showLoading();
                            StartSearch();
                        }
                    });

        }
        return false;
    }

    /**
     * 初始化搜索历史的记录显示
     */
    private void initSearchHistory() {
        String cache = SearchHistoryCacheUtils.getCache(SearchActivity.this);
        if (cache != null) {
            mViewLineTop.setVisibility(View.VISIBLE);
            historyRecordList = new ArrayList<>();
            for (String record : cache.split(",")) {
                historyRecordList.add(record);
            }
            if (historyRecordList.size() > 10) {
                historyRecordList = historyRecordList.subList(0, 10);
            }
            mHistoryAdapter = new SearchHistoryAdapter(this, historyRecordList, new SearchHistoryAdapter.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(View view, int position) {
                    historyRecordList.remove(position);
                    deleteItemCache(position);
//                    mListViewHistory.setAdapter(mHistoryAdapter);
                    mHistoryAdapter.notifyDataSetChanged();
                    if (mHistoryAdapter.getCount() > 0) {
                        mClearHistoryBtn.setText(getString(R.string.str_clearSearchHistory));
                        mViewLineTop.setVisibility(View.VISIBLE);
                    } else {
                        mClearHistoryBtn.setText(getString(R.string.str_empty_history));
                        mViewLineTop.setVisibility(View.GONE);
                    }
                }
            });
            if (historyRecordList.size() > 0) {
                mListViewHistory.setAdapter(mHistoryAdapter);
                mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etSearchContent.setText("");
                        etSearchContent.setText(historyRecordList.get(position));
                        // 先隐藏键盘
                        KeyBoardUtils.closeKeybord(etSearchContent, SearchActivity.this);
                        //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                        StartSearch();
                    }
                });
            }
        } else {
//            llHistory.setVisibility(View.GONE);
            mViewLineTop.setVisibility(View.GONE);
            mClearHistoryBtn.setText(getString(R.string.str_empty_history));
            historyRecordList.clear();
            if (mHistoryAdapter != null) {
                mHistoryAdapter.notifyDataSetChanged();
            }
        }
    }

    private void save(String text) {
        String oldCache = SearchHistoryCacheUtils.getCache(SearchActivity.this);
        StringBuilder builder = new StringBuilder(text);
        if (oldCache == null) {
            SearchHistoryCacheUtils.setCache(builder.toString(), SearchActivity.this);
            updateData();
        } else {
            builder.append("," + oldCache);
            if (!oldCache.contains(text)) {//避免缓存重复的数据
                SearchHistoryCacheUtils.setCache(builder.toString(), SearchActivity.this);
                updateData();
            }
        }
    }

    private void deleteItemCache(final int position) {
        String cache = SearchHistoryCacheUtils.getCache(SearchActivity.this);
        if (cache != null) {
            historyData = new ArrayList<>();
            String[] array = cache.split(",");
            for (int i = 0; i < array.length; i++) {
                historyData.add(array[i]);
                if (position == array.length - 1)
                    cache = cache.replace(array[position], "");
                else
                    cache = cache.replace(array[position] + ",", "");
            }
            SearchHistoryCacheUtils.setCache(cache, SearchActivity.this);
            historyData.remove(position);

//            historyData = Arrays.asList(cache.split(","));
        }
    }

    /**
     * 更新搜索历史数据显示
     */
    private void updateData() {
        initSearchHistory();
    }

    /**
     * "搜索" 请求网络数据
     */
    public void StartSearch() {
        String text = etSearchContent.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //缓存搜索历史
        save(text);
        if (index == 2 || index == 3) {
            switch (index) {
                case 2:
                    String locData = getIntent().getExtras().getString("locData");
                    if (page==1)
                        showLoading();
                    serverDao.doShopsSearch(locData, page, 15, text, new JsonCallback<BaseResponse<List<SearchModel>>>() {
                        @Override
                        public void onSuccess(BaseResponse<List<SearchModel>> listBaseResponse, Call call, Response response) {
                            KeyBoardUtils.closeKeybord(etSearchContent, SearchActivity.this);
                            Log.e( "StartSearchcj", "-----------------2");
                            if (page == 1) {
                                dismissDialog();
                                mData = new ArrayList<>();
                                mData.addAll(listBaseResponse.retData);
                                mAdapter.setNewData(mData);
                                mAdapter.setEnableLoadMore(true);
                                if (llResult.getVisibility() == View.GONE) {
                                    llResult.setVisibility(View.VISIBLE);
                                }
                                if (mData.size() == 0) {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(R.layout.layout_empty_search);
                                }
                            } else {
                                mData.addAll(listBaseResponse.retData);
                                if (listBaseResponse.retData.size() < AppConstants.PAGE_SIZE) {
                                    List<SearchModel> data = new ArrayList<>();
                                    data.addAll(listBaseResponse.retData);
                                    mAdapter.addData(data);
                                    mAdapter.loadMoreEnd();         //加载完成,没有更多内容了
                                } else {
                                    List<SearchModel> data = new ArrayList<>();
                                    data.addAll(listBaseResponse.retData);
                                    mAdapter.addData(data);
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            dismissDialog();
                            showToast(e.getMessage());
                        }
                    });
                    break;
                case 3:
                    serverDao.getBbs(getUser(this).id, String.valueOf(page),
                            String.valueOf(AppConstants.PAGE_SIZE), "", text,
                            new JsonCallback<BaseResponse<Object>>() {
                                @Override
                                public void onSuccess(BaseResponse<Object> objectBaseResponse, Call call, Response response) {
                                    String jsonObject = new Gson().toJson(objectBaseResponse.retData);
                                    if (llResult.getVisibility() == View.GONE) {
                                        llResult.setVisibility(View.VISIBLE);
                                    }
                                    KeyBoardUtils.closeKeybord(etSearchContent, SearchActivity.this);
//                        Log.e("tag_f",objectBaseResponse.retData.toString()+"");

                                    BbsBean bbsBean = new Gson().fromJson(jsonObject, BbsBean.class);

                                    artListBeanList.addAll(bbsBean.getArtList());
                                    if (bbsApdater != null) {
                                        if (page == 1) {
                                            bbsApdater.setNewData(artListBeanList);
                                            bbsApdater.setEnableLoadMore(true);
                                        } else {
                                            bbsApdater.addData(bbsBean.getArtList());
                                            bbsApdater.loadMoreComplete();
                                        }

                                        if (bbsBean.getArtList().size() < AppConstants.PAGE_SIZE)
                                            bbsApdater.loadMoreEnd();
                                    } else {
                                        setAdapter(bbsBean);
                                    }


                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    Log.e("tag_f", e.getMessage().toString() + "");
                                }
                            });

            }


        } else {
            //网络请求
            serverDao.doSearch(type, text, new DialogCallback<BaseResponse<List<SearchModel>>>(this) {
                @Override
                public void onSuccess(BaseResponse<List<SearchModel>> baseResponse, Call call, Response response) {
                    mData = new ArrayList<>();
                    mData.addAll(baseResponse.retData);
                    if (llResult.getVisibility() == View.GONE) {
                        llResult.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setNewData(mData);
                    if (mData.size() == 0) {
                        mAdapter.setNewData(null);
                        mAdapter.setEmptyView(R.layout.layout_empty_search);
                    }
                    KeyBoardUtils.closeKeybord(etSearchContent, SearchActivity.this);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    showToast(e.getMessage());
                }
            });
        }


    }

    private void setAdapter(BbsBean bbsBean) {

        KeyBoardUtils.closeKeybord(etSearchContent, SearchActivity.this);
        bbsApdater = new SearchBbsAdapter(artListBeanList, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArticleDetailsActivity.startActivity(SearchActivity.this, bbsApdater.getData().get(position).getId(), "");

            }

            @Override
            public void onCustomClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.bbs_item_like_comment:
                        popAwindow(view, position);
                        break;
                    case R.id.bbs_item_like:
                        collect(bbsApdater.getData().get(position).getId());
//                        if (data.get(position).id.equals("10")) data.get(position).id = "0";
//                        else data.get(position).id = "10";
//                        bbsApdater.notifyItemChanged(position, data.get(position));
                        break;
                }
            }
        });

        bbsApdater.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                StartSearch();

            }
        });


        mResultRecyclerView.setAdapter(bbsApdater);


        if (page == 1) {
            bbsApdater.setNewData(artListBeanList);
            bbsApdater.setEnableLoadMore(true);
        } else {
            bbsApdater.addData(bbsBean.getArtList());
            bbsApdater.loadMoreComplete();
        }

        if (bbsBean.getArtList().size() < AppConstants.PAGE_SIZE)
            bbsApdater.loadMoreEnd();
    }


    /**
     * 清空输入框
     */
    @OnClick(R.id.iv_clear_content)
    public void ClearContent(View view) {
        etSearchContent.setText("");
        etSearchContent.setCursorVisible(true);
        ivClear.setVisibility(View.GONE);
        if (historyRecordList.size() > 0 || historyData.size() > 0) {
            mClearHistoryBtn.setText(getString(R.string.str_clearSearchHistory));
        }

    }

    /**
     * 清空搜索历史
     */
    @OnClick(R.id.clear_history_btn)
    public void ClearSearchHistory(View view) {
        SearchHistoryCacheUtils.ClearCache(SearchActivity.this);
        updateData();
    }

    /**
     * 取消
     */
    @OnClick(R.id.tv_cancel)
    public void onCancel(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            KeyBoardUtils.closeKeybord(etSearchContent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<CommentsListBean.CommentsBean> comments = new ArrayList<>();//评论列表
    private BbsCommentsApdater commentsPopwinAdapter;
    private int commentPage = 1;
    private CommentPopwindow popupWindow;
    /**
     * 表示当前是回复那个人
     */
    private CommentsListBean.CommentsBean commentsBean;

    /**
     * 弹出评论列表
     *
     * @param parent
     */
    private void popAwindow(View parent, final int position) {
        commentPage = 1;

        comments.clear();

        commentsPopwinAdapter = new BbsCommentsApdater(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                commentsBean = comments.get(position);
                popupWindow.et_comment.setText(getString(R.string.txt_receive) + comments.get(position).getUserName() + ":");
                popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        popupWindow = new CommentPopwindow(this, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(this) * 0.8);
        popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
        commentsPopwinAdapter.bindToRecyclerView(popupWindow.lv_container);
//        if (comments.size() > 0)
//            popupWindow.lv_container.smoothScrollToPosition(comments.size() - 1);
//        if (comments.size() == 0) {
//            commentsPopwinAdapter.setNewData(null);
//            commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
//            TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
//            textView.setText(getString(R.string.empty_no_comment));
//        }


        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentsBean != null) {
                    //表示是回复
                    if (popupWindow.et_comment.getText().toString().contains(String.valueOf(getString(R.string.txt_receive) + comments.get(position).getUserName() + ":"))) {
                        replyComment(popupWindow.et_comment.getText().toString(), position);

                    } else {
                        //发表评论
                        releaseComment(popupWindow.et_comment.getText().toString(), position);
                    }


                } else {
                    //发表评论
                    releaseComment(popupWindow.et_comment.getText().toString(), position);


                }

            }
        });


        //设置评论总数
        commentsPopwinAdapter.setTotalComments(bbsApdater.getData().get(position).getComments());

        //加载更多
        commentsPopwinAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                commentPage++;
                getComment(position);
            }
        });


        getComment(position);
    }


    /**
     * 发表评论
     *
     * @param string
     * @param position
     */
    private void releaseComment(String string, final int position) {
        KeyBoardUtil.closeKeybord(this);
        progressDialog.show();
        serverDao.saveComment(getUser(this).id, artListBeanList.get(position).getId(),
                String.valueOf(artListBeanList.get(position).getCategoryId()), string, "", new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                        progressDialog.dismiss();
                        ToastUtil.showToast(SearchActivity.this, baseResponse.message + "");
                        if (baseResponse.errNum.equals("0")) {
                            popupWindow.et_comment.setText("");
                            comments.clear();
                            commentPage = 1;

                            artListBeanList.get(position).setComments(artListBeanList.get(position).getComments() + 1);
                            bbsApdater.notifyDataSetChanged();


                            getComment(position);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        progressDialog.dismiss();
                    }
                });
    }


    /**
     * 回复
     *
     * @param string
     * @param position
     */
    private void replyComment(String string, final int position) {
        KeyBoardUtil.closeKeybord(this);
        progressDialog.show();
        serverDao.saveComment(getUser(this).id, artListBeanList.get(position).getId(),
                String.valueOf(artListBeanList.get(position).getCategoryId()), string.replace(String.valueOf(getString(R.string.txt_receive) + comments.get(position).getUserName() + ":"), ""),
                commentsBean.getUserId(), new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {

                        progressDialog.dismiss();
                        ToastUtil.showToast(SearchActivity.this, baseResponse.message + "");
                        if (baseResponse.errNum.equals("0")) {
                            popupWindow.et_comment.setText("");
                            comments.clear();
                            commentPage = 1;
                            artListBeanList.get(position).setComments(artListBeanList.get(position).getComments() + 1);
                            bbsApdater.notifyDataSetChanged();
                            getComment(position);
                        } else {

                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        progressDialog.dismiss();
                    }
                });
    }


    /**
     * 获取评论
     */
    private void getComment(int position) {
        progressDialog.show();
        serverDao.getCommentList(bbsApdater.getData().get(position).getId(), String.valueOf(commentPage),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<CommentsListBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<CommentsListBean> commentsListBeanBaseResponse, Call call, Response response) {
                        progressDialog.dismiss();


                        Log.e("tag_f", commentsListBeanBaseResponse.toString() + "");

                        comments.addAll(commentsListBeanBaseResponse.retData.getComments());
                        if (commentPage == 1) {
                            commentsPopwinAdapter.setNewData(comments);
                            commentsPopwinAdapter.setEnableLoadMore(true);
                        } else {
                            commentsPopwinAdapter.addData(commentsListBeanBaseResponse.retData.getComments());
                            commentsPopwinAdapter.loadMoreComplete();
                        }

                        if (commentsListBeanBaseResponse.retData.getComments().size() < AppConstants.PAGE_SIZE)
                            commentsPopwinAdapter.loadMoreEnd();

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        progressDialog.dismiss();
                    }
                }
        );

        if (comments.size() == 0) {
            commentsPopwinAdapter.setNewData(comments);
            commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
            TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
            textView.setText(getString(R.string.empty_no_comment));
        }
    }


    /**
     * 添加收藏或者取消收藏
     *
     * @param id
     */
    private void collect(final String id) {
        progressDialog.show();
        serverDao.collectBbs(getUser(this).id, id, new JsonCallback<BaseResponse<Object>>() {
            @Override
            public void onSuccess(BaseResponse<Object> objectBaseResponse, Call call, Response response) {
                progressDialog.dismiss();
                ToastUtil.showToast(SearchActivity.this, objectBaseResponse.message);
                if (objectBaseResponse.errNum.equals("0")) {

                    for (int i = 0; i < bbsApdater.getData().size(); i++) {

                        if (bbsApdater.getData().get(i).getId().equals(id)) {
                            if (bbsApdater.getData().get(i).getStatus() == 0) {
                                bbsApdater.getData().get(i).setStatus(999);
                                bbsApdater.getData().get(i).setCollections(bbsApdater.getData().get(i).getCollections() + 1);
                                bbsApdater.notifyDataSetChanged();
                            } else {

                                bbsApdater.getData().get(i).setStatus(0);
                                bbsApdater.getData().get(i).setCollections(bbsApdater.getData().get(i).getCollections() - 1);

                                bbsApdater.notifyDataSetChanged();
                            }
                        }


                    }


                } else {

                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
                Log.e("tag_f", e.getMessage().toString() + "");
            }
        });


    }
}

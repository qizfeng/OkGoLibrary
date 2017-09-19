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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.NewsModel;
import com.project.community.model.SearchModel;
import com.project.community.ui.adapter.SearchAdapter;
import com.project.community.ui.adapter.SearchHistoryAdapter;
import com.project.community.util.SearchHistoryCacheUtils;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    private String type = "0";
    private int index = 0;

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
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("artId", mAdapter.getItem(position).id);
                TopicDetailActivity.startActivity(SearchActivity.this, bundle);
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });

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
            StartSearch();
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
}

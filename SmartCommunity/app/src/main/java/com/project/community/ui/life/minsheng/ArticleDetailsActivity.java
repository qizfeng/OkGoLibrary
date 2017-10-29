package com.project.community.ui.life.minsheng;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.Event.DelCommnetEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.ArticleBean;
import com.project.community.bean.CommentsListBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.ui.adapter.ArticleDetailsAdapter;
import com.project.community.util.KeyBoardUtil;
import com.project.community.util.ToastUtil;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/9/27.
 * 文章详情页面
 */
public class ArticleDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.et_input)
    EditText mEtInput;
    @Bind(R.id.btn_send)
    Button mBtnSend;
    @Bind(R.id.bottom_layout)
    LinearLayout mBottomLayout;

    private MenuItem menuItem;
    private Dialog mDialog;
    private View header;
    private ArticleDetailsAdapter mAdapter;
    private List<CommentsListBean.CommentsBean> comments = new ArrayList<>();//评论列表
    private String recStr = "";//回复评论
    private String targetId;//回復人id

//    private ImageView mIvHeadHeader; //头像
//    private TextView mTvHeadName, mTvHeadDate, mTvHeadComment, mTvCommentsTips;//头部名字,日期,内容
//    private GridView mGvImgs;
//    private List<String> mImages = new ArrayList<>();
//    ArticleDetailsImagsAdapter grid_photoAdapter;


    private  String categoryId;

    private String id = null;


    private int page = 1;
    /**
     * 帖子详情
     */
    private ArticleBean articleBean = new ArticleBean();

    /**
     * @param context
     * @param id      帖子id
     */
    public static void startActivity(Context context, String id,String categoryId) {

        Intent intent = new Intent(context, ArticleDetailsActivity.class);

        intent.putExtra("id", id);
        intent.putExtra("categoryId", categoryId);

        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }


    /**
     * 删除评论
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DelCommnetEvent event) {
            delComment(event.getCommentsBean());
    }

    private void delComment(final CommentsListBean.CommentsBean commentsBean) {

        KeyBoardUtil.closeKeybord(this);
        progressDialog.show();
        serverDao.delComment(getUser(this).id, commentsBean.getId(), new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                progressDialog.dismiss();
                ToastUtil.showToast(ArticleDetailsActivity.this, baseResponse.message + "");
                if (baseResponse.errNum.equals("0")) {
                    for (int i = 0; i < mAdapter.getData().size(); i++) {
                        if (mAdapter.getData().get(i).getId().equals(commentsBean.getId())) {
                            mAdapter.remove(i);
                        }
                    }
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.articledetails_title), R.mipmap.iv_back);


        id = getIntent().getStringExtra("id");
        categoryId = getIntent().getStringExtra("categoryId");

        if (id != null)
            getArticleDetails();
        else
            ToastUtil.showToast(this, "参数错误");

    }


    /**
     * 获取帖子详情
     */
    private void getArticleDetails() {


        progressDialog.show();
        serverDao.getArticle(getUser(this).id, id, new JsonCallback<BaseResponse<ArticleBean>>() {
            @Override
            public void onSuccess(BaseResponse<ArticleBean> baseResponse, Call call, Response response) {
                progressDialog.dismiss();
                if (baseResponse.errNum.equals("0")) {
                    articleBean = baseResponse.retData;
                    setView();
                } else {

                    ToastUtil.showToast(ArticleDetailsActivity.this, baseResponse.message);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
            }
        });


    }

    private void setView() {
        header = LayoutInflater.from(this).inflate(R.layout.header_article_detail, null);
//        mIvHeadHeader = (ImageView) header.findViewById(R.id.iv_head_header);
//        mTvHeadName = (TextView) header.findViewById(R.id.tv_head_name);
//        mTvHeadDate = (TextView) header.findViewById(R.id.tv_head_date);
//        mTvHeadComment = (TextView) header.findViewById(R.id.tv_head_comment);
//        mTvCommentsTips = (TextView) header.findViewById(R.id.tv_comment_tips);
//        mGvImgs = (GridView) header.findViewById(R.id.gv_imgs);


        TextView textView = (TextView) header.findViewById(R.id.bbs_item_name);
        textView.setText(articleBean.getUserName() + "");
        TextView textView1 = (TextView) header.findViewById(R.id.bbs_item_time);
        textView1.setText(articleBean.getCreateDate() + "");
        TextView textView2 = (TextView) header.findViewById(R.id.bbs_item_content);
        textView2.setText(articleBean.getContent() + "");
        TextView textView3 = (TextView) header.findViewById(R.id.bbs_item_comment);
        textView3.setText(articleBean.getComments() + "评论");


        Glide.with(this)
                .load(AppConstants.HOST + articleBean.getUserPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(this))
                .into((ImageView) header.findViewById(R.id.bbs_item_head));


        if (menuItem != null)
            if (articleBean.getStatus() != 0) {

                menuItem.setIcon(R.mipmap.d4_shoucang1_p);
            } else {
                menuItem.setIcon(R.mipmap.d4_shoucang1);
            }


        List<String> result = Arrays.asList(articleBean.getImageUrl().split(","));

        if (result.size() == 1) {

            header.findViewById(R.id.bbs_item_big_img).setVisibility(View.VISIBLE);
            header.findViewById(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            header.findViewById(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);

            Glide.with(this)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) header.findViewById(R.id.bbs_item_big_img));

        } else if (result.size() == 2) {
            header.findViewById(R.id.bbs_item_big_img).setVisibility(View.GONE);
            header.findViewById(R.id.bbs_item_ll_two_img).setVisibility(View.VISIBLE);
            header.findViewById(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) header.findViewById(R.id.bbs_item_ll_two_img_1));
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(1))
                    .into((ImageView) header.findViewById(R.id.bbs_item_ll_two_img_2));

        } else if (result.size() == 3) {
            header.findViewById(R.id.bbs_item_big_img).setVisibility(View.GONE);
            header.findViewById(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            header.findViewById(R.id.bbs_item_ll_three_img).setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) header.findViewById(R.id.bbs_item_ll_three_img_1));
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(1))
                    .into((ImageView) header.findViewById(R.id.bbs_item_ll_three_img_2));
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(2))
                    .into((ImageView) header.findViewById(R.id.bbs_item_ll_three_img_3));
        } else if (result.size() == 0) {
            header.findViewById(R.id.bbs_item_big_img).setVisibility(View.GONE);
            header.findViewById(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            header.findViewById(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
        }


        setAdapter();


    }

    private void setAdapter() {

        //        for (int i = 0; i < 6; i++) {
//            mImages.add("");
//        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(0, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();
        mAdapter = new ArticleDetailsAdapter(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                position = position - 1;//去掉头部
                commentsBean = comments.get(position);
                mBottomLayout.setVisibility(View.VISIBLE);
                recStr = getString(R.string.txt_receive) + comments.get(position).getUserName() + ":";
                targetId = comments.get(position).getId();
                mEtInput.setText(recStr);
                mEtInput.setSelection(mEtInput.getText().length());

            }

            @Override
            public void onCustomClick(View view, int position) {
                position = position - 1;//去除头部
                showAlertDialog(position);
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(header);
        mBtnSend.setOnClickListener(this);
//        grid_photoAdapter = new ArticleDetailsImagsAdapter(this, mImages);
//        mGvImgs.setAdapter(grid_photoAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeyBoardUtils.closeKeybord(mEtInput, ArticleDetailsActivity.this);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 10) {//向上滚动隐藏评论框
                    mBottomLayout.setVisibility(View.GONE);
                } else if (dy < -10) {//向下滚动显示评论框
                    mBottomLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView.scrollToPosition(0);

        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getComment();
            }
        });
        getComment();
    }

    private void getComment() {
        serverDao.getCommentList(id, String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<CommentsListBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<CommentsListBean> commentsListBeanBaseResponse, Call call, Response response) {
                        setRefreshing(false);

                        Log.e("tag_f", commentsListBeanBaseResponse.toString() + "");

                        comments.addAll(commentsListBeanBaseResponse.retData.getComments());
                        if (page == 1) {
                            mAdapter.setNewData(comments);
                            mAdapter.setEnableLoadMore(true);
                        } else {
                            mAdapter.addData(commentsListBeanBaseResponse.retData.getComments());
                            mAdapter.loadMoreComplete();
                        }

                        if (commentsListBeanBaseResponse.retData.getComments().size() < AppConstants.PAGE_SIZE)
                            mAdapter.loadMoreEnd();

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        setRefreshing(false);
                    }
                }
        );
    }

    private void loadData() {
//        String userId;
//        if (isLogin(this))
//            userId = getUser(this).id;
//        else
//            userId = "";
//        serverDao.getTopicDetail(userId, artId, new JsonCallback<BaseResponse<ArticleModel>>() {
//            @Override
//            public void onSuccess(final BaseResponse<ArticleModel> baseResponse, Call call, Response response) {
//                mData = baseResponse.retData;
//                try {
//                    if (0 == baseResponse.retData.status) {
//                        menuItem.setIcon(R.mipmap.d4_shoucang1);
//                    } else if (1 == baseResponse.retData.status) {
//                        menuItem.setIcon(R.mipmap.d4_shoucang1_p);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (baseResponse.retData.surveyInfo != null) {
//                    if (!TextUtils.isEmpty(baseResponse.retData.surveyInfo.id))
//                        mBtnWenjuan.setVisibility(View.VISIBLE);
//                    else
//                        mBtnWenjuan.setVisibility(View.GONE);
//                } else {
//                    mBtnWenjuan.setVisibility(View.GONE);
//                }
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            LogUtils.e("url:" + AppConstants.HOST + baseResponse.retData.url);
//                            mWebView.loadUrl(AppConstants.HOST + baseResponse.retData.url);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                getComments(artId);
//
//            }
//        });

    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        if (mAdapter != null) {
            comments.clear();
            mAdapter.notifyDataSetChanged();
            getComment();
        }

    }

    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(R.string.txt_confirm_delete);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin(ArticleDetailsActivity.this)) {
                    showToast(getString(R.string.toast_no_login));
                    return;
                }
//                deleteComment(position, mAdapter.getItem(position).id, 1);
                mDialog.dismiss();
            }
        });

    }

    /**
     * 表示当前是回复那个人
     */
    private CommentsListBean.CommentsBean commentsBean;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:

//                if (TextUtils.isEmpty(recStr)) {
//                    if (TextUtils.isEmpty(mEtInput.getText().toString())) {
//                        return;
//                    }
////                    doComment(artId, mEtInput.getText().toString(), "");
//                } else {
//                    if (!mEtInput.getText().toString().startsWith(recStr))
//                        targetId = "";
//                    String content = mEtInput.getText().toString().replace(recStr, "");
//                    if (TextUtils.isEmpty(content)) {
//                        return;
//                    }
////                    doComment(artId, content, targetId);
//                }
////                recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                if (commentsBean != null) {
                    //表示是回复
                    if (mEtInput.getText().toString().contains(String.valueOf(getString(R.string.txt_receive) + commentsBean.getUserName() + ":"))) {
                        replyComment(mEtInput.getText().toString());

                    } else {
                        //发表评论
                        releaseComment(mEtInput.getText().toString());
                    }


                } else {
                    //发表评论
                    releaseComment(mEtInput.getText().toString());


                }

                break;
        }
    }

    private void releaseComment(String string) {
        KeyBoardUtil.closeKeybord(this);
        progressDialog.show();
        serverDao.saveComment(getUser(this).id, id,
                categoryId, string, "", new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                        progressDialog.dismiss();
                        ToastUtil.showToast(ArticleDetailsActivity.this, baseResponse.message + "");
                        if (baseResponse.errNum.equals("0")) {
                            mEtInput.setText("");
                            comments.clear();
                            mAdapter.notifyDataSetChanged();
                            page = 1;
                            getComment();
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

    private void replyComment(String string) {
        KeyBoardUtil.closeKeybord(this);
        progressDialog.show();
        serverDao.saveComment(getUser(this).id,id,
              categoryId , string.replace(String.valueOf(getString(R.string.txt_receive) + commentsBean.getUserName() + ":"), ""),
                commentsBean.getUserId(), new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {

                        progressDialog.dismiss();
                        ToastUtil.showToast(ArticleDetailsActivity.this, baseResponse.message + "");
                        if (baseResponse.errNum.equals("0")) {
                            mEtInput.setText("");
                            comments.clear();
                            mAdapter.notifyDataSetChanged();
                            page = 1;
                            getComment();
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

    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        menuItem = menu.findItem(R.id.navigation_collect);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_collect:
                onCollect(item);
                KeyBoardUtils.closeKeybord(mEtInput, ArticleDetailsActivity.this);
                return true;
            case R.id.navigation_share:
//                if (mData.category.allowShare == 0 || mData.allowShare == 0) {
//                    showToast(getString(R.string.toast_no_share));
//                    return true;
//                }
                KeyBoardUtils.closeKeybord(mEtInput, ArticleDetailsActivity.this);
                UMWeb web = new UMWeb("1121");
                web.setTitle("11");//标题
                web.setThumb(new UMImage(ArticleDetailsActivity.this, R.mipmap.ic_launcher_round));  //缩略图
                web.setDescription("!111");//描述
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                config.setIndicatorVisibility(false);
                new ShareAction(ArticleDetailsActivity.this)
                        .withText("111")
                        .withMedia(new UMImage(ArticleDetailsActivity.this, R.mipmap.ic_launcher_round))
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(shareListener)
                        .open(config);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (KeyBoardUtils.isSHowKeyboard(this, mEtInput))
            KeyBoardUtils.closeKeybord(mEtInput, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private boolean isCollect;

    /**
     * 点击收藏
     *
     * @param item
     */
    private void onCollect(final MenuItem item) {
        if (!isLogin(this)) {
            showToast(getString(R.string.toast_no_login));
            return;
        }

        if (!isCollect) {
            item.setIcon(R.mipmap.d4_shoucang1_p);
        } else {
            item.setIcon(R.mipmap.d4_shoucang1);
        }
        isCollect = !isCollect;

//        if (mData.category.allowCollection == 0 || mData.allowCollection == 0) {
//            showToast(getString(R.string.toast_no_collect));
//            return;
//        }
//        serverDao.doCollectTopic(getUser(this).id, FartId, new JsonCallback<BaseResponse<List>>() {
//            @Override
//            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
//                if ("收藏成功".equals(baseResponse.message)) {
//                    item.setIcon(R.mipmap.d4_shoucang1_p);
//                } else if ("取消收藏成功".equals(baseResponse.message)) {
//                    item.setIcon(R.mipmap.d4_shoucang1);
//                }
//                showToast(baseResponse.message);
//
//            }
//
//            @Override
//            public void onError(Call call, Response response, Exception e) {
//                super.onError(call, response, e);
//                showToast(e.getMessage());
//            }
//        });
    }

}

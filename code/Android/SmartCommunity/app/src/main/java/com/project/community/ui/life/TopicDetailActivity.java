package com.project.community.ui.life;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.CommentModel;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.ui.life.zhengwu.WenjuanDetailActivity;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/3.
 * 文章正文页
 */

public class TopicDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_input)
    EditText mEtInput;
    @Bind(R.id.btn_send)
    Button mBtnSend;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.bottom_layout)
    LinearLayout mBottomLayout;

    private View header;
    private WebView mWebView;
    private TextView mTvCommentsTips;
    private Button mBtnWenjuan;
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsApdater mAdapter;
    private String mUrl;
    private String artId;
    private String recStr = "";//回复评论
    private String targetId;//回復人id
    private MenuItem menuItem;
    private Dialog mDialog;
    private ArticleModel mData = new ArticleModel();

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.title_topic_detail), R.mipmap.iv_back);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            String title = bundle.getString("title");
            if (getString(R.string.title_communication_notice).equals(title))
                initToolBar(mToolBar, mTvTitle, true, getString(R.string.title_communication_notice), R.mipmap.iv_back);
            else if (getString(R.string.tab_title_wuye_kuaixun).equals(title))
                initToolBar(mToolBar, mTvTitle, true, getString(R.string.tab_title_wuye_kuaixun), R.mipmap.iv_back);
            artId = bundle.getString("artId");
            mUrl = bundle.getString("url");
        }
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_topic_detail, null);
        mWebView = (WebView) header.findViewById(R.id.webView);
        mBtnWenjuan = (Button) header.findViewById(R.id.btn_wenjuan);
        mTvCommentsTips = (TextView) header.findViewById(R.id.tv_comment_tips);
        mBtnWenjuan.setOnClickListener(this);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    addImageClickListner();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        mWebView.loadUrl(mUrl);
        // 添加js交互接口类，并起别名 imageListener
        mWebView.addJavascriptInterface(new JavascriptInterface(this), "imageListener");
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(0, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();
        mAdapter = new CommentsApdater(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                position = position - 1;//去掉头部
                mBottomLayout.setVisibility(View.VISIBLE);
                recStr = getString(R.string.txt_receive) + comments.get(position).userName + ":";
                targetId = comments.get(position).userId;
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeyBoardUtils.closeKeybord(mEtInput, TopicDetailActivity.this);
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
    }


    // 注入js函数监听

    @SuppressLint("JavascriptInterface")
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        String jsStr = "javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "var imgs =new Array();" +
                "for(var i=0;i<objs.length;i++){" +
                "   objs[i].index = i;" +
                "   var index=i;" +
                "   imgs[index]=objs[index].src;" +
                "   objs[i].onclick=function(){" +
                "       window.imageListener.openImage(imgs[index],imgs,index);" +
                "   }" +
                "}" +
                "})()";
        mWebView.loadUrl(jsStr);
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img, String[] imgs, int position) {
            Intent intent = new Intent(TopicDetailActivity.this, ImageBrowseActivity.class);
            ArrayList<String> imgArray = new ArrayList<>();
            imgArray.addAll(Arrays.asList(imgs));
            intent.putStringArrayListExtra("imgs", imgArray);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:

                if (TextUtils.isEmpty(recStr)) {
                    if (TextUtils.isEmpty(mEtInput.getText().toString())) {
                        return;
                    }
                    doComment(artId, mEtInput.getText().toString(), "");
                } else {
                    if (!mEtInput.getText().toString().startsWith(recStr))
                        targetId = "";
                    String content = mEtInput.getText().toString().replace(recStr, "");
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    doComment(artId, content, targetId);
                }
//                recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                break;
            case R.id.btn_wenjuan:
                if (isLogin(this)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", AppConstants.URL_WENJUAN_DETIAL + "?uid=" + getUser(this).id + "&id=" + mData.surveyInfo.id);
                    intent.putExtra("bundle", bundle);
                    intent.setClass(this, WenjuanDetailActivity.class);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.toast_no_login));
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        loadData();
    }


    /**
     * 获取评论列表
     *
     * @param artId
     */
    private void getComments(final String artId) {
        serverDao.getComments(artId, new JsonCallback<BaseResponse<List<CommentModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<CommentModel>> baseResponse, Call call, Response response) {
                setRefreshing(false);
                comments = new ArrayList<>();
                comments = baseResponse.retData;
                mAdapter.setNewData(comments);
                mAdapter.loadMoreComplete();
                mTvCommentsTips.setText("评论(" + comments.size() + ")");
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }

    /**
     * 发评论
     *
     * @param artId
     * @param content
     * @param targetId
     */
    private void doComment(final String artId, String content, String targetId) {
        if (!isLogin(this)) {
            showToast(getString(R.string.toast_no_login));
            return;
        }
        if (mData.category.allowComment == 0 || mData.allowComment == 0) {
            showToast(getString(R.string.toast_no_comment));
            return;
        }
        serverDao.doComment(getUser(this).id, artId, content, targetId, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                mEtInput.setText("");
                getComments(artId);
                showToast(baseResponse.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }


    /**
     * 设置是否刷新动画
     *
     * @param refreshing true开始刷新动画 false结束刷新动画
     */
    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    private void loadData() {
        String userId;
        if (isLogin(this))
            userId = getUser(this).id;
        else
            userId = "";
        serverDao.getTopicDetail(userId, artId, new JsonCallback<BaseResponse<ArticleModel>>() {
            @Override
            public void onSuccess(final BaseResponse<ArticleModel> baseResponse, Call call, Response response) {
                mData = baseResponse.retData;
                try {
                    if (0 == baseResponse.retData.status) {
                        menuItem.setIcon(R.mipmap.d4_shoucang1);
                    } else if (1 == baseResponse.retData.status) {
                        menuItem.setIcon(R.mipmap.d4_shoucang1_p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (baseResponse.retData.surveyInfo != null) {
                    if (!TextUtils.isEmpty(baseResponse.retData.surveyInfo.id))
                        mBtnWenjuan.setVisibility(View.VISIBLE);
                    else
                        mBtnWenjuan.setVisibility(View.GONE);
                } else {
                    mBtnWenjuan.setVisibility(View.GONE);
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LogUtils.e("url:" + AppConstants.HOST + baseResponse.retData.url);
                            mWebView.loadUrl(AppConstants.HOST + baseResponse.retData.url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                getComments(artId);

            }
        });

    }

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
        if (mData.category.allowCollection == 0 || mData.allowCollection == 0) {
            showToast(getString(R.string.toast_no_collect));
            return;
        }
        serverDao.doCollectTopic(getUser(this).id, artId, new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                if ("收藏成功".equals(baseResponse.message)) {
                    item.setIcon(R.mipmap.d4_shoucang1_p);
                } else if ("取消收藏成功".equals(baseResponse.message)) {
                    item.setIcon(R.mipmap.d4_shoucang1);
                }
                showToast(baseResponse.message);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
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
                if(!isLogin(TopicDetailActivity.this)){
                    showToast(getString(R.string.toast_no_login));
                    return;
                }
                deleteComment(position, mAdapter.getItem(position).id, 1);
                mDialog.dismiss();
            }
        });

    }

    /**
     * 删除评论
     */
    private void deleteComment(final int position, String commentId, int type) {
        serverDao.doDeleteComment(getUser(this).id, commentId, type, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
                mAdapter.remove(position);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
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
                KeyBoardUtils.closeKeybord(mEtInput, TopicDetailActivity.this);
                return true;
            case R.id.navigation_share:
                if (mData.category.allowShare == 0 || mData.allowShare == 0) {
                    showToast(getString(R.string.toast_no_share));
                    return true;
                }
                KeyBoardUtils.closeKeybord(mEtInput, TopicDetailActivity.this);
                UMWeb web = new UMWeb(mWebView.getUrl());
                web.setTitle(mData.title);//标题
                web.setThumb(new UMImage(TopicDetailActivity.this, R.mipmap.ic_launcher_round));  //缩略图
                web.setDescription(mData.description);//描述
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                config.setIndicatorVisibility(false);
                new ShareAction(TopicDetailActivity.this)
                        .withText(mData.description)
                        .withMedia(new UMImage(TopicDetailActivity.this, R.mipmap.ic_launcher_round))
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
}

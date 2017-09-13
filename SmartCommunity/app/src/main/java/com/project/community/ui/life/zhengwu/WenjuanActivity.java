package com.project.community.ui.life.zhengwu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.view.Html5WebView;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/7/28.
 * 问卷
 */

public class WenjuanActivity extends BaseActivity {
    private String mUrl;
    private String mTitle;
    private LinearLayout mLayout;
    private WebView mWebView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private Menu mMenu;

    @android.webkit.JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenjuan);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            mUrl = bundle.getString("url");
        }
        initToolBar(toolbar, mTvTitle, true, getString(R.string.activity_wenjuan), R.mipmap.iv_back);
        pb.setMax(100);
        mLayout = (LinearLayout) findViewById(R.id.web_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        mWebView = new Html5WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setProgress(newProgress);
                if (newProgress >= 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    pb.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                String url = view.getUrl();
                LogUtils.e("onreceived:"+url);
                if (url.contains(AppConstants.URL_WENJUAN_LIST)) {
                    mMenu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
                    mTvTitle.setText(getString(R.string.activity_wenjuan));
                } else if (url.contains(AppConstants.URL_WENJUAN_DETIAL) ) {
                    mMenu.findItem(R.id.action_favorite).setIcon(null).setTitle("");
                    mTvTitle.setText(getString(R.string.activity_write_wenjuan));
                }else if(url.contains(AppConstants.URL_WENJUAN_RESULT)){
                    mTvTitle.setText(getString(R.string.activity_wenjuan_result));
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    String webViewurl = view.getUrl();
                    LogUtils.e("onPageFinished:"+webViewurl);
                    if (url.contains(AppConstants.URL_WENJUAN_LIST)) {
                        mMenu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
                        mTvTitle.setText(getString(R.string.activity_wenjuan));
                    } else if (url.contains(AppConstants.URL_WENJUAN_DETIAL) ) {
                        mMenu.findItem(R.id.action_favorite).setIcon(null).setTitle("");
                        mTvTitle.setText(getString(R.string.activity_write_wenjuan));
                    }else if(url.contains(AppConstants.URL_WENJUAN_RESULT)){
                        mTvTitle.setText(getString(R.string.activity_wenjuan_result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

        if (getString(R.string.title_masses_guide).equals(mTitle)) {
            mWebView.loadData(mUrl, "text/html;charset=utf-8", null);
        } else {
            if (mUrl != null)
                if (!mUrl.startsWith("http"))
                    mUrl = "http://" + mUrl;
            mWebView.loadUrl(mUrl);
        }
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                if (type == WebView.HitTestResult.UNKNOWN_TYPE)
                    return false;

                // 这里可以拦截很多类型，我们只处理图片类型就可以了
                switch (type) {
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        break;
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        // 获取图片的路径
                        String saveImgUrl = result.getExtra();
                        // 跳转到图片详情页，显示图片
//                        Intent i = new Intent(this, ImageActivity.class);
//                        i.putExtra("imgUrl", saveImgUrl);
//                        startActivity(i);
                        // ToastUtils.showShortToast(WebViewActivity.this, "长按了图片:" + saveImgUrl);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String url = mWebView.getUrl();
            if (url.contains(AppConstants.URL_WENJUAN_LIST))
                finish();
            else {
                mWebView.goBack();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_actionbar, menu);
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Bundle bundle = new Bundle();
                bundle.putString("url", AppConstants.URL_WENJUAN_SEARCH+"?uid="+getUser(this).id+"&keyword=");
                LogUtils.e("wenjuan:"+AppConstants.URL_WENJUAN_SEARCH+"?uid="+getUser(this).id+"&keyword=");
                WenjuanSearchActivity.startActivity(WenjuanActivity.this, bundle);
                return true;
            case android.R.id.home:
                String url = mWebView.getUrl();
                if (url.contains(AppConstants.URL_WENJUAN_LIST))
                    finish();
                else {
                    mWebView.goBack();
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
        mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }
}


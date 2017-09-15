package com.project.community.ui.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.AgreementResponse;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.view.Html5WebView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/28.
 * 服务条款
 */

public class AgreementWebViewActivity extends BaseActivity {
    private String mUrl;
    private LinearLayout mLayout;
    private WebView mWebView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @android.webkit.JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initToolBar(toolbar, mTvTitle, true, getString(R.string.activity_agreement), R.mipmap.iv_back);
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
        loadData();
        // 添加js交互接口类，并起别名 imageListener
        mWebView.addJavascriptInterface(new AgreementWebViewActivity.JavascriptInterface(this), "imageListener");
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
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    // 下面的@SuppressLint("JavascriptInterface")最好加上。防止在某些版本中js和java的交互不支持。
//    @SuppressLint("JavascriptInterface")
//    public void openImage(String img) {
//        ToastUtils.showShortToast(WebViewActivity.this, "点击了图片:" + img);
//    }


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
            Intent intent = new Intent(AgreementWebViewActivity.this, ImageBrowseActivity.class);
            ArrayList<String> imgArray = new ArrayList<>();
            imgArray.addAll(Arrays.asList(imgs));
            intent.putStringArrayListExtra("imgs", imgArray);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    private long mOldTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mOldTime < 1500) {
                mWebView.clearHistory();
                mWebView.loadUrl(mUrl);
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                this.finish();
            }
            mOldTime = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

    }

    private void loadData() {
        serverDao.getAgreement(new JsonCallback<BaseResponse<AgreementResponse>>() {
            @Override
            public void onSuccess(BaseResponse<AgreementResponse> baseResponse, Call call, Response response) {
                try {
                    mUrl = baseResponse.retData.content;
                    mWebView.loadData("<meta content=\"width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" name=\"viewport\" />\n"+mUrl, "text/html; charset=UTF-8", null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
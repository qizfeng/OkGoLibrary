package com.project.community.ui.life.zhengwu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.SearchModel;
import com.project.community.ui.adapter.SearchHistoryAdapter;
import com.project.community.util.SearchHistoryCacheUtils;
import com.project.community.view.Html5WebView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qizfeng on 17/7/31.
 * 问卷调查详情
 */

public class WenjuanSearchActivity extends BaseActivity implements View.OnKeyListener {
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


    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
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
    private RelativeLayout mSearchLayout;
    private SearchHistoryAdapter mHistoryAdapter;
    private List<SearchModel> mData = new ArrayList<>();
    private List<String> historyData = new ArrayList<>();
    List<String> historyRecordList = new ArrayList<>();

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, WenjuanSearchActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @android.webkit.JavascriptInterface
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenjuan_detail);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        initToolBar(toolbar, mTvTitle, true, getString(R.string.activity_wenjuan), R.mipmap.iv_back);
        if (bundle != null) {
            mUrl = bundle.getString("url");
        }
        pb.setMax(100);
        mLayout = (LinearLayout) findViewById(R.id.web_layout);
        mSearchLayout = (RelativeLayout) findView(R.id.global_search_action_bar_rl);
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
                LogUtils.e("webUrl:" + view.getUrl());
                if (url.equals("http://zhihuishequ.zpftech.com/surveyList/history.back")) {
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    String webViewurl = view.getUrl();
                    LogUtils.e("onPageFinished:" + webViewurl);
                    if (url.contains(AppConstants.URL_WENJUAN_SEARCH)) {
                        mSearchLayout.setVisibility(View.VISIBLE);
                        mAppbar.setVisibility(View.GONE);
                    } else if (url.contains(AppConstants.URL_WENJUAN_DETIAL)) {
                        mTvTitle.setText(getString(R.string.activity_write_wenjuan));
                        mSearchLayout.setVisibility(View.GONE);
                        mAppbar.setVisibility(View.VISIBLE);
                    } else if (url.contains(AppConstants.URL_WENJUAN_RESULT)) {
                        mTvTitle.setText(getString(R.string.activity_wenjuan_result));
                        mSearchLayout.setVisibility(View.GONE);
                        mAppbar.setVisibility(View.VISIBLE);
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
        // 添加js交互接口类，并起别名 imageListener
        mWebView.addJavascriptInterface(new JavascriptInterface(this), "imageListener");
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

        initView();
    }


    private void initView() {
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
            KeyBoardUtils.closeKeybord(etSearchContent, WenjuanSearchActivity.this);
            etSearchContent.setCursorVisible(false);
            //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
            LogUtils.e("======" + mUrl);
            loadUrl();
        }
        return false;
    }

    /**
     * 初始化搜索历史的记录显示
     */
    private void initSearchHistory() {
        String cache = SearchHistoryCacheUtils.getCache(WenjuanSearchActivity.this);
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
                        KeyBoardUtils.closeKeybord(etSearchContent, WenjuanSearchActivity.this);
                        //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                        loadUrl();
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

    private void loadUrl() {
        String text = etSearchContent.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //缓存搜索历史
        save(text);
        try {
            String url = mUrl + URLEncoder.encode(text, "utf-8");
            LogUtils.e("search:" + url);
            mWebView.loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        llHistory.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
    }

    private void save(String text) {
        String oldCache = SearchHistoryCacheUtils.getCache(WenjuanSearchActivity.this);
        StringBuilder builder = new StringBuilder(text);
        if (oldCache == null) {
            SearchHistoryCacheUtils.setCache(builder.toString(), WenjuanSearchActivity.this);
            updateData();
        } else {
            builder.append("," + oldCache);
            if (!oldCache.contains(text)) {//避免缓存重复的数据
                SearchHistoryCacheUtils.setCache(builder.toString(), WenjuanSearchActivity.this);
                updateData();
            }
        }
    }

    private void deleteItemCache(final int position) {
        String cache = SearchHistoryCacheUtils.getCache(WenjuanSearchActivity.this);
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
            SearchHistoryCacheUtils.setCache(cache, WenjuanSearchActivity.this);
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

    // 注入js函数监听

    @SuppressLint("JavascriptInterface")
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//        String jsStr = "javascript:(function(){" +
//                "var objs = document.getElementsByTagName(\"img\"); " +
//                "var imgs =new Array();" +
//                "for(var i=0;i<objs.length;i++){" +
//                "   objs[i].index = i;" +
//                "   var index=i;" +
//                "   imgs[index]=objs[index].src;" +
//                "   objs[i].onclick=function(){" +
//                "       window.imageListener.openImage(imgs[index],imgs,index);" +
//                "   }" +
//                "}" +
//                "})()";
//        mWebView.loadUrl(jsStr);
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img, String[] imgs, int position) {
//            Intent intent = new Intent(WebViewActivity.this, ImageBrowseActivity.class);
//            ArrayList<String> imgArray = new ArrayList<>();
//            imgArray.addAll(Arrays.asList(imgs));
//            intent.putStringArrayListExtra("imgs", imgArray);
//            intent.putExtra("position", position);
//            startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String url = mWebView.getUrl();
            if (url.contains(AppConstants.URL_WENJUAN_SEARCH))
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
        SearchHistoryCacheUtils.ClearCache(this);
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
            case android.R.id.home:
                String url = mWebView.getUrl();
                if (url.contains(AppConstants.URL_WENJUAN_SEARCH))
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
        menu.findItem(R.id.action_favorite).setIcon(null).setTitle("");
        return super.onPrepareOptionsMenu(menu);
    }


}

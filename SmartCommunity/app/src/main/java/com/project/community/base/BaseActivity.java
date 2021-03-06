package com.project.community.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.view.CustomProgress;
import com.lzy.imagepicker.view.SystemBarTintManager;
import com.project.community.R;
import com.project.community.callback.ServerDao;
import com.project.community.callback.ServerDaoImpl;
import com.project.community.constants.SharedPreferenceUtils;
import com.project.community.model.UserModel;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    public ServerDao serverDao;
    public GlideImageLoader glide;
    public CustomProgress progressDialog;

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        serverDao = new ServerDaoImpl(this);
        glide = new GlideImageLoader();
        progressDialog = new CustomProgress(this, com.library.okgo.R.style.Custom_Progress);
        initSystemBarTint();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor() {
        return getColorPrimary();
//        return Color.TRANSPARENT;
    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return false;
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具类，有些4.4的手机有问题，这里为演示方便，不使用沉浸式
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(setStatusBarColor());
        }

    }

    /**
     * 获取主题色
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题色
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, TextView tv_title, boolean homeAsUpEnabled, String title, int navigationIcon) {
        tv_title.setText(title);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(navigationIcon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

//    public void initToolBar(Toolbar toolbar,TextView tv_title, boolean homeAsUpEnabled, int resTitle,int navigationIcon) {
//        initToolBar(toolbar,tv_title, homeAsUpEnabled, getString(resTitle),navigationIcon);
//    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    public void showToast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        ToastUtils.showShortToast(this, msg);
    }

//    private ProgressDialog dialog;
//
//    public void showLoading() {
//        if (dialog != null && dialog.isShowing()) return;
//        dialog = new ProgressDialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("请求网络中...");
//        dialog.show();
//    }
//
//    public void dismissLoading() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }

    /**
     * 是否登录
     *
     * @param context
     * @return
     */
    public boolean isLogin(Context context) {
        return SharedPreferenceUtils.getBoolean(context, SharedPreferenceUtils.SP_LOGIN, false);
    }

    /**
     * 保存登录状态
     *
     * @param context
     * @param status
     */
    public void saveLoginStatus(Context context, boolean status) {
        SharedPreferenceUtils.putBoolean(context, SharedPreferenceUtils.SP_LOGIN, status);
    }


    /**
     * 获取登录用户信息
     *
     * @param context
     * @return
     */
    public UserModel getUser(Context context) {
        UserModel userModel = null;
        Gson gson = new Gson();
        try {
            userModel = gson.fromJson(SharedPreferenceUtils.getString(context, SharedPreferenceUtils.SP_USER), UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userModel;
    }


    /**
     * 保存用户信息
     *
     * @param context
     * @param user
     */
    public void saveUser(Context context, String user) {
        SharedPreferenceUtils.putString(context, SharedPreferenceUtils.SP_USER, user);
    }


    /**
     * 获取用户名
     *
     * @param context
     * @return
     */
    public String getUsername(Context context) {
        String username = SharedPreferenceUtils.getString(context, SharedPreferenceUtils.SP_USERNAME);
        return username;
    }

    /**
     * 保存用户名
     *
     * @param context
     * @param username
     */
    public void saveUsername(Context context, String username) {
        SharedPreferenceUtils.putString(context, SharedPreferenceUtils.SP_USERNAME, username);
    }

    /**
     * 获取密码
     *
     * @param context
     * @return
     */
    public String getUserPwd(Context context) {
        String pwd = SharedPreferenceUtils.getString(context, SharedPreferenceUtils.SP_USER_PWD);
        return pwd;
    }

    /**
     * 保存密碼
     *
     * @param context
     * @param pwd
     */
    public void saveUserPwd(Context context, String pwd) {
        SharedPreferenceUtils.putString(context, SharedPreferenceUtils.SP_USER_PWD, pwd);
    }


    /**
     * 是否保存密码
     *
     * @param context
     * @return
     */
    public boolean getIsRemember(Context context) {
        boolean isRemember = SharedPreferenceUtils.getBoolean(context, SharedPreferenceUtils.SP_IS_REMEMBER_PWD);
        return isRemember;
    }

    /**
     * 保存是否记住密码
     *
     * @param context
     * @param isRemember
     */
    public void saveIsRemember(Context context, boolean isRemember) {
        SharedPreferenceUtils.putBoolean(context, SharedPreferenceUtils.SP_IS_REMEMBER_PWD, isRemember);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取是否播放动画
     *
     * @param context
     * @return
     */
    public static boolean willPlayAnim(Context context) {
        return SharedPreferenceUtils.getBoolean(context, SharedPreferenceUtils.SP_WILL_PLAY);
    }

    /**
     * 保存是否播放动画
     *
     * @param context
     * @param willPlay
     */
    public static void saveWillPlayAnim(Context context, boolean willPlay) {
        SharedPreferenceUtils.putBoolean(context, SharedPreferenceUtils.SP_WILL_PLAY, willPlay);
    }

    public void showLoading() {
        //网络请求前显示对话框
        try {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        //网络请求结束后关闭对话框
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 保存定位经纬度
     *
     * @param context
     * @param latitude
     * @param longitude
     */
    public void saveLocation(Context context, String latitude, String longitude) {
        SharedPreferenceUtils.putString(context, "lat", latitude);
        SharedPreferenceUtils.putString(context, "long", longitude);
    }

    /**
     * 取保存的经纬度
     *
     * @param context
     * @return
     */
    public String[] getLocation(Context context) {
        String[] locData = new String[2];
        locData[0] = SharedPreferenceUtils.getString(context, "lat");
        locData[1] = SharedPreferenceUtils.getString(context, "long");
        return locData;
    }

    /**
     * 社区启动页
     *
     * @param context
     * @param url
     */
    public void saveCommunityStartPage(Context context, String url) {
        SharedPreferenceUtils.putString(context, "communityStartPage", url);
    }

    /**
     * 获取社区启动页
     *
     * @param context
     * @return
     */
    public String getCommunityStartPage(Context context) {
        return SharedPreferenceUtils.getString(context, "communityStartPage");
    }

    /**
     * 保存購物車数量
     *
     * @param context
     * @param count
     */
    public void saveShopCartCount(Context context, int count) {
        SharedPreferenceUtils.putInt(context, "shopCartCount", count);
    }

    /**
     * 獲取購物車数量
     *
     * @param context
     * @return
     */
    public int getShopCartCount(Context context) {
        int count = 0;
        count = SharedPreferenceUtils.getInt(context, "shopCartCount", 0);
        return count;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
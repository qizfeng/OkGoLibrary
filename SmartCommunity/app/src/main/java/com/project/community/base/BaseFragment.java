package com.project.community.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.ToastUtils;
import com.project.community.callback.ServerDao;
import com.project.community.callback.ServerDaoImpl;
import com.project.community.constants.SharedPreferenceUtils;
import com.project.community.model.UserModel;

/**
 * 若把初始化内容放到initData实现,就是采用Lazy方式加载的Fragment
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * -
 * -注1: 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * ------可以调用mViewPager.setOffscreenPageLimit(size),若设置了该属性 则viewpager会缓存指定数量的Fragment
 * -注2: 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * -注3: 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 */
public abstract class BaseFragment extends Fragment {

    protected String fragmentTitle;             //fragment标题
    private boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected LayoutInflater inflater;
    public GlideImageLoader glide;
    public ServerDao serverDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        isFirstLoad = true;
        serverDao = new ServerDaoImpl(getActivity());
        View view = initView(inflater, container, savedInstanceState);
        isPrepared = true;
        lazyLoad();
        glide = new GlideImageLoader();
        return view;
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initData();

    public String getTitle() {
        return TextUtils.isEmpty(fragmentTitle) ? "" : fragmentTitle;
    }

    public void setTitle(String title) {
        fragmentTitle = title;
    }

    public void showToast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        ToastUtils.showShortToast(getActivity(), msg);
    }

    public Toolbar initToolbar(Toolbar toolbar, String title) {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) getActivity();
        //Toolbar toolbar = (Toolbar) mAppCompatActivity.findViewById(toolbarId);
        mAppCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    public Toolbar initToolbar(Toolbar toolbar, TextView tv_title, String title) {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) getActivity();
        //Toolbar toolbar = (Toolbar) mAppCompatActivity.findViewById(toolbarId);
        mAppCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        tv_title.setText(title);
        return toolbar;
    }

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
        UserModel userModel = new UserModel();
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
}
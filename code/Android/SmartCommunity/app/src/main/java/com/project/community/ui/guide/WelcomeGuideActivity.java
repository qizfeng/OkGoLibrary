package com.project.community.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.constants.SharedPreferenceUtils;
import com.project.community.ui.SplashActivity;
import com.project.community.ui.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zipingfang on 17/7/4.
 */

public class WelcomeGuideActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;

    // 引导页图片资源
    private static final int[] pics = { R.layout.guid_view1,
            R.layout.guid_view2, R.layout.guid_view3};

    // 底部小点图片
//    private ImageView[] dots;

    // 记录当前选中位置
//    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        views = new ArrayList<View>();

        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_login);
                startBtn.setTag("enter");
                RxView.clicks(startBtn)
                        .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                if (startBtn.getTag().equals("enter")) {
                                    enterMainActivity();
                                    return;
                                }

                                int position = (Integer) startBtn.getTag();
                                setCurView(position);
                                setCurDot(position);
                            }
                        });
            }

            views.add(view);

        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());
        initDots();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SharedPreferenceUtils.putBoolean(WelcomeGuideActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        Intent intent = new Intent(WelcomeGuideActivity.this,
                SplashActivity.class);
        startActivity(intent);
        SharedPreferenceUtils.putBoolean(WelcomeGuideActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置

        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }
    }
}


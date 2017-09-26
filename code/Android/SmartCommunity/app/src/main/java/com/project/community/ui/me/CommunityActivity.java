package com.project.community.ui.me;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.customview.viewpager.NoScrollViewPager;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityActivity extends BaseActivity {

    @Bind(R.id.tv_serview_wait)
    TextView mTvServiewWait;
    @Bind(R.id.tv_serview_ing)
    TextView mTvServiewIng;
    @Bind(R.id.tv_serview_com)
    TextView mTvServiewComp;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.view_red_wait)
    View viewRedWait;
    @Bind(R.id.ll_servers_wait)
    LinearLayout llServersWait;
    @Bind(R.id.view_red_ing)
    View viewRedIng;
    @Bind(R.id.ll_servers_ing)
    LinearLayout llServersIng;
    @Bind(R.id.view_red_complet)
    View viewRedComplet;
    @Bind(R.id.ll_servers_commplet)
    LinearLayout llServersCommplet;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.viewpager)
    NoScrollViewPager mViewpager;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private List<Fragment> mFragmentsList;
    private MyFrageStatePagerAdapter mAdapter;
    Fragment mServiesWaitFragment;
    Fragment mServiesIngFragment;
    Fragment mServiesCompFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community2);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.community), R.mipmap.iv_back);
        mToolBar.setLogo(R.mipmap.e1_icon1);
        mFragmentsList=new ArrayList<>();
        mServiesWaitFragment=new ServiesCompFragment();
        mServiesIngFragment=new ServiesCompFragment();
        mServiesCompFragment=new ServiesCompFragment();
        mFragmentsList.add(mServiesWaitFragment);
        mFragmentsList.add(mServiesIngFragment);
        mFragmentsList.add(mServiesCompFragment);
        mAdapter=new MyFrageStatePagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setNoScroll(true);
        /**/mViewpager.setOffscreenPageLimit(3);  //*这一点需注意缓存.....****

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                WindowManager wm = getWindowManager();

                int screeWidth = wm.getDefaultDisplay().getWidth();
                int screeHeight = wm.getDefaultDisplay().getHeight();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line
                        .getLayoutParams();
                lp.leftMargin= (int) (positionOffset*(screeWidth*1.0/4)+position*(screeWidth/4));
                line.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetTextView(mTvServiewWait);
                        break;
                    case 1:
                        resetTextView(mTvServiewIng);
                        break;
                    case 2:
                        resetTextView(mTvServiewComp);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void resetTextView(TextView v) {

        mTvServiewWait.setTextColor(getResources().getColor(R.color.color_gray_666666));
        mTvServiewIng.setTextColor(getResources().getColor(R.color.color_gray_666666));
        mTvServiewComp.setTextColor(getResources().getColor(R.color.color_gray_666666));
        v.setTextColor(getResources().getColor(R.color.login_bottom_txt));

    }
    //    手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        mViewpager.setCurrentItem(desTab, true);
    }
    @OnClick({R.id.ll_servers_wait, R.id.ll_servers_ing, R.id.ll_servers_commplet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_servers_wait:
                changeView(0);
                break;
            case R.id.ll_servers_ing:
                changeView(1);
                break;
            case R.id.ll_servers_commplet:
                changeView(2);
                break;
        }
    }

    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        FragmentManager fm;

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

    }

    private MenuItem menuItem;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_off_duty, menu);

//        getLayoutInflater().setFactory(new android.view.LayoutInflater.Factory(){
//
//            @Override
//            public View onCreateView(String name, Context context, AttributeSet attrs) {
//                if (name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {
//                    try{
//                        LayoutInflater f = LayoutInflater.from(context);
//                        final View view = f.createView(name, null, attrs);
//                        if(view instanceof TextView) {
//                            TextView menuTv = ((TextView)view);
//                            //这里你就可以修改TextView的字体颜色，大小，背景等等.
//                            menuTv.setTextColor(getResources().getColor(R.color.color_red_d54824));
//                        }
//                        return view;
//                    }catch (InflateException e) {
//                        e.printStackTrace();
//                    }catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//        });
        menuItem = menu.findItem(R.id.action_menu_off);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_off:
                ToastUtils.showLongToast(CommunityActivity.this,"下班");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}

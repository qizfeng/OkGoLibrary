package com.project.community.ui;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.community.CommunityFragment;
import com.project.community.ui.index.IndexFragment;
import com.project.community.ui.life.LifeFragment;
import com.project.community.ui.me.MeFragment;
import com.project.community.util.BottomNavigationViewHelper;
import com.project.community.view.RedPointDrawable;
import com.umeng.socialize.UMShareAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页activity
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;//底部导航栏
//    @Bind(R.id.viewPager)
//    NoScrollViewPager viewPager;//viewPager
    private static final int INDEX_HOME_FRAGMENT = 0;
    private static final int INDEX_LIFE_FRAGMENT = 1;
    private static final int INDEX_COMMUNITY_FRAGMENT = 2;
    private static final int INDEX_MY_FRAGMENT=3;
    private int checkFragment = INDEX_HOME_FRAGMENT;//当前选中

    public static void startActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        //取消位移动画
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(this);
        initFragments();
        initForMessageCenterIcon(bottom_navigation,false);
    }

    private List<Fragment> fragments  =new ArrayList<>();
    private void initFragments(){
        fragments  =new ArrayList<>();
        fragments.add(new IndexFragment());
        fragments.add(new LifeFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new MeFragment());
        if (fragments.size() == 4) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                transaction.add(R.id.fl_home_fragment_container, fragment);
                transaction.hide(fragment);
            }
            transaction.show(fragments.get(INDEX_HOME_FRAGMENT));
            transaction.commit();
        }
    }

    @Override
    protected boolean translucentStatusBar() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_index:
                //首页
                checkFragment = INDEX_HOME_FRAGMENT;
                switchFragment(checkFragment);
                initForMessageCenterIcon(bottom_navigation,false);
                return true;//注意!!! 不要break,否则BottomNavigationView无切换效果
            case R.id.navigation_life:
                //生活
                checkFragment= INDEX_LIFE_FRAGMENT;
                switchFragment(checkFragment);
                initForMessageCenterIcon(bottom_navigation,false);
                return true;//注意!!! 不要break,否则BottomNavigationView无切换效果
            case R.id.navigation_community:
                //社区
                if(!isLogin(this)){
                    showToast(getString(R.string.toast_no_login));
                    return false;
                }
                checkFragment=INDEX_COMMUNITY_FRAGMENT;
                switchFragment(checkFragment);
                initForMessageCenterIcon(bottom_navigation,false);
                return true;//注意!!! 不要break,否则BottomNavigationView无切换效果
            case R.id.navigation_me:
                //我的
                checkFragment=INDEX_MY_FRAGMENT;
                switchFragment(checkFragment);
                initForMessageCenterIcon(bottom_navigation,true);
                return true;//注意!!! 不要break,否则BottomNavigationView无切换效果
            default:
                break;
        }

        return false;
    }

    //切换fragment
    private void switchFragment(int index){
        //切换Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (index == i) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
    }

    private LocalActivityManager manager;
    private MainPagerAdapter viewPageAdapter;

//    private void AddActivitiesToViewPager() {
//        List<View> mViews = new ArrayList<View>();
//        Intent intent = new Intent();
//
//        intent.setClass(this, IndexActivity.class);
//        intent.putExtra("id", 1);
//        mViews.add(getView("IndexActivity", intent));
//
//        intent.setClass(this, IndexActivity.class);
//        intent.putExtra("id", 2);
//        mViews.add(getView("LifeActivity", intent));
//
//        intent.setClass(this, CommunityActivity.class);
//        intent.putExtra("id", 3);
//        mViews.add(getView("CommunityActivity", intent));
//
//        intent.setClass(this, IndexActivity.class);
//        intent.putExtra("id", 4);
//        mViews.add(getView("QualityActivity4", intent));
//
//        viewPageAdapter = new MainPagerAdapter(mViews);
//       // viewPager.setAdapter(viewPageAdapter);
//
//    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    class MainPagerAdapter extends PagerAdapter implements Serializable {
        private List<View> views;

        public MainPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            return views.get(position);
        }
    }

    class MainFragmentPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragments;
        public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
          //  super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    private void initForMessageCenterIcon(BottomNavigationView navigationView,boolean isShowRedPoint) {
        Menu menu = navigationView.getMenu();
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.navigation_me) {
                RedPointDrawable redPointDrawable;
                if(checkFragment==INDEX_MY_FRAGMENT){
                    redPointDrawable=RedPointDrawable.wrap(MainActivity.this,getResources().getDrawable(R.mipmap.c1_btn4_p_dian),getResources()
                            .getDimensionPixelSize(R.dimen.red_point_radius_small));
                }else {
                    redPointDrawable=RedPointDrawable.wrap(MainActivity.this,getResources().getDrawable(R.mipmap.c1_btn4_dian),getResources()
                            .getDimensionPixelSize(R.dimen.red_point_radius_small));
                }
                redPointDrawable.setGravity(Gravity.LEFT);
                redPointDrawable.setShowRedPoint(false);
                item.setIcon(redPointDrawable);
                // 把drawable添加到我们的成员变量中去，以便后面直接对它进行设置
                //mRedPointView.addRedPointDrawable(redPointDrawable);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}

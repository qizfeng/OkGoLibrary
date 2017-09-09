package com.project.community.ui.life;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.library.customview.viewpager.NoScrollViewPager;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.ui.life.minsheng.MinshengActivity;
import com.project.community.ui.life.wuye.WuyeActivity;
import com.project.community.ui.life.zhengwu.ZhengwuActivity;
import com.project.community.util.TablayoutLineReflex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qizfeng on 17/7/11.
 */

public class LifeFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
//    @Bind(R.id.tv_title)
//    TextView tv_title;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    NoScrollViewPager viewPager;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_life,container,false);
        manager = new LocalActivityManager(getActivity(), true);
        manager.dispatchCreate(savedInstanceState);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void initData() {
        initToolbar(toolbar,  "");
        setHasOptionsMenu(true);
        initTabLayout();
        AddActivitiesToViewPager();
    }


    /**
     * 初始化tabLayout
     */
    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_life_zhengwu));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_life_wuye));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_life_minsheng));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineReflex.setTabLine(getActivity(),tabLayout,15,15);
//                TablayoutLineReflex.setTabLine(getActivity(),tabLayout,15,15);
            }
        });
//        TablayoutLineReflex.setTabLine(getActivity(),tabLayout,30,30);
        viewPager.setNoScroll(true);
    }

    @Override
    public void onClick(View view) {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:

                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Bundle bundle = new Bundle();
                if(viewPager.getCurrentItem()==0){
                    bundle.putString("type","0");
                    bundle.putInt("index",0);
                }else if(viewPager.getCurrentItem()==1){
                    bundle.putString("type","1");
                    bundle.putInt("index",1);
                }else if(viewPager.getCurrentItem()==2){
                    bundle.putString("type","mobile");
                    bundle.putInt("index",2);
                }
                SearchActivity.startActivity(getActivity(),bundle);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
//        return true;
//    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actionbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    class MainFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private String[] mTitles = new String[]{"政务", "物业", "民生"};
        public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }


        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }



    private LocalActivityManager manager;
    private MainPagerAdapter viewPageAdapter;

    private void AddActivitiesToViewPager() {
        List<View> mViews = new ArrayList<View>();
        Intent intent = new Intent();

        intent.setClass(getActivity(), ZhengwuActivity.class);
        intent.putExtra("id", 1);
        mViews.add(getView("ZhengwuActivity", intent));

        intent.setClass(getActivity(), WuyeActivity.class);
        intent.putExtra("id", 2);
        mViews.add(getView("WuyeActivity", intent));

        intent.setClass(getActivity(), MinshengActivity.class);
        intent.putExtra("id", 3);
        mViews.add(getView("MinshengActivity", intent));


        viewPageAdapter = new MainPagerAdapter(mViews);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);

    }


    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
    class MainPagerAdapter extends PagerAdapter implements Serializable {
        private List<View> views;
        private String[] mTitles = new String[]{"政务", "物业", "民生"};
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }


}

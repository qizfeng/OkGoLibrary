package com.project.community.ui.life;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.library.customview.viewpager.NoScrollViewPager;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.ui.life.minsheng.MinshengFragment;
import com.project.community.ui.life.wuye.WuyeFragment;
import com.project.community.ui.life.zhengwu.ZhengwuFragment;
import com.project.community.util.TablayoutLineReflex;

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
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    NoScrollViewPager viewPager;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_life, container, false);
        ButterKnife.bind(this, view);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //换成下面这句就OK了
        toolbar.inflateMenu(R.menu.menu_actionbar);
        return view;
    }

    @Override
    protected void initData() {
        initToolbar(toolbar, "");
        setHasOptionsMenu(true);
        initTabLayout();
        initFragments();
//        AddActivitiesToViewPager();
    }

    public static int index = 0;

    @Override
    protected void onVisible() {
        super.onVisible();
        LogUtils.e("index:" + index);
        if (index != 0)
            viewPager.setCurrentItem(index);
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
                TablayoutLineReflex.setTabLine(getActivity(), tabLayout, 30, 30);
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
                if (viewPager.getCurrentItem() == 0) {
                    bundle.putString("type", "0");
                    bundle.putInt("index", 0);
                } else if (viewPager.getCurrentItem() == 1) {
                    bundle.putString("type", "1");
                    bundle.putInt("index", 1);
                } else if (viewPager.getCurrentItem() == 2) {
                    bundle.putString("type", "mobile");
                    bundle.putInt("index", 2);
                }
                SearchActivity.startActivity(getActivity(), bundle);
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
        inflater.inflate(R.menu.menu_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private List<Fragment> fragments = new ArrayList<>();
    private void initFragments(){
        fragments.add(new ZhengwuFragment());
        fragments.add(new WuyeFragment());
        fragments.add(new MinshengFragment());
        MainFragmentPagerAdapter fragmentPagerAdapter = new MainFragmentPagerAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(fragmentPagerAdapter);
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
}

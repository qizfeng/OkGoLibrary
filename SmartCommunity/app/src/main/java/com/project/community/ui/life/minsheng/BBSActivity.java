package com.project.community.ui.life.minsheng;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.ClassifyBaseBean;
import com.project.community.ui.adapter.PageFragmentAdapter;
import com.project.community.ui.life.SearchActivity;
import com.project.community.util.TablayoutLineReflex;
import com.project.community.util.ToastUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/9/24.
 * 社区论坛
 */

public class BBSActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.bbs_tablayout)
    TabLayout bbsTablayout;
    @Bind(R.id.bbs_viewpager)
    ViewPager bbsViewpager;

    PageFragmentAdapter adapter;
    List<Fragment> fragmentList = new ArrayList<>();
    private ClassifyBaseBean classifylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs);
        ButterKnife.bind(this);
        ininData();
    }

    private void ininData() {



        initToolBar(mToolBar, mTvTitle, true, getString(R.string.bbs), R.mipmap.iv_back);

        getClassify();



    }

    private void getClassify() {

        progressDialog.show();
        serverDao.getClassifyList("livelihood_article_category", new JsonCallback<BaseResponse<ClassifyBaseBean>>() {
            @Override
            public void onSuccess(BaseResponse<ClassifyBaseBean> listBaseResponse, Call call, Response response) {

                progressDialog.dismiss();
                if (listBaseResponse.errNum.equals("0")) {

                    classifylist = listBaseResponse.retData;
                    setViewPage();

                } else {
                    ToastUtil.showToast(BBSActivity.this, response.message());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();

            }
        });

    }


    /**
     * 设置fragment
     */
    private void setViewPage() {

        List<String> titleID=new ArrayList<>();

        titleID.add("全部");
        fragmentList.add(BbsFragment.newInstance("全部"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("全部"));
        for (int i = 0; i < classifylist.getDictList().size(); i++) {
            titleID.add(classifylist.getDictList().get(i).getValue());
            fragmentList.add(BbsFragment.newInstance(classifylist.getDictList().get(i).getValue()));
            bbsTablayout.addTab(bbsTablayout.newTab().setText(classifylist.getDictList().get(i).getLabel()));
        }
        // 设置TabLayout模式
        bbsTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // 设置从PagerAdapter中获取Tab
        adapter = new PageFragmentAdapter(getSupportFragmentManager(), fragmentList);
        bbsViewpager.setOffscreenPageLimit(fragmentList.size());
        bbsViewpager.setAdapter(adapter);
        bbsViewpager.setOnPageChangeListener(this);
        bbsTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                bbsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        bbsTablayout.post(new Runnable() {
            @Override
            public void run() {
//                setIndicator(bbsTablayout,15,15);
                TablayoutLineReflex.setTabLine(BBSActivity.this, bbsTablayout, 15, 0);
            }
        });


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bbsTablayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = 0;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:

                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                Bundle bundle = new Bundle();
                bundle.putString("type", "mobile");
                bundle.putInt("index", 3);
                SearchActivity.startActivity(BBSActivity.this,bundle);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @OnClick(R.id.bbs_btn_send_message)
    public void onViewClicked() {
        Intent intent =new Intent(BBSActivity.this,SendMessageActivity.class);
        startActivity(intent);
    }
}

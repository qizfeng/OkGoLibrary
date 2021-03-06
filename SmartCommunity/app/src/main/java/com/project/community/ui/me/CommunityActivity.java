package com.project.community.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.customview.viewpager.NoScrollViewPager;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.Event.SetWorkBean;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/9/26.
 * 社区论坛
 */

public class CommunityActivity extends BaseActivity {

    @Bind(R.id.tv_serview_wait)
    TextView mTvServiewWait;
    @Bind(R.id.tv_serview_ing)
    TextView mTvServiewIng;
    @Bind(R.id.tv_serview_com)
    TextView mTvServiewComp;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
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
    @Bind(R.id.line_1)
    View line_1;
    @Bind(R.id.line_2)
    View line_2;
    @Bind(R.id.line_3)
    View line_3;

    @Bind(R.id.community_off)
    TextView community_off;
    @Bind(R.id.viewpager)
    NoScrollViewPager mViewpager;


    private List<Fragment> mFragmentsList;
    private MyFrageStatePagerAdapter mAdapter;
    Fragment mServiesWaitFragment;
    Fragment mServiesIngFragment;
    Fragment mServiesCompFragment;

    private boolean isType;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CommunityActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community2);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {


        mFragmentsList = new ArrayList<>();
        mServiesWaitFragment = new ServiesWaitFragment();
        mServiesIngFragment = new ServiesIngFragment();
        mServiesCompFragment = new ServiesCompFragment();
        mFragmentsList.add(mServiesWaitFragment);
        mFragmentsList.add(mServiesIngFragment);
        mFragmentsList.add(mServiesCompFragment);
        mAdapter = new MyFrageStatePagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setNoScroll(true);
        /**/
        mViewpager.setOffscreenPageLimit(3);  //*这一点需注意缓存.....****

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                WindowManager wm = getWindowManager();
//
//                int screeWidth = wm.getDefaultDisplay().getWidth();
//                int screeHeight = wm.getDefaultDisplay().getHeight();
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line
//                        .getLayoutParams();
//                lp.leftMargin = (int) (positionOffset * (screeWidth * 1.0 / 3) + position * (screeWidth / 3));
//                line.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetTextView(mTvServiewWait, line_1);
                        break;
                    case 1:
                        resetTextView(mTvServiewIng, line_2);
                        break;
                    case 2:
                        resetTextView(mTvServiewComp, line_3);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetTextView(TextView v, View line) {

        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line.setVisibility(View.VISIBLE);
        mTvServiewWait.setTextColor(getResources().getColor(R.color.color_gray_666666));
        mTvServiewIng.setTextColor(getResources().getColor(R.color.color_gray_666666));
        mTvServiewComp.setTextColor(getResources().getColor(R.color.color_gray_666666));
        v.setTextColor(getResources().getColor(R.color.login_bottom_txt));

    }

    //    手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        mViewpager.setCurrentItem(desTab, true);
    }

    @OnClick({R.id.ll_servers_wait, R.id.ll_servers_ing, R.id.ll_servers_commplet, R.id.community_finish, R.id.community_friends, R.id.community_off})
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
            case R.id.community_finish:
                finish();
                break;
            case R.id.community_friends:
                Intent intent = new Intent(this, MyActivity.class);
                startActivity(intent);
                break;
            case R.id.community_off:
                workSave();


                break;
        }
    }

    private void workSave() {

        serverDao.workSave(getUser(this).id, new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {

                if (listBaseResponse.errNum.equals("0")) {
                    ToastUtil.showToast(CommunityActivity.this, "打卡成功");
                    if (!isType) {
                        community_off.setText("上班");
                    } else {
                        community_off.setText("下班");
                    }
                    isType = !isType;

                } else {
                    ToastUtil.showToast(CommunityActivity.this, listBaseResponse.message);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

            }
        });


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetWorkBean event) {
        if (event.getWorkStatus().equals("1")) {
            community_off.setText("上班");
            isType = true;
        }
        if (event.getWorkStatus().equals("2")) {
            community_off.setText("下班");
            isType = false;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}

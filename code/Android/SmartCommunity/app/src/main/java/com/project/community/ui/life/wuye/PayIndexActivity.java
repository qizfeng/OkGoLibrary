package com.project.community.ui.life.wuye;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.PaymentWayModel;
import com.project.community.ui.adapter.PayWayAdapter;
import com.project.community.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/8/16.
 * 缴费首页
 */

public class PayIndexActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private PayWayAdapter mAdapter;
    private List<PaymentWayModel> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_index);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_payment), R.mipmap.iv_back);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        initData();
        mAdapter = new PayWayAdapter(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", mAdapter.getItem(position).payWay);
                PayHistroyActivity.startActivity(PayIndexActivity.this, bundle);
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initData() {
        mData = new ArrayList<>();
        PaymentWayModel model = new PaymentWayModel();
        model.res = R.mipmap.d20_icon1;
        model.payWay = getString(R.string.txt_payway_water);
        mData.add(model);

        model = new PaymentWayModel();
        model.res = R.mipmap.d20_icon2;
        model.payWay = getString(R.string.txt_payway_gas);
        mData.add(model);

        model = new PaymentWayModel();
        model.res = R.mipmap.d20_icon3;
        model.payWay = getString(R.string.txt_payway_cooling);
        mData.add(model);

        model = new PaymentWayModel();
        model.res = R.mipmap.d20_icon4;
        model.payWay = getString(R.string.txt_payway_electric);
        mData.add(model);

        model = new PaymentWayModel();
        model.res = R.mipmap.d20_icon5;
        model.payWay = getString(R.string.txt_payway_property);
        mData.add(model);

        model = new PaymentWayModel();
        model.res = R.mipmap.d20_icon6;
        model.payWay = getString(R.string.txt_payway_tv);
        mData.add(model);

    }
}

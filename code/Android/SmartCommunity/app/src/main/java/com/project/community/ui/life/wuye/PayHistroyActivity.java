package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.PaymentHistroyModel;
import com.project.community.ui.adapter.PaymentHistoryAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qizfeng on 17/8/21.
 * 缴费历史记录
 */

public class PayHistroyActivity extends BaseActivity {
    public static final int PAYMENT_TOP = 1;//缴费历史第一项标题展示
    public static final int PAYMENT_HISTROY = 2;//缴费历史

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.btn_add)
    Button mBtnAdd;
    private int type;//缴费类型
    private List<PaymentHistroyModel> mData = new ArrayList<>();
    private PaymentHistoryAdapter mAdapter;
    private String title;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PayHistroyActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            title = bundle.getString("title");
        }
        initToolBar(mToolBar, mTvTitle, true, title, R.mipmap.iv_back);

        mData = new ArrayList<>();
        PaymentHistroyModel modelTop = new PaymentHistroyModel(true, title, "", R.mipmap.d21_icon1);
        mData.add(modelTop);
        PaymentHistroyModel model1 = new PaymentHistroyModel(false, "", "1", PAYMENT_HISTROY, "房屋编号6575876");
        mData.add(model1);
        mData.add(model1);
        mData.add(model1);

        mAdapter = new PaymentHistoryAdapter(R.layout.layout_item_payhistory, R.layout.layout_item_payhistory_top, mData,
                new PaymentHistoryAdapter.OnAdapterItemClickListener() {
                    @Override
                    public void onDeleteClick(List<PaymentHistroyModel> list, int position) {
                        onDelete(position);
                    }

                    @Override
                    public void onItemClick(PaymentHistroyModel item, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("houseNo", mAdapter.getItem(position).payNo);
                        bundle.putString("title",title);
                        PayDetailActivity.startActivity(PayHistroyActivity.this, bundle);
                    }
                });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void onDelete(int position) {
        mData.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_add)
    public void onAddClick(View v) {
        AddHouseNoActivity.startActivity(this);
    }


}

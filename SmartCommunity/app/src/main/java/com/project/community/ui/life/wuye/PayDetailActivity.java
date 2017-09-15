package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.NewsModel;
import com.project.community.model.PaymentDetailHistoryModel;
import com.project.community.ui.adapter.PayHistoryAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qizfeng on 17/8/17.
 * 缴费详情
 */

public class PayDetailActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
   // @Bind(R.id.btn_pay)
    Button mBtnPay;
    EditText mEtMoeny;
    private View header;
    private String title;
    private PayHistoryAdapter mAdapter;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PayDetailActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            title = bundle.getString("title");
        }
        initToolBar(mToolBar, mTvTitle, true, title, R.mipmap.iv_back);
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_pay_history,null);
        mEtMoeny=(EditText) header.findViewById(R.id.et_money);
        if("物业费".equals(title)){
            mEtMoeny.setVisibility(View.GONE);
        }else {
            mEtMoeny.setVisibility(View.VISIBLE);
        }
        mBtnPay=(Button) header.findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                PaymentDialogActivity.startActivity(PayDetailActivity.this, bundle);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<PaymentDetailHistoryModel> data = new ArrayList<>();
        PaymentDetailHistoryModel model = new PaymentDetailHistoryModel();
        for (int i = 0; i < 6; i++)
            data.add(model);
        mAdapter = new PayHistoryAdapter(data, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(header);
    }

//    @OnClick(R.id.btn_pay)
    public void onPay(View v) {
        Bundle bundle = new Bundle();
        PaymentDialogActivity.startActivity(this, bundle);
    }

}

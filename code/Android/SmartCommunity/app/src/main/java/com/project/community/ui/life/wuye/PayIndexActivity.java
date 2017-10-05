package com.project.community.ui.life.wuye;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.PaymentWayModel;
import com.project.community.ui.adapter.PayWayAdapter;
import com.project.community.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

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
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this,2));
        initData();
        mAdapter = new PayWayAdapter(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view,final int position) {
                RxView.clicks(view)
                        .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                if(!isLogin(PayIndexActivity.this)){
                                    showToast(getString(R.string.toast_no_login));
                                    return;
                                }
                                if (position!=4){
                                    showToast("暂未开通");
                                    return;
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("title", mAdapter.getItem(position).label);
                                PayHistroyActivity.startActivity(PayIndexActivity.this, bundle);
                            }
                        });

            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initData() {
        serverDao.getPaymentWay(new DialogCallback<BaseResponse<List<PaymentWayModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<PaymentWayModel>> baseResponse, Call call, Response response) {
                mData = new ArrayList<PaymentWayModel>();
                mData.addAll(baseResponse.retData);
                mAdapter.setNewData(mData);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });

    }
}

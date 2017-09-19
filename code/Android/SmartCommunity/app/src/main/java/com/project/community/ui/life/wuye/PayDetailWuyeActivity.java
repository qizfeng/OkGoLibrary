package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.PaymentDetailHistoryModel;
import com.project.community.model.PaymentInfoModel;
import com.project.community.ui.adapter.PayHistoryAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/9/5.
 * 物业费详细信息
 */

public class PayDetailWuyeActivity extends BaseActivity {
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
    private TextView mTvInfo;
    private String title;
    private PayHistoryAdapter mAdapter;
    private List<PaymentDetailHistoryModel> mData = new ArrayList<>();
    private String houseNo;
    private String paymentId;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PayDetailWuyeActivity.class);
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
            houseNo = bundle.getString("houseNo");
        }
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_pay_wuye), R.mipmap.iv_back);
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_pay_history, null);
        mTvInfo = (TextView) header.findViewById(R.id.tv_info);
        mEtMoeny = (EditText) header.findViewById(R.id.et_money);
        mEtMoeny.setVisibility(View.GONE);
        mBtnPay = (Button) header.findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("paymentId", paymentId);
                PaymentDialogActivity.startActivity(PayDetailWuyeActivity.this, bundle);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new PayHistoryAdapter(mData, new RecycleItemClickListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        serverDao.getPaymentDetailInfo("5", houseNo, new DialogCallback<BaseResponse<PaymentInfoModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<PaymentInfoModel> baseResponse, Call call, Response response) {
                try {

                    if (baseResponse.retData.getPayment() == null) {
                        mTvInfo.setText(getString(R.string.txt_no_payment_info));
                        mBtnPay.setEnabled(false);
                        mBtnPay.setClickable(false);
                    } else {
                        String infoStr = "";
                        if (!TextUtils.isEmpty(baseResponse.retData.getPayment().getRoom().getRoomNo())) {
                            infoStr = "房屋编号" + baseResponse.retData.getPayment().getRoom().getRoomNo();
                        }
                        LogUtils.e("address:" + baseResponse.retData.getPayment().getRoom().getAddress());
                        if (!TextUtils.isEmpty(baseResponse.retData.getPayment().getRoom().getAddress())) {
                            infoStr = infoStr + "\n" + baseResponse.retData.getPayment().getRoom().getAddress();
                        }
                        LogUtils.e("money:" + baseResponse.retData.getPayment().getPayMoney());
                        if (!TextUtils.isEmpty(baseResponse.retData.getPayment().getPayMoney())) {
                            infoStr = infoStr + "\n应缴金额（元）：" + baseResponse.retData.getPayment().getPayMoney();
                        }
                        LogUtils.e("money:" + baseResponse.retData.getPayment().getPayMouth());
                        if (!TextUtils.isEmpty(baseResponse.retData.getPayment().getPayMouth())) {
                            infoStr = infoStr + "\n到期时间:" + baseResponse.retData.getPayment().getPayMouth();
                        }
                        mTvInfo.setText(infoStr);
                        paymentId = baseResponse.retData.getPayment().getId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mData = new ArrayList<>();
                mData.addAll(baseResponse.retData.getPaymentList());
                mAdapter.setNewData(mData);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }

            @Override
            public void onAfter(@Nullable BaseResponse<PaymentInfoModel> baseResponse, @Nullable Exception e) {
                super.onAfter(baseResponse, e);
                try {
                    AddHouseNoActivity.getInstance().finish();
//                    AddHouseNoActivity.getInstance().finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}

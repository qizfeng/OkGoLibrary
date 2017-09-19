package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.PaymentHouseHistroyModel;
import com.project.community.ui.adapter.PaymentHistoryAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    TextView mBtnAdd;
    private int type;//缴费类型
    private List<PaymentHouseHistroyModel> mData = new ArrayList<>();
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
        loadData();
        mAdapter = new PaymentHistoryAdapter(R.layout.layout_item_payhistory, R.layout.layout_item_payhistory_top, mData,
                new PaymentHistoryAdapter.OnAdapterItemClickListener() {
                    @Override
                    public void onDeleteClick(List<PaymentHouseHistroyModel> list, int position) {
                        onDelete(position);
                    }

                    @Override
                    public void onItemClick(PaymentHouseHistroyModel item, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("houseNo", mAdapter.getItem(position).room.getRoomNo());
                        bundle.putString("title", title);
                        if ("物业费".equals(title))
                            PayDetailWuyeActivity.startActivity(PayHistroyActivity.this, bundle);
                        else
                            PayDetailActivity.startActivity(PayHistroyActivity.this, bundle);
                    }
                });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        serverDao.getPaymentNoData(getUser(this).id, new DialogCallback<BaseResponse<List<PaymentHouseHistroyModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<PaymentHouseHistroyModel>> baseResponse, Call call, Response response) {
                mData = new ArrayList<>();
                PaymentHouseHistroyModel modelTop = new PaymentHouseHistroyModel(true, title, "", R.mipmap.d21_icon1);
                mData.add(modelTop);
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


    /**
     * 删除
     *
     * @param position
     */
    private void onDelete(final int position) {
        serverDao.deleteHouse(getUser(this).id, mAdapter.getItem(position).id, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
                showToast(baseResponse.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });

    }

    @OnClick(R.id.btn_add)
    public void onAddClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        AddHouseNoActivity.startActivity(this,bundle);
    }
}

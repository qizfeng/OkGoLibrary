package com.project.community.ui.life.minsheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.project.community.Event.AddGoodsEvent;
import com.project.community.Event.CartRefreshEvent;
import com.project.community.Event.ChangeAddressEvent;
import com.project.community.Event.DefaultAddressEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.GoodsModel;
import com.project.community.model.MerchantDeailModel;
import com.project.community.model.OrderModel;
import com.project.community.ui.adapter.GoodsOrderDetailApdater;
import com.project.community.ui.me.MyAddressActivity;
import com.project.community.ui.me.all_order.GoodsOrderActivity;
import com.project.community.ui.me.all_order.MyOrderActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PayActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.goods_order_tv_name)
    TextView goodsOrderTvName;
    @Bind(R.id.goods_order_tv_phone)
    TextView goodsOrderTvPhone;
    @Bind(R.id.goods_order_tv_address)
    TextView goodsOrderTvAddress;
    @Bind(R.id.goods_order_tv_shop_name)
    TextView goodsOrderTvShopName;
    @Bind(R.id.goods_order_rv_order)
    RecyclerView goodsOrderRvOrder;
    @Bind(R.id.goods_order_tv_price)
    TextView goodsOrderTvPrice;
    @Bind(R.id.goods_order_tv_jiaoyidanhao)
    TextView goodsOrderTvJiaoyidanhao;
    @Bind(R.id.goods_order_tv_xiadan_time)
    TextView goodsOrderTvXiadanTime;
    @Bind(R.id.price)
    TextView price;

    List<GoodsModel> list =new ArrayList<>();

    private GoodsOrderDetailApdater mAdapter;//商品详情订单适配器

    private OrderModel merchantDeailModel;

    private String addressId="";
    private String orderNo="";


    public static void startActivity(Context context,OrderModel merchantDeailModel){
        Intent intent = new Intent(context,PayActivity.class);
        intent.putExtra("cj",merchantDeailModel);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.apply_sale_pay), R.mipmap.iv_back);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {

        merchantDeailModel= (OrderModel) getIntent().getExtras().getSerializable("cj");
        list.clear();
        list.addAll(merchantDeailModel.detailList);

        if (merchantDeailModel.address!=null){
            goodsOrderTvShopName.setText(merchantDeailModel.address.consignee);

            goodsOrderTvName.setText(getString(R.string.my_order_address_apply_shouhuoren)+merchantDeailModel.address.consignee);
            goodsOrderTvPhone.setText(merchantDeailModel.address.contactPhone);
            goodsOrderTvAddress.setText(merchantDeailModel.address.address);
            addressId=merchantDeailModel.address.id;
        }else {
            goodsOrderTvName.setText(getString(R.string.my_order_address_apply_shouhuoren));

        }


        orderNo=merchantDeailModel.orderNo;

        price.setText("¥"+merchantDeailModel.orderAmountTotal);
        goodsOrderTvPrice.setText("¥"+merchantDeailModel.orderAmountTotal);
//        goodsOrderTvJiaoyidanhao.setText(getResources().getString(R.string.goods_order_tv_jiaoyidanhao)+merchantDeailModel.orderNo);
//        goodsOrderTvXiadanTime.setText(getResources().getString(R.string.goods_order_tv_xiadan_time)+merchantDeailModel.createDate);


        goodsOrderRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GoodsOrderDetailApdater(list, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onCustomClick(View view, int position) {
            }
        });
        goodsOrderRvOrder.setAdapter(mAdapter);

    }

    @OnClick({R.id.ll_changge_address, R.id.layout_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_changge_address:
                Intent intent = new Intent(PayActivity.this, MyAddressActivity.class);
                intent.putExtra("cj",1);
                startActivity(intent);
                break;
            case R.id.layout_buy:
//                delData();

                JSONArray jsonArray = new JSONArray();
                JSONArray cartList = new JSONArray();
                JSONObject jsonObject = new JSONObject();
//                BigDecimal bigDecimal = new BigDecimal("0");
                for (int i = 0; i < list.size(); i++) {
//                    BigDecimal bigDecimal1=new BigDecimal(list.get(i).goodsPrice).multiply(new BigDecimal(list.get(i).goodsCount));
//                    bigDecimal=bigDecimal.add(bigDecimal1);
                    JSONObject item = new JSONObject();
                    try {
                        item.put("goodId",list.get(i).goodId);
                        item.put("number",list.get(i).number);
                        item.put("goodName",list.get(i).goodName);
                        item.put("goodPrice",list.get(i).goodPrice);
                        item.put("goodImage", AppConstants.URL_BASE +list.get(i).goodImage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cartList.put(item);
                }

                try {
                    jsonObject.put("orderAmountTotal",merchantDeailModel.orderAmountTotal);
                    jsonObject.put("shopId",merchantDeailModel.shopId);
                    jsonObject.put("addressId",addressId);
                    jsonObject.put("userId",getUser(this).id);
                    jsonObject.put("goodsCount",merchantDeailModel.goodsCount);
                    jsonObject.put("cartList",cartList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);

                Log.e("onCartClick: ", jsonArray.toString());
//                commitOrder(jsonArray.toString());

                commitOrder(jsonArray.toString());
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChangeAddressEvent def) {
        goodsOrderTvName.setText(getString(R.string.my_order_address_apply_shouhuoren)+def.getAddressListBean().getConsignee());
        goodsOrderTvPhone.setText(def.getAddressListBean().getContactPhone());
        goodsOrderTvAddress.setText(def.getAddressListBean().getUserArea() + def.getAddressListBean().getAddress());
        addressId=def.getAddressListBean().getId();
    }

    private void getData(){

        merchantDeailModel= (OrderModel) getIntent().getExtras().getSerializable("cj");
        list.clear();
        list.addAll(merchantDeailModel.detailList);
        mAdapter.notifyDataSetChanged();

        goodsOrderTvShopName.setText(merchantDeailModel.address.consignee);

        goodsOrderTvName.setText(getString(R.string.my_order_address_apply_shouhuoren)+merchantDeailModel.address.consignee);
        goodsOrderTvPhone.setText(merchantDeailModel.address.contactPhone);
        goodsOrderTvAddress.setText(merchantDeailModel.address.address);
        addressId=merchantDeailModel.address.id;

        orderNo=merchantDeailModel.orderNo;

        price.setText("¥"+merchantDeailModel.orderAmountTotal);
        goodsOrderTvPrice.setText("¥"+merchantDeailModel.orderAmountTotal);
        goodsOrderTvJiaoyidanhao.setText(getResources().getString(R.string.goods_order_tv_jiaoyidanhao)+merchantDeailModel.orderNo);
        goodsOrderTvXiadanTime.setText(getResources().getString(R.string.goods_order_tv_xiadan_time)+merchantDeailModel.createDate);

    }


    /**
     * D确认订单
     */
    private void delData(){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.submit(
                getUser(this).id,addressId,orderNo,new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
                EventBus.getDefault().post(new CartRefreshEvent(""));
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * D56订单提交
     */
    private void commitOrder(String orderJson){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.commitOrder(orderJson,new JsonCallback<BaseResponse<OrderModel>>() {
            @Override
            public void onSuccess(BaseResponse<OrderModel> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
//                if (listBaseResponse.retData.address==null) startActivity(new Intent(PayActivity.this, MyAddressActivity.class));
//                else PayActivity.startActivity(PayActivity.this,listBaseResponse.retData);

                EventBus.getDefault().post(new CartRefreshEvent(""));
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                showToast(e.getMessage());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

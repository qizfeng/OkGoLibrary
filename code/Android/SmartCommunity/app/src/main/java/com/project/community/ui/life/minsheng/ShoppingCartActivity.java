package com.project.community.ui.life.minsheng;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.Event.ChangeAddressEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.GoodsManagerModel;
import com.project.community.model.GoodsModel;
import com.project.community.model.OrderModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.adapter.ShoppingCartAdapter;
import com.project.community.ui.me.MyAddressActivity;
import com.project.community.ui.me.shop_management.ShopDataActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.view.VpSwipeRefreshLayout;

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
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/10/17.
 * 购物车
 */

public class ShoppingCartActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private ShoppingCartAdapter mAdapter;
    private List<ShoppingCartModel> mData = new ArrayList<>();

    private Dialog mDialog;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ShoppingCartActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_shopping_cart), R.mipmap.iv_back);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
//        mData = getListData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShoppingCartAdapter(this, mData, new ShoppingCartAdapter.OnGoodsClickListener() {
            @Override
            public void onGoodsItemClick(View view,int parentPosition, int childPosition) {

            }
        }, new ShoppingCartAdapter.OnFooterClickListener() {
            @Override
            public void onFooterDeleteClick(View view, int position) {
                switch (view.getId()){
                    case R.id.tv_delete:
                        showAlertDialog(position);
                        break;
                }
            }

            @Override
            public void onSettlementClick(View view, int position) {
                JSONArray jsonArray = new JSONArray();
                JSONArray cartList = new JSONArray();
                JSONObject jsonObject = new JSONObject();
//                         BigDecimal bigDecimal = new BigDecimal("0");
                for (int i = 0; i < mData.get(position).shop.goods.size(); i++) {
//                             BigDecimal bigDecimal1=new BigDecimal(mData.get(position).shop.goods.get(i).number).add(new BigDecimal(mCartData.get(i).goodsCount));
//                             bigDecimal=bigDecimal.add(new BigDecimal(mData.get(position).shop.goods.get(i).number));
                    JSONObject item = new JSONObject();
                    try {
                        item.put("goodId",mData.get(position).shop.goods.get(i).id);
                        item.put("number",mData.get(position).shop.goods.get(i).number);
                        item.put("goodName",mData.get(position).shop.goods.get(i).name);
                        item.put("goodPrice",mData.get(position).shop.goods.get(i).price);
                        item.put("goodImage", AppConstants.URL_BASE +mData.get(position).shop.goods.get(i).images);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cartList.put(item);
                }

                try {
                    jsonObject.put("orderAmountTotal",mData.get(position).shop.totalCost);
                    jsonObject.put("shopId",mData.get(position).shop.id);
                    jsonObject.put("userId",getUser(ShoppingCartActivity.this).id);
                    jsonObject.put("goodsCount",mData.get(position).goodsCount);
                    jsonObject.put("cartList",cartList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);

                Log.e("onCartClick: ", jsonArray.toString());
                commitOrder(jsonArray.toString());
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        getData();
    }

    /**
     * 获取购物车列表
     */
    private void getData(){

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }
        serverDao.getCartList(
                getUser(this).id,
                new JsonCallback<BaseResponse<List<ShoppingCartModel>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<ShoppingCartModel>> listBaseResponse, Call call, Response response) {
                        dismissDialog();
//                        showToast(listBaseResponse.message);
                        mData.clear();
                        mData.addAll(listBaseResponse.retData);
//                        BigDecimal bigDecimal = new BigDecimal("0");
                        for (int i = 0; i < mData.size(); i++) {
                            int num=0;
                            for (int j = 0; j < mData.get(i).shop.goods.size(); j++) {
                                num+=mData.get(i).shop.goods.get(j).number;
                            }
                            mData.get(i).goodsCount=num;
                        }
                        mAdapter.notifyDataSetChanged();
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
     * 删除
     */
    private void delData(String shopid, final int position){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        Log.e( "delData--->", getUser(this).id);
        Log.e( "delData--->", shopid);

        serverDao.delCart(getUser(this).id,"",shopid,new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
                mDialog.dismiss();
                mAdapter.removeGroup(position);
                mData.remove(position);
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
     * 删除提示窗口
     *
     * @param position
     */

    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(R.string.txt_confirm_delete);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delData(mData.get(position).shop.id,position);
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
                if (listBaseResponse.retData.address==null) startActivity(new Intent(ShoppingCartActivity.this, MyAddressActivity.class));
                else PayActivity.startActivity(ShoppingCartActivity.this,listBaseResponse.retData);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChangeAddressEvent def) {
        finish();
    }
}

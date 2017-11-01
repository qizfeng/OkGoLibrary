package com.project.community.ui.me.all_order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.model.GoodsModel;
import com.project.community.model.OrderModel;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.AllOrderApdater;
import com.project.community.ui.adapter.ArticleDetailsImagsAdapter;
import com.project.community.ui.adapter.GoodsOrderCommentApdater;
import com.project.community.ui.adapter.GoodsOrderDetailApdater;
import com.project.community.ui.adapter.ProductSellApdater;
import com.project.community.ui.me.shop_management.AllProductsActivity;
import com.project.community.ui.me.shop_management.ShopDataActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.view.MyButton;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/10/24.
 * 商品订单详情
 */

public class GoodsOrderActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.goods_order_tv_type)
    TextView goodsOrderTvType;
    @Bind(R.id.goods_order_view_type_1)
    View goodsOrderViewType1;
    @Bind(R.id.goods_order_view_type_left)
    View goodsOrderViewTypeLeft;
    @Bind(R.id.goods_order_view_type_2)
    View goodsOrderViewType2;
    @Bind(R.id.goods_order_view_type_right)
    View goodsOrderViewTypeRight;
    @Bind(R.id.goods_order_view_type_3)
    View goodsOrderViewType3;
    @Bind(R.id.goods_order_tv_type_1)
    TextView goodsOrderTvType1;
    @Bind(R.id.goods_order_tv_type_2)
    TextView goodsOrderTvType2;
    @Bind(R.id.goods_order_tv_type_3)
    TextView goodsOrderTvType3;
    @Bind(R.id.goods_order_tv_name)
    TextView goodsOrderTvName;
    @Bind(R.id.goods_order_tv_address)
    TextView goodsOrderTvAddress;
    @Bind(R.id.goods_order_tv_shop_name)
    TextView goods_order_tv_shop_name;//店铺名称
    @Bind(R.id.goods_order_tv_phone)
    TextView goodsOrderTvPhone;
    @Bind(R.id.goods_order_rv_order)
    RecyclerView goodsOrderRvOrder;
    @Bind(R.id.goods_order_tv_reason)
    TextView goodsOrderTvReason;//原因
    @Bind(R.id.goods_order_tv_pingzheng)
    TextView goods_order_tv_pingzheng;//凭证
    @Bind(R.id.goods_order_rv_pingzheng)
    GridView goodsOrderRvPingzheng;
    @Bind(R.id.goods_order_tv_order_type)
    TextView goods_order_tv_order_type;//订单状态
    @Bind(R.id.goods_order_tv_price)
    TextView goodsOrderTvPrice;
    @Bind(R.id.goods_order_tv_type4)
    TextView goodsOrderTvType4;
    @Bind(R.id.goods_order_tv_jiaoyidanhao)
    TextView goodsOrderTvJiaoyidanhao;
    @Bind(R.id.goods_order_tv_xiadan_time)
    TextView goodsOrderTvXiadanTime;
    @Bind(R.id.goods_order_btn_type1)
    TextView goods_order_btn_type1;
    @Bind(R.id.goods_order_btn_type2)
    TextView goods_order_btn_type2;
    @Bind(R.id.goods_order_btn_type3)
    TextView goods_order_btn_type3;
    @Bind(R.id.goods_order_ll_pinglun)
    LinearLayout goods_order_ll_pinglun;//评论列表
    @Bind(R.id.goods_order_rv_pinglun)
    RecyclerView goods_order_rv_pinglun;
    @Bind(R.id.goods_order_shouhou_type1)
    TextView goods_order_shouhou_type1;
    @Bind(R.id.goods_order_shouhou_type2)
    LinearLayout goods_order_shouhou_type2;
    @Bind(R.id.goods_order_shouhou_type3)
    RelativeLayout goods_order_shouhou_type3;
    /**
     * 发货
     */
    @Bind(R.id.goods_order_shouhou_type4)
    LinearLayout goods_order_shouhou_type4;
    @Bind(R.id.goods_order_shouhou_type4_tv1)
    TextView goods_order_shouhou_type4_tv1;
    @Bind(R.id.goods_order_shouhou_type4_tv2)
    TextView goods_order_shouhou_type4_tv2;
    @Bind(R.id.goods_order_shouhou_type4_tv3)
    TextView goods_order_shouhou_type4_tv3;


    private OrderModel item;
    private int code;//1商铺订单详情

    private String status;//0:未发货:1：已发货:2：已完成,3：退货申请(售后),4：退货中,5：已退货,


    private GoodsOrderDetailApdater mAdapter;//商品详情订单适配器
    private GoodsOrderCommentApdater mCommentAdapter;//商品详情订单评论适配器
    private Dialog mDialog;
    List<GoodsModel> list =new ArrayList<>();
    List<CommentModel> mCommentList =new ArrayList<>();

    ArticleDetailsImagsAdapter grid_photoAdapter; //凭证的适配器
    private List<String> mImages = new ArrayList<>();

    public static void startActivity(Context context,OrderModel item,int code){
        Intent intent = new Intent(context,GoodsOrderActivity.class);
        intent.putExtra("item",item);
        intent.putExtra("code",code);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        item= (OrderModel) getIntent().getSerializableExtra("item");
        code = getIntent().getIntExtra("code",0);
        status=item.orderStatus;
        if (status.equals("3")) initToolBar(mToolBar, mTvTitle, true, getString(R.string.apply_sale_detail), R.mipmap.iv_back);
        else initToolBar(mToolBar, mTvTitle, true, getString(R.string.goods_order_title), R.mipmap.iv_back);
        list.addAll(item.detailList);

        goodsOrderTvName.setText(getResources().getString(R.string.my_order_address_apply_shouhuoren)+item.address.consignee);
        goodsOrderTvAddress.setText(item.address.address);
        goodsOrderTvPhone.setText(item.address.contactPhone);
        goods_order_tv_shop_name.setText(item.shopsName);
        goodsOrderTvPrice.setText(getResources().getString(R.string.money)+item.orderAmountTotal);

        switch (item.orderStatus){//0:未发货:1：已发货:2：已完成,3：退货申请(售后),4：退货中,5：已退货,
            case "0":
                goodsOrderTvType4.setText(getString(R.string.my_order_wait_fahuo));
                break;
            case "1":
                goodsOrderTvType4.setText(getString(R.string.my_order_end));
                break;
            case "2":
                if (item.isComment==0) goodsOrderTvType4.setText(getString(R.string.my_order_wait_pingjia));
                else goodsOrderTvType4.setText(getString(R.string.my_order_wait_yipingjia));
                break;
            case "4":
                goodsOrderTvType4.setText(getString(R.string.my_order_address_apply_safe_status_ing));
                break;
            case "5":
                goodsOrderTvType4.setText(getString(R.string.my_order_address_apply_safe_status_end));
                break;


        }

        goodsOrderTvJiaoyidanhao.setText(getResources().getString(R.string.goods_order_tv_jiaoyidanhao)+item.orderNo);
        goodsOrderTvXiadanTime.setText(getResources().getString(R.string.goods_order_tv_xiadan_time)+item.createDate);

        switch (status){

            case "0"://待发货
                goods_order_shouhou_type1.setVisibility(View.GONE);
                goods_order_ll_pinglun.setVisibility(View.GONE);

                goodsOrderViewTypeRight.setBackgroundColor(getResources().getColor(R.color.color_gray_eeeeee));
                goodsOrderViewTypeLeft.setBackgroundColor(getResources().getColor(R.color.color_gray_eeeeee));
                goodsOrderViewType3.setBackgroundResource(R.drawable.dot_goods_type_hui);
                goodsOrderViewType2.setBackgroundResource(R.drawable.dot_goods_type_hui);
                goodsOrderTvType2.setTextColor(getResources().getColor(R.color.color_gray_cccccc));
                goodsOrderTvType3.setTextColor(getResources().getColor(R.color.color_gray_cccccc));

                goodsOrderRvPingzheng.setVisibility(View.GONE);
                goodsOrderTvReason.setVisibility(View.GONE);
                goods_order_tv_pingzheng.setVisibility(View.GONE);
                goods_order_ll_pinglun.setVisibility(View.GONE);

                goods_order_btn_type1.setVisibility(View.GONE);
                goods_order_btn_type2.setText(getResources().getString(R.string.my_order_address_lianxishangjia));
                goods_order_btn_type3.setText(getResources().getString(R.string.my_order_address_cacel_order));
                goodsOrderTvType.setText(getResources().getString(R.string.my_order_address_wait_fahuo));

                if (code==1){
                    goods_order_shouhou_type4.setVisibility(View.VISIBLE);
                    goods_order_shouhou_type4_tv2.setVisibility(View.GONE);
                    goods_order_shouhou_type4_tv3.setVisibility(View.GONE);

                }

                break;
            case "1"://已发货
                goods_order_shouhou_type1.setVisibility(View.GONE);
                goods_order_tv_shop_name.setVisibility(View.VISIBLE);
                goodsOrderRvPingzheng.setVisibility(View.GONE);
                goodsOrderTvReason.setVisibility(View.GONE);
                goods_order_tv_pingzheng.setVisibility(View.GONE);
                goods_order_tv_order_type.setVisibility(View.GONE);

                goods_order_ll_pinglun.setVisibility(View.GONE);

                goods_order_btn_type1.setVisibility(View.GONE);
                goods_order_btn_type2.setVisibility(View.GONE);
                goods_order_btn_type3.setText(getResources().getString(R.string.my_order_address_querenshouhuo));
                goodsOrderViewTypeRight.setBackgroundColor(getResources().getColor(R.color.color_gray_eeeeee));
                goodsOrderViewType3.setBackgroundResource(R.drawable.dot_goods_type_hui);
                goodsOrderTvType3.setTextColor(getResources().getColor(R.color.color_gray_cccccc));
                if (code==1){
                    goods_order_btn_type3.setText(getResources().getString(R.string.my_order_yichuli));
                }
                break;
            case "2"://待评价
                if (item.isComment==0){
                    goods_order_shouhou_type1.setVisibility(View.GONE);
                    goods_order_tv_shop_name.setVisibility(View.VISIBLE);
                    goodsOrderRvPingzheng.setVisibility(View.GONE);
                    goodsOrderTvReason.setVisibility(View.GONE);
                    goods_order_tv_pingzheng.setVisibility(View.GONE);

                    goods_order_tv_order_type.setText("");
                    goodsOrderTvType.setTextColor(getResources().getColor(R.color.color_gray_666666));
                    goods_order_btn_type1.setText(getResources().getString(R.string.my_order_address_pinglun));
                    goods_order_btn_type2.setText(getResources().getString(R.string.my_order_address_del_order));
                    goods_order_btn_type3.setText(getResources().getString(R.string.my_order_address_apply_safe));
                }

                break;
            case "3"://售后
                goods_order_shouhou_type1.setVisibility(View.VISIBLE);
                goods_order_shouhou_type2.setVisibility(View.GONE);
                goods_order_shouhou_type3.setVisibility(View.GONE);
                goods_order_tv_shop_name.setVisibility(View.GONE);
                goods_order_tv_order_type.setVisibility(View.GONE);
                goods_order_ll_pinglun.setVisibility(View.GONE);


                goodsOrderTvReason.setVisibility(View.VISIBLE);
                goods_order_tv_pingzheng.setVisibility(View.VISIBLE);
                goodsOrderRvPingzheng.setVisibility(View.VISIBLE);

                goodsOrderTvReason.setText(item.sale.content);


                if (code==1){
                    goods_order_shouhou_type4.setVisibility(View.VISIBLE);
                    goods_order_shouhou_type4_tv1.setVisibility(View.GONE);

                }

                break;

        }
        goodsOrderRvOrder.setLayoutManager(new LinearLayoutManager(this));
//        for (int i = 0; i < 2; i++) {
//            CommentModel commentModel =new CommentModel();
//            commentModel.id="0";
//            commentModel.rating=2.5f;
//            list.add(commentModel);
//            mCommentList.add(commentModel);
//        }
        if (status.equals("2")){
            goods_order_rv_pinglun.setLayoutManager(new LinearLayoutManager(this));
            mCommentAdapter=new GoodsOrderCommentApdater(mCommentList, new RecycleItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onCustomClick(View view, int position) {
                    showAlertDialog(position);
                }
            });
            goods_order_rv_pinglun.setAdapter(mCommentAdapter);
        }

        mAdapter = new GoodsOrderDetailApdater(list, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onCustomClick(View view, int position) {
            }
        });
        goodsOrderRvOrder.setAdapter(mAdapter);


        for (int i = 0; i < 3; i++) {
            mImages.add("");
        }
        grid_photoAdapter=new ArticleDetailsImagsAdapter(this,mImages);
        goodsOrderRvPingzheng.setAdapter(grid_photoAdapter);
    }

    @OnClick({R.id.goods_order_btn_type1,R.id.goods_order_btn_type2,R.id.goods_order_btn_type3,})
    public void onViewClicked(View view) {
        Intent mIntent;
        switch (status){
            case "0"://待发货
                switch (view.getId()){
                    case R.id.goods_order_btn_type2:
                        mIntent = new Intent(this, PhoneDialogActivity.class);
                        mIntent.putExtra("hasHeader", false);
                        mIntent.putExtra("type", item.shopsPhone);
                        startActivity(mIntent);
                        break;
                    case R.id.goods_order_btn_type3:
                        showWindomDialog(1);
                        break;
                }
                break;
            case "1"://:1：已发货
                switch (view.getId()){
                    case R.id.goods_order_btn_type3:
                        if (code==0)
                            showWindomDialog(2);
                        break;
                }
                break;
            case "2"://2：已完成
                if (item.isComment==0)
                    switch (view.getId()){
                        case R.id.goods_order_btn_type1:
                            TakeDeliveryOfGoodsActivity.startActivity(this,item);
                            break;
                        case R.id.goods_order_btn_type2:
                            showWindomDialog(0);
                            break;
                        case R.id.goods_order_btn_type3:
                            ApplySaleActivity.startActivity(this,item);
                            break;
                    }
                else
                    switch (view.getId()){
                        case R.id.goods_order_btn_type3:

                            break;
                    }
                break;
            default:
                switch (view.getId()){
                    case R.id.goods_order_btn_type3:
//                                ApplySaleActivity.startActivity(getActivity());
                        break;
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && requestCode == 100 && resultCode == 100){
            CommentModel commentModel = (CommentModel) data.getSerializableExtra("value");
            mCommentList.add(commentModel);
            mCommentAdapter.notifyItemInserted(mCommentList.size()-1);
        }
    }

    /**
     * 删除成员对话框
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
                mDialog.dismiss();
                mCommentList.remove(position);
                mCommentAdapter.notifyItemRemoved(position);
            }
        });
    }



    /**
     * D61售后详情
     */
    private void commitOrder(String orderNo){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.getDetail(
                getUser(this).id,
                orderNo,
                new JsonCallback<BaseResponse<OrderModel>>() {
            @Override
            public void onSuccess(BaseResponse<OrderModel> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
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
     * D55取消订单
     */

    private void cacelOrder(String orderNo) {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.cacelOrder(
                getUser(this).id,
                orderNo,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
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
     * D55删除订单
     */

    private void deleteOrder(String orderNo) {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.deleteOrder(
                getUser(this).id,
                orderNo,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
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
     * D55确认收货
     */

    private void complete(String orderNo) {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.complete(
                getUser(this).id,
                orderNo,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
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
     * 0删除,1其他取消订单 2 确认收货  提示窗口
     *
     * @param code
     */

    private Dialog mWindomDialog;
    public void showWindomDialog(final int code) {
        mWindomDialog = new Dialog(this);
        mWindomDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mWindomDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mWindomDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        if (code==0) tv_content.setText(R.string.txt_confirm_detlet_order);
        else if (code==1) tv_content.setText(R.string.txt_confirm_cancel);
        else if (code==2) tv_content.setText(R.string.txt_confirm_shouhuo);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindomDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindomDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindomDialog.dismiss();
                if (code==0){
                    deleteOrder(item.orderNo);
                }else if (code==1){
                    cacelOrder(item.orderNo);
                }else if (code==2){
                    complete(item.orderNo);
                }


            }
        });
    }



}

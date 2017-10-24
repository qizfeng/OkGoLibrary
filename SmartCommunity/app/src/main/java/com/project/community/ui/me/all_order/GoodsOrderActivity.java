package com.project.community.ui.me.all_order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.AllOrderApdater;
import com.project.community.ui.adapter.ArticleDetailsImagsAdapter;
import com.project.community.ui.adapter.GoodsOrderDetailApdater;
import com.project.community.ui.adapter.ProductSellApdater;
import com.project.community.ui.me.shop_management.AllProductsActivity;
import com.project.community.ui.me.shop_management.ShopDataActivity;
import com.project.community.view.MyButton;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.goods_order_tv_phone)
    TextView goodsOrderTvPhone;
    @Bind(R.id.goods_order_rv_order)
    RecyclerView goodsOrderRvOrder;
    @Bind(R.id.goods_order_tv_reason)
    TextView goodsOrderTvReason;
    @Bind(R.id.goods_order_rv_pingzheng)
    GridView goodsOrderRvPingzheng;
    @Bind(R.id.goods_order_tv_price)
    TextView goodsOrderTvPrice;
    @Bind(R.id.goods_order_tv_type4)
    TextView goodsOrderTvType4;
    @Bind(R.id.goods_order_tv_jiaoyidanhao)
    TextView goodsOrderTvJiaoyidanhao;
    @Bind(R.id.goods_order_tv_xiadan_time)
    TextView goodsOrderTvXiadanTime;
    @Bind(R.id.goods_order_btn_type)
    MyButton goodsOrderBtnType;

    private GoodsOrderDetailApdater mAdapter;
    List<CommentModel> comments =new ArrayList<>();

    ArticleDetailsImagsAdapter grid_photoAdapter; //凭证的适配器
    private List<String> mImages = new ArrayList<>();

    public static void startActivity(Context context){
        Intent intent = new Intent(context,GoodsOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.goods_order_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        goodsOrderRvOrder.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 2; i++) {
            CommentModel commentModel =new CommentModel();
            commentModel.id="0";
            comments.add(commentModel);
        }
        mAdapter = new GoodsOrderDetailApdater(comments, new RecycleItemClickListener() {
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

    @OnClick(R.id.goods_order_btn_type)
    public void onViewClicked() {
    }
}

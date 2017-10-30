package com.project.community.ui.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.model.GoodsModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qizfeng on 17/10/18.
 */

public class MerchantCartPopwinAdapter extends BaseQuickAdapter<GoodsModel, BaseViewHolder> {
    public OnMerchantCartItemClickListener onMerchantCartItemClickListener;//点击事件

    public MerchantCartPopwinAdapter(List<GoodsModel> data, OnMerchantCartItemClickListener onMerchantItemClickListener) {
        super(R.layout.layout_item_merchant_cart, data);
        this.onMerchantCartItemClickListener = onMerchantItemClickListener;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final GoodsModel model) {
        final ImageView iv_minus = baseViewHolder.getView(R.id.iv_minus);
        final ImageView iv_add = baseViewHolder.getView(R.id.iv_add);
        final TextView tv_count = baseViewHolder.getView(R.id.tv_count);
        final int position = baseViewHolder.getLayoutPosition();
        final TextView tv_price = baseViewHolder.getView(R.id.tv_price);
        //        new GlideImageLoader().onDisplayImageWithDefault(mContext,headeCover,listBaseResponse.retData.shop.shopPhoto,R.mipmap.c1_image2);
        tv_count.setVisibility(View.VISIBLE);
        iv_minus.setVisibility(View.VISIBLE);
        tv_count.setText(""+model.goodsCount);
        BigDecimal bigDecimal = new BigDecimal(model.goodsPrice);
//        bigDecimal.multiply(new BigDecimal(model.goodsCount));
//        tv_price.setText("¥" + (model.goodsPrice * model.goodsCount));
        tv_price.setText("¥" + bigDecimal.multiply(new BigDecimal(model.goodsCount)).toString());
        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMerchantCartItemClickListener.onMinusClick(iv_minus, tv_count, tv_price, position);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMerchantCartItemClickListener.onAddClick(iv_add, tv_count, iv_minus, tv_price, position);
            }
        });

    }

    public interface OnMerchantCartItemClickListener {
        void onMinusClick(View view, TextView tv_count, TextView tv_price, int position);

        void onAddClick(View view, TextView tv_count, ImageView iv_minus, TextView tv_price, int position);
    }

}


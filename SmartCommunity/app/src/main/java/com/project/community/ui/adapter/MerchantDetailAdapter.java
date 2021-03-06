package com.project.community.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hedgehog.ratingbar.RatingBar;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.model.GoodsModel;

import java.util.List;

/**
 * Created by zipingfang on 17/10/18.
 */

public class MerchantDetailAdapter extends BaseQuickAdapter<GoodsModel, BaseViewHolder> {
    private OnMerchantItemClickListener onMerchantItemClickListener;

    public MerchantDetailAdapter(List<GoodsModel> data, OnMerchantItemClickListener onMerchantItemClickListener) {
        super(R.layout.layout_item_merchant_detail, data);
        this.onMerchantItemClickListener = onMerchantItemClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, GoodsModel item) {
        helper.setIsRecyclable(false);
        final ImageView iv_minus = helper.getView(R.id.iv_minus);
        final ImageView iv_add = helper.getView(R.id.iv_add);
        final TextView tv_count = helper.getView(R.id.tv_count);
        final TextView tv_goods_name = helper.getView(R.id.tv_goods_name);
        final int position = helper.getLayoutPosition();
        TextView tv_original_price = helper.getView(R.id.tv_original_price);
        final TextView tv_price = helper.getView(R.id.tv_price);
        RatingBar ratingBar = helper.getView(R.id.ratingBar);
        if (item.starLevel>5.0f)
            item.starLevel=5.0f;
        ratingBar.setStar(item.starLevel);
        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) helper.getView(R.id.iv_image), AppConstants.URL_BASE + item.images, R.mipmap.c1_image2);
        tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_original_price.setText("¥"+item.originalPrice);
        tv_price.setText("¥"+item.price);
        tv_goods_name.setText(item.name);
        int count = item.goodsCount;
        if(count>0){
            iv_minus.setVisibility(View.VISIBLE);
            tv_count.setVisibility(View.VISIBLE);
        }else {
            iv_minus.setVisibility(View.INVISIBLE);
            tv_count.setVisibility(View.INVISIBLE);
        }
        tv_count.setText(""+count);
        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMerchantItemClickListener.onMinusClick(iv_minus, tv_count, position);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMerchantItemClickListener.onAddClick(iv_add, tv_count,iv_minus, position);
            }
        });

    }

    public interface OnMerchantItemClickListener {
        void onMinusClick(View view, TextView tv_count,int position);

        void onAddClick(View view, TextView tv_count,ImageView iv_minus, int position);
    }

    public void setCount(int count){

    }
}

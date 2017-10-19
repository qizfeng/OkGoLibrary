package com.project.community.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
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
        final int position = helper.getLayoutPosition();
        TextView tv_original_price = helper.getView(R.id.tv_original_price);
        final TextView tv_price = helper.getView(R.id.tv_price);
        tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_price.setText("¥"+item.goodsPrice);
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

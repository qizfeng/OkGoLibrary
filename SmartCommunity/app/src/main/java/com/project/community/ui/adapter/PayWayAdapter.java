package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.PaymentWayModel;

import java.util.List;

/**
 * Created by 缴费方式 on 17/8/17.
 */

public class PayWayAdapter extends BaseQuickAdapter<PaymentWayModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public PayWayAdapter(List<PaymentWayModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_payment_way, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final PaymentWayModel model) {
        baseViewHolder.setText(R.id.tv_payment_way,model.label);
        Glide.with(mContext)
                .load(model.icon)
                .into((ImageView) baseViewHolder.getView(R.id.iv_image));
//        baseViewHolder.setImageResource(R.id.iv_image,model.res);
        final int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,position);
            }
        });
    }

}

package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.DeviceUtil;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.PaymentWayModel;
import com.project.community.util.ScreenUtils;
import com.project.community.view.crop.FloatDrawable;
import com.squareup.picasso.Picasso;

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
        Picasso.with(mContext)
                .load(AppConstants.HOST+model.icon)
                .resize(FloatDrawable.dipTopx(mContext,25),FloatDrawable.dipTopx(mContext,25))
                .centerInside()
                .into((ImageView) baseViewHolder.getView(R.id.iv_image));
        final int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,position);
            }
        });
    }

}

package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.OrderModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 所有订单适配器
 */

public class AllOrderApdater extends BaseQuickAdapter<OrderModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public AllOrderApdater(List<OrderModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_all_order, data);
        itemClickListener = itemClick;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final OrderModel model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });
        baseViewHolder.setOnClickListener(R.id.item_all_order_btn_type, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        });

        baseViewHolder.setText(R.id.item_all_order_tv_time,model.createDate)
                .setText(R.id.item_all_order_tv_name,mContext.getString(R.string.my_order_address_apply_shouhuoren)+model.address.consignee)
                .setText(R.id.item_all_order_tv_phone,model.address.contactPhone)
                .setText(R.id.item_all_order_tv_address,model.address.address)
                .setText(R.id.item_all_order_tv_price,mContext.getString(R.string.money)+model.orderAmountTotal);

        switch (model.orderStatus){
            case "0"://待发货
                baseViewHolder.setText(R.id.item_all_order_tv_type,mContext.getString(R.string.my_order_wait_fahuo))
                        .setTextColor(R.id.item_all_order_tv_type,mContext.getResources().getColor(R.color.yellow_ff961b))
                        .setText(R.id.item_all_order_btn_type,mContext.getString(R.string.my_order_fahuo));
                break;
            case "1"://已发货
                baseViewHolder.setText(R.id.item_all_order_tv_type,mContext.getString(R.string.my_order_wait_yishouhuo))
                        .setTextColor(R.id.item_all_order_tv_type,mContext.getResources().getColor(R.color.yellow_ff961b))
                        .setText(R.id.item_all_order_btn_type,mContext.getString(R.string.all_order_yichuli));
                break;
            case "2"://已完成
                baseViewHolder.setText(R.id.item_all_order_tv_type,mContext.getString(R.string.my_order_end_over))
                        .setTextColor(R.id.item_all_order_tv_type,mContext.getResources().getColor(R.color.color_gray_999999));
                break;
            case "3"://售后
                baseViewHolder.setText(R.id.item_all_order_tv_type,mContext.getString(R.string.my_order_daiyichuli))
                        .setTextColor(R.id.item_all_order_tv_type,mContext.getResources().getColor(R.color.yellow_ff961b))
                        .setText(R.id.item_all_order_btn_type,mContext.getString(R.string.all_order_chuli));
                break;
            case "4":
                baseViewHolder.setText(R.id.item_all_order_tv_type,mContext.getString(R.string.my_order_yiguanbi))
                        .setTextColor(R.id.item_all_order_tv_type,mContext.getResources().getColor(R.color.color_gray_999999)) ;
                break;

        }
        if (model.orderStatus.equals("4")) baseViewHolder.setVisible(R.id.item_all_order_btn_type,false);
        else baseViewHolder.setVisible(R.id.item_all_order_btn_type,true);
    }
}

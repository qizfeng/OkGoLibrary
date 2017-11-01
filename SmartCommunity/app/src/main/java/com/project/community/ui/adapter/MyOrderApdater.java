package com.project.community.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.model.GoodsModel;
import com.project.community.model.OrderModel;

import java.util.List;

/**
 * Created by cj on 17/10/25
 * 我的订单适配器
 */

public class MyOrderApdater extends GroupedRecyclerViewAdapter {

    private List<OrderModel> mGroups;
    private OnFooterClickListener onFooterClickListener;
    private OnGoodsClickListener onGoodsClickListener;

    private String status;

    public MyOrderApdater(Context context, List<OrderModel> groups,String status, OnGoodsClickListener onGoodsClickListener, OnFooterClickListener onFooterClickListener) {
        super(context);
        mGroups = groups;
        this.status=status;
        this.onFooterClickListener = onFooterClickListener;
        this.onGoodsClickListener = onGoodsClickListener;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<GoodsModel> children = mGroups.get(groupPosition).detailList;
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        if (status.equals("3"))
           return R.layout.layout_item_after_sale_header;
        return R.layout.layout_item_my_order_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.layout_item_myorder_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.layout_item_shoppingcart_goods;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        OrderModel entity = mGroups.get(groupPosition);
        Log.e("onBindChildViewHolder: ", entity.toString());
        if (!status.equals("3"))
            holder.setText(R.id.header_title,entity.shopsName)
                    .setText(R.id.header_time,entity.createDate);
        switch (status){
            case ""://"", #全部
                switch (entity.orderStatus){
                    case "0"://"0", #未发货
                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_fahuo));
                        break;
                    case "1"://:1：已发货
                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_end));
                        break;
                    case "2"://2：已完成    (包括 已评价 与 待评价 )
                        if (entity.isComment==0)
                            holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_pingjia));
                        else
                            holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_yipingjia));
                        break;
                    case "3"://3：退货申请
                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status));
                        break;
                    case "4"://,4：退货中
                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_ing));
                        break;
                    case "5"://5：已退货
                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_end));
                        break;
                }
                break;
            case "0"://"0", #未发货
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_fahuo));
                break;
            case "1"://:1：已发货
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_end));
                break;
            case "2"://2：已完成
                if (entity.isComment==0)
                    holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_pingjia));
                else
                    holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_yipingjia));
                break;
            case "3"://3：退货申请
//                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status));
                holder.setText(R.id.item_header_title,mContext.getString(R.string.my_order_address_order_num)+entity.orderNo)
                        .setText(R.id.item_header_status,entity.createDate);
                if (entity.orderStatus.equals("0")){
                    holder.setText(R.id.item_header_status,mContext.getString(R.string.my_order_address_daichuli))
                            .setTextColor(R.id.item_header_status,mContext.getResources().getColor(R.color.yellow_ff961b));
                }
                else  {
                    holder.setText(R.id.item_header_status,mContext.getString(R.string.my_order_address_yichuli))
                            .setTextColor(R.id.item_header_status,mContext.getResources().getColor(R.color.yellow));
                }
                break;
            case "4"://,4：退货中
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_ing));
                break;
            case "5"://5：已退货
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_end));
                break;
        }
    }

    @Override
    public void onBindFooterViewHolder(final BaseViewHolder holder, final int groupPosition) {
        OrderModel entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_total_price,"¥"+entity.orderAmountTotal)
                .setText(R.id.tv_goods_count,"共"+entity.goodsCount+"件商品，合计");

        switch (status){
            case ""://"", #全部
                switch (entity.orderStatus){
                    case "0"://"0", #未发货
                        holder.setVisible(R.id.item_foot_1,false)
                                .setVisible(R.id.item_foot_2,true)
                                .setVisible(R.id.item_foot_3,true)
                                .setText(R.id.item_foot_2,mContext.getString(R.string.my_order_address_lianxishangjia))
                                .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_cacel_order));
                        break;
                    case "1"://:1：已发货
                        holder.setVisible(R.id.item_foot_1,false)
                                .setVisible(R.id.item_foot_2,true)
                                .setVisible(R.id.item_foot_3,true)
                                .setText(R.id.item_foot_2,mContext.getString(R.string.my_order_address_lianxishangjia))
                                .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_querenshouhuo));
                        break;
                    case "2"://2：已完成 (包括 已评价 与 待评价 )
                        if (entity.isComment==0) //待评价
                            holder.setVisible(R.id.item_foot_1,true)
                                .setVisible(R.id.item_foot_2,true)
                                .setVisible(R.id.item_foot_3,true)
                                .setText(R.id.item_foot_1,mContext.getString(R.string.my_order_address_pingjia))
                                .setText(R.id.item_foot_2,mContext.getString(R.string.my_order_address_del_order))
                                .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_apply_safe));
                        else
                            holder.setVisible(R.id.item_foot_1,false)
                                    .setVisible(R.id.item_foot_2,false)
                                    .setVisible(R.id.item_foot_3,true)
                                    .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_del_order));
                        break;
//                    case "3"://3：退货申请
//                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status));
//                        break;
//                    case "4"://,4：退货中
//                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_ing));
//                        break;
//                    case "5"://5：已退货
//                        holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_end));
//                        break;
                    default:
                        holder.setVisible(R.id.item_foot_1,false)
                                .setVisible(R.id.item_foot_2,false)
                                .setVisible(R.id.item_foot_3,true)
                                .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_del_order));
                        break;
                }
                break;
            case "0"://"0", #未发货
                holder.setVisible(R.id.item_foot_1,false)
                        .setVisible(R.id.item_foot_2,true)
                        .setVisible(R.id.item_foot_3,true)
                        .setText(R.id.item_foot_2,mContext.getString(R.string.my_order_address_lianxishangjia))
                        .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_cacel_order));
                break;
            case "1"://:1：已发货
                holder.setVisible(R.id.item_foot_1,false)
                        .setVisible(R.id.item_foot_2,true)
                        .setVisible(R.id.item_foot_3,true)
                        .setText(R.id.item_foot_2,mContext.getString(R.string.my_order_address_lianxishangjia))
                        .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_querenshouhuo));
                break;
            case "2"://2：已完成(包括 已评价 与 待评价 )
                if (entity.isComment==0) //待评价
                    holder.setVisible(R.id.item_foot_1,true)
                            .setVisible(R.id.item_foot_2,true)
                            .setVisible(R.id.item_foot_3,true)
                            .setText(R.id.item_foot_1,mContext.getString(R.string.my_order_address_pingjia))
                            .setText(R.id.item_foot_2,mContext.getString(R.string.my_order_address_del_order))
                            .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_apply_safe));
                else
                    holder.setVisible(R.id.item_foot_1,false)
                            .setVisible(R.id.item_foot_2,false)
                            .setVisible(R.id.item_foot_3,true)
                            .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_del_order));
                break;
            default:
                holder.setVisible(R.id.item_foot_1,false)
                        .setVisible(R.id.item_foot_2,false)
                        .setVisible(R.id.item_foot_3,true)
                        .setText(R.id.item_foot_3,mContext.getString(R.string.my_order_address_del_order));
                break;
//            case "3"://3：退货申请
//                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status));
//                break;
//            case "4"://,4：退货中
//                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_ing));
//                break;
//            case "5"://5：已退货
//                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status_end));
//                break;
        }

        holder.get(R.id.item_foot_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onFooterDeleteClick(holder.get(R.id.item_foot_1), groupPosition);
            }
        });

        holder.get(R.id.item_foot_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onFooterDeleteClick(holder.get(R.id.item_foot_2), groupPosition);
            }
        });

        holder.get(R.id.item_foot_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onFooterDeleteClick(holder.get(R.id.item_foot_3), groupPosition);
            }
        });
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        GoodsModel entity = mGroups.get(groupPosition).detailList.get(childPosition);
        holder.setText(R.id.item_goods_name,entity.goodName)
                .setText(R.id.tv_unit_price,"¥"+entity.goodPrice)
                .setText(R.id.tv_goods_count,"x"+entity.number);
        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) holder.get(R.id.item_goods_cover), entity.goodImage,R.mipmap.c1_image2);

    }

    public interface OnFooterClickListener {
        void onFooterDeleteClick(View view, int position);

        void onSettlementClick(View view, int position);
    }

    public interface OnGoodsClickListener {
        void onGoodsItemClick(View view, int parentPosition,int childPosition);
    }
}

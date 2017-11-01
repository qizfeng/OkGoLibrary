package com.project.community.ui.adapter;

import android.content.Context;
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

    public MyOrderApdater(Context context, List<OrderModel> groups, OnGoodsClickListener onGoodsClickListener, OnFooterClickListener onFooterClickListener) {
        super(context);
        mGroups = groups;
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
        holder.setText(R.id.header_title,entity.shopsName)
                .setText(R.id.header_time,"共"+entity.createDate);
        switch (entity.orderStatus){
            case "0"://"0", #未发货
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_wait_fahuo));
                break;
            case "1"://1：已发货
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_end));
                break;
            case "2"://2：已完成
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_end_over));
                break;
            case "3"://3：退货申请
                holder.setText(R.id.header_status,mContext.getString(R.string.my_order_address_apply_safe_status));
                break;
            case "4"://4：退货中
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
        holder.setText(R.id.item_goods_name,entity.goodsName)
                .setText(R.id.tv_unit_price,"¥"+entity.goodPrice)
                .setText(R.id.tv_goods_count,"x"+entity.number);
        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) holder.get(R.id.item_goods_cover), AppConstants.URL_BASE+entity.goodImage,R.mipmap.c1_image2);

    }

    public interface OnFooterClickListener {
        void onFooterDeleteClick(View view, int position);

        void onSettlementClick(View view, int position);
    }

    public interface OnGoodsClickListener {
        void onGoodsItemClick(View view, int parentPosition,int childPosition);
    }
}

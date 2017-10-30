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
import com.project.community.model.ShopModel;
import com.project.community.model.ShoppingCartModel;

import java.util.List;


/**
 * Created by zipingfang on 17/10/17.
 */

public class ShoppingCartAdapter extends GroupedRecyclerViewAdapter {

    private List<ShoppingCartModel> mGroups;
    private OnFooterClickListener onFooterClickListener;
    private OnGoodsClickListener onGoodsClickListener;

    public ShoppingCartAdapter(Context context, List<ShoppingCartModel> groups, OnGoodsClickListener onGoodsClickListener, OnFooterClickListener onFooterClickListener) {
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
        List<GoodsModel> children = mGroups.get(groupPosition).shop.goods;
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
        return R.layout.layout_item_shoppingcart_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.layout_item_shoppingcart_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.layout_item_shoppingcart_goods;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        ShopModel entity = mGroups.get(groupPosition).shop;
        holder.setText(R.id.item_header_title,entity.shopsName);

    }

    @Override
    public void onBindFooterViewHolder(final BaseViewHolder holder, final int groupPosition) {
        holder.get(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onFooterDeleteClick(view, groupPosition);
            }
        });

        holder.get(R.id.tv_settlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onSettlementClick(view, groupPosition);
            }
        });
        ShopModel entity = mGroups.get(groupPosition).shop;
        holder.setText(R.id.tv_total_price,"¥"+entity.totalCost);
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        GoodsModel entity = mGroups.get(groupPosition).shop.goods.get(childPosition);

        holder.setText(R.id.item_goods_name,entity.name)
                .setText(R.id.tv_unit_price,"¥"+entity.price)
                .setText(R.id.tv_goods_count,"x"+entity.number);
        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) holder.get(R.id.item_goods_cover), AppConstants.URL_BASE+entity.images, R.mipmap.c1_image2);

    }

    public interface OnFooterClickListener {
        void onFooterDeleteClick(View view, int position);

        void onSettlementClick(View view, int position);
    }

    public interface OnGoodsClickListener {
        void onGoodsItemClick(View view, int parentPosition,int childPosition);
    }
}
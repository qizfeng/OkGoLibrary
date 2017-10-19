package com.project.community.ui.adapter;

import android.content.Context;
import android.view.View;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.project.community.R;
import com.project.community.model.GoodsModel;
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
        List<GoodsModel> children = mGroups.get(groupPosition).goods;
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
        ShoppingCartModel entity = mGroups.get(groupPosition);
    }

    @Override
    public void onBindFooterViewHolder(final BaseViewHolder holder, final int groupPosition) {
        ShoppingCartModel entity = mGroups.get(groupPosition);
        holder.get(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onFooterDeleteClick(holder.get(R.id.tv_comment_num), groupPosition);
            }
        });

        holder.get(R.id.tv_settlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFooterClickListener.onSettlementClick(holder.get(R.id.tv_settlement), groupPosition);
            }
        });
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        GoodsModel entity = mGroups.get(groupPosition).goods.get(childPosition);

    }

    public interface OnFooterClickListener {
        void onFooterDeleteClick(View view, int position);

        void onSettlementClick(View view, int position);
    }

    public interface OnGoodsClickListener {
        void onGoodsItemClick(View view, int parentPosition,int childPosition);
    }
}
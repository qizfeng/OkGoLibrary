package com.project.community.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.model.GoodsModel;
import com.project.community.model.OrderModel;

import java.util.List;

/**
 * Created by cj on 17/10/26
 * 售后列表适配器
 */

public class AfterSaleApdater extends GroupedRecyclerViewAdapter {

    private List<OrderModel> mGroups;
    private OnFooterClickListener onFooterClickListener;
    private OnGoodsClickListener onGoodsClickListener;

    public AfterSaleApdater(Context context, List<OrderModel> groups) {
        super(context);
        mGroups = groups;

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
        return R.layout.layout_item_after_sale_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.layout_item_after_sale_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.layout_item_shoppingcart_goods;
    }

    @Override
    public void onBindHeaderViewHolder(com.donkingliang.groupedadapter.holder.BaseViewHolder holder, int groupPosition) {
        OrderModel entity = mGroups.get(groupPosition);
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
    }

    @Override
    public void onBindFooterViewHolder(final com.donkingliang.groupedadapter.holder.BaseViewHolder holder, final int groupPosition) {
        OrderModel entity = mGroups.get(groupPosition);
    }

    @Override
    public void onBindChildViewHolder(com.donkingliang.groupedadapter.holder.BaseViewHolder holder, int groupPosition, int childPosition) {
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
        void onGoodsItemClick(View view, int parentPosition, int childPosition);
    }











//    protected List<T> mData;


    //load more
    private boolean mNextLoadEnable = false;
    private boolean mLoadMoreEnable = false;
    private boolean mLoading = false;
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    private BaseQuickAdapter.RequestLoadMoreListener mRequestLoadMoreListener;

    private RecyclerView mRecyclerView;

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }


    public void setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener, RecyclerView recyclerView) {
        openLoadMore(requestLoadMoreListener);
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
    }
    private void openLoadMore(BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
        mNextLoadEnable = true;
        mLoadMoreEnable = true;
        mLoading = false;
    }

    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }


    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getHeaderLayoutCount() + mGroups.size() + getFooterLayoutCount());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getHeaderLayoutCount() + mGroups.size() + getFooterLayoutCount());
        }
    }


    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    public int getLoadMoreViewCount() {
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }
        if (mGroups.size() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getHeaderLayoutCount() {
        if (getHeaderLayoutCount() == 0 ) {
            return 0;
        }
        return 1;
    }
    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        if (getFooterLayoutCount() == 0) {
            return 0;
        }
        return 1;
    }
    private int mPreLoadNumber = 1;
    private void autoLoadMore(int position) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - mPreLoadNumber) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        mRequestLoadMoreListener.onLoadMoreRequested();
                    }
                });
            } else {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
        }
    }
}

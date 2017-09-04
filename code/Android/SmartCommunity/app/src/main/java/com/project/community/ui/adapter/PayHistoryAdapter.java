package com.project.community.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.NewsModel;
import com.project.community.ui.adapter.listener.MinshengAdapterItemListener;

import java.util.List;

/**
 * Created by zipingfang on 17/8/24.
 */

public class PayHistoryAdapter extends BaseQuickAdapter<NewsModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;

    public PayHistoryAdapter(List<NewsModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_pay_history, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final NewsModel model) {
    }

}

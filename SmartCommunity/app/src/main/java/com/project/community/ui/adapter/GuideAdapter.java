package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.GuideModel;
import com.project.community.model.WenjuanModel;

import java.util.List;

/**
 * Created by qizfeng on 17/8/11.
 * 办事指南适配器
 */

public class GuideAdapter extends BaseQuickAdapter<GuideModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public GuideAdapter(List<GuideModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_guide, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final GuideModel model) {
        baseViewHolder.setText(R.id.tv_content, model.title);
        final int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

}


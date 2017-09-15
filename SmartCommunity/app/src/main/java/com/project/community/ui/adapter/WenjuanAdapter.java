package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.WenjuanModel;

import java.util.List;

/**
 * Created by qizfng on 17/7/31.
 * 问卷列表适配器
 */

public class WenjuanAdapter extends BaseQuickAdapter<WenjuanModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public WenjuanAdapter(List<WenjuanModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_wenjuan, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final WenjuanModel model) {
        baseViewHolder.setText(R.id.tv_title,model.surveyName);

        final int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,position);
            }
        });
    }

}

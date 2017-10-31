package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.bean.RepairListBean;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 已完成列表适配器
 */

public class ServiesComApdater extends BaseQuickAdapter<RepairListBean, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public ServiesComApdater(List<RepairListBean> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_serives_comple, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final RepairListBean model) {
//        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
//            }
//        });
        baseViewHolder.setOnClickListener(R.id.item_del, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        }).setOnClickListener(R.id.rl_content, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });

    }
}

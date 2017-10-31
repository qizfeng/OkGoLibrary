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
 * 待服务列表适配器
 */

public class ServiesWaitApdater extends BaseQuickAdapter<RepairListBean, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public ServiesWaitApdater(List<RepairListBean> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_serives_wait, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final RepairListBean model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });

        baseViewHolder.setText(R.id.item_title,model.getRoomAddress());
        baseViewHolder.setText(R.id.item_data,model.getCreateDate());
        baseViewHolder.setText(R.id.item_type,"【"+model.getOrderType()+"】");

        baseViewHolder.getView(R.id.item_dispose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
}

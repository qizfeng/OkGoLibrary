package com.project.community.ui.adapter;

import android.content.Intent;
import android.net.Uri;
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
 * 服务中列表适配器
 */

public class ServiesIngApdater extends BaseQuickAdapter<RepairListBean.ListBean, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public ServiesIngApdater(List<RepairListBean.ListBean> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_serives_ing, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final RepairListBean.ListBean model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });
        baseViewHolder.setOnClickListener(R.id.itme_tel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                itemClickListener.onCustomClick(view, model.getPhone());

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        baseViewHolder.setText(R.id.item_title,model.getRoomAddress());
        baseViewHolder.setText(R.id.item_data,model.getCreateDate());
        baseViewHolder.setText(R.id.item_type,"【"+model.getOrderType()+"】");

    }
}

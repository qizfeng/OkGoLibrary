package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.FamilyPersonModel;
import com.project.community.model.PaymentHistroyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/8/24.
 */

public class FamilyAdapter extends BaseMultiItemQuickAdapter<FamilyPersonModel, BaseViewHolder> {
    private OnAdapterItemClickListener itemClickListener;
    private List<FamilyPersonModel> mData = new ArrayList<>();

    public FamilyAdapter(List<FamilyPersonModel> data, OnAdapterItemClickListener itemClick) {
        //super(R.layout.layout_item_family_flag);
        super(data);
        addItemType(FamilyPersonModel.HEADER, R.layout.layout_item_family_flag);
        addItemType(FamilyPersonModel.CONTENT, R.layout.layout_item_family_person);
        itemClickListener = itemClick;
        this.mData = data;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final FamilyPersonModel item) {
        if (item.itemType == 2) {
            helper.setImageResource(R.id.iv_avatar, item.res);
            if (item.hasTag) {
                helper.setImageResource(R.id.iv_tag, item.tagRes);
                helper.setVisible(R.id.iv_tag, true);
            } else {
                helper.setVisible(R.id.iv_tag, false);
            }
            helper.setText(R.id.tv_name, item.name);
            helper.setText(R.id.tv_relative, item.relative);
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(item, helper.getLayoutPosition());
                }
            });

            helper.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onDeleteClick(item, helper.getLayoutPosition());
                }
            });

            helper.setOnClickListener(R.id.tv_edit, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onEdit(item, helper.getLayoutPosition());
                }
            });

        }
    }

    public interface OnAdapterItemClickListener {
        void onDeleteClick(FamilyPersonModel item, int position);//刪除

        void onEdit(FamilyPersonModel item, int position);//编辑

        void onItemClick(FamilyPersonModel item, int position);//item
    }
}

package com.project.community.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.model.FamilyPersonModel;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
            if (TextUtils.isEmpty(item.headRelation))
                helper.setVisible(R.id.iv_tag, false);
            else {
                if ("户主".equals(item.headRelation)) {
//                    helper.setImageResource(R.id.iv_tag, item.tagRes);
                    helper.setVisible(R.id.iv_tag, true);
                } else {
                    helper.setVisible(R.id.iv_tag, false);
                }
            }
            helper.setText(R.id.tv_name, item.realName);
            helper.setText(R.id.tv_relative, item.headRelation);
            Glide.with(mContext)
                    .load(AppConstants.HOST + item.photo)
                    .placeholder(R.mipmap.d54_tx)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into((ImageView) helper.getView(R.id.iv_avatar));
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

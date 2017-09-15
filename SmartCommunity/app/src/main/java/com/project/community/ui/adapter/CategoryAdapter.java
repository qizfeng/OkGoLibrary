package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.model.CategoryModel;
import com.project.community.model.CategorySection;
import com.project.community.util.ScreenUtils;

import java.util.List;

/**
 * Created by qizfeng on 17/7/27.
 * 分类适配器
 */

public class CategoryAdapter extends BaseSectionQuickAdapter<CategorySection, BaseViewHolder> {
    OnClickListener onClickListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public CategoryAdapter(int layoutResId, int sectionHeadResId, List data, OnClickListener onClickListener) {
        super(layoutResId, sectionHeadResId, data);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final CategorySection item) {
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.view, false);
        } else {
            helper.setVisible(R.id.view, true);
        }
        helper.setText(R.id.tv_header, item.header);
        helper.setVisible(R.id.tv_more, item.isMore());
        helper.addOnClickListener(R.id.tv_more);
    }

    /**
     * 显示可编辑按钮
     *
     * @param data
     */
    public void showEditBtn(List<CategorySection> data) {
        try {
            for (int i = 2; i < data.size(); i++) {
                if (!data.get(i).isHeader)
                    data.get(i).t.isShowEdit = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();

    }


    /**
     * 隐藏可编辑按钮
     *
     * @param data
     */
    public void hideEditBtn(List<CategorySection> data) {
        for (int i = 2; i < data.size(); i++) {
            if (!data.get(i).isHeader)
                data.get(i).t.isShowEdit = false;
        }
        notifyDataSetChanged();

    }


    @Override
    protected void convert(final BaseViewHolder helper, CategorySection item) {
        final CategoryModel categoryModel = item.t;
        helper.getConvertView().getLayoutParams().width = (ScreenUtils.getScreenWidth(mContext)-60)/4;
        helper.setImageResource(R.id.iv_icon, item.t.res);
        helper.setText(R.id.tv_category, categoryModel.title);
        if (categoryModel.isShowEdit) {//是否显示编辑按钮
            helper.setVisible(R.id.layout_edit, true);
            if (categoryModel.isAdd) {//是添加
                helper.setImageResource(R.id.iv_edit, R.mipmap.c3_jian_p);
            } else {//是删除
                helper.setImageResource(R.id.iv_edit, R.mipmap.c3_jian);
            }
            helper.setBackgroundRes(R.id.item_layout,R.color.common_gray_bg);
        } else {
            helper.setVisible(R.id.layout_edit, false);
            helper.setBackgroundRes(R.id.item_layout,R.color.white);
        }
        if (categoryModel.state == -1) {//默认不可编辑的
            helper.setVisible(R.id.layout_edit, false);
            helper.getView(R.id.layout_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onCancelClick(mData, helper.getLayoutPosition());
                }
            });
            helper.setTextColor(R.id.tv_category, mContext.getResources().getColor(R.color.colorPrimary));
        } else if (categoryModel.state == 0) {//我的分类
            helper.setTextColor(R.id.tv_category, mContext.getResources().getColor(R.color.colorPrimary));
            helper.getView(R.id.layout_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onCancelClick(mData, helper.getLayoutPosition());
                }
            });

        } else if (categoryModel.state == 1) {//未添加的
            helper.setTextColor(R.id.tv_category, mContext.getResources().getColor(R.color.txt_color));
            helper.getView(R.id.layout_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onAddClick(mData, helper.getLayoutPosition());
                }
            });
        } else if (categoryModel.state == 2) {//已添加的
            helper.setImageResource(R.id.iv_edit, R.mipmap.c3_jian_p1);
            helper.setTextColor(R.id.tv_category, mContext.getResources().getColor(R.color.txt_color));
            helper.getView(R.id.layout_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onNothingClick(helper.getLayoutPosition());
                }
            });
        }
    }



    public interface OnClickListener {
        void onCancelClick(List<CategorySection> list, int position);//取消

        void onAddClick(List<CategorySection> list, int position);

        void onNothingClick(int position);
    }

}

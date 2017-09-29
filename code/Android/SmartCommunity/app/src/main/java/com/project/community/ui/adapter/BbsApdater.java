package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 论坛适配器
 */

public class BbsApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public BbsApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_bbs, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentModel model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });

        baseViewHolder.setOnClickListener(R.id.bbs_item_like_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        }).setOnClickListener(R.id.bbs_item_like, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        });


        if (baseViewHolder.getLayoutPosition()==0){
            baseViewHolder
                    .setVisible(R.id.bbs_item_big_img,true)
                    .setVisible(R.id.bbs_item_ll_two_img,false)
                    .setVisible(R.id.bbs_item_ll_three_img,false);
        } else if (baseViewHolder.getLayoutPosition()== getItemCount()-1){
            baseViewHolder
                    .setVisible(R.id.bbs_item_big_img,false)
                    .setVisible(R.id.bbs_item_ll_two_img,false)
                    .setVisible(R.id.bbs_item_ll_three_img,true);
        }else {
            baseViewHolder
                    .setVisible(R.id.bbs_item_big_img,false)
                    .setVisible(R.id.bbs_item_ll_two_img,true)
                    .setVisible(R.id.bbs_item_ll_three_img,false);
        }

    }
}

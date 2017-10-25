package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hedgehog.ratingbar.RatingBar;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.List;

/**
 * Created by cj on 17/9/26
 * 商品订单评论适配器
 */

public class GoodsOrderCommentApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public GoodsOrderCommentApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_goods_comment, data);
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
        baseViewHolder.setOnClickListener(R.id.item_comment_del, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        });
        RatingBar ratingBar = baseViewHolder.getView(R.id.ratingBar);
        ratingBar.setStar(model.rating);
    }
}

package com.project.community.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 订单详情受理评论适配器
 */

public class OrderDetailShouliCommentApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public OrderDetailShouliCommentApdater(List<CommentModel> data) {
        super(R.layout.layout_item_order_detail_shouli_comment, data);
//        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentModel model) {
//        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
//            }
//        });

            if (baseViewHolder.getLayoutPosition()==0){
                baseViewHolder
                        .setVisible(R.id.item_order_detail_shouli_comment_pro,true)
                        .setVisible(R.id.view_line,true)
                        .setVisible(R.id.item_order_detail_shouli_comment_time,false);
            } else if (baseViewHolder.getLayoutPosition()== getItemCount()-1){
                baseViewHolder .setVisible(R.id.item_order_detail_shouli_comment_pro,false)
                        .setVisible(R.id.view_line,false)
                        .setVisible(R.id.item_order_detail_shouli_comment_time,true);
            }else {
                    baseViewHolder .setVisible(R.id.item_order_detail_shouli_comment_pro,false)
                                        .setVisible(R.id.view_line,true)
                                        .setVisible(R.id.item_order_detail_shouli_comment_time,true);
                }
    }
}

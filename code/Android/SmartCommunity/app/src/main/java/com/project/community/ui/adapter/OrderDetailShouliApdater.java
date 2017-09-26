package com.project.community.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 17/9/25
 * 订单详情受理适配器
 */

public class OrderDetailShouliApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public OrderDetailShouliApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_order_detail_shouli, data);
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

        RecyclerView recyclerView=baseViewHolder.getView(R.id.item_iscurrt_reylerview);

        View view1 = baseViewHolder.getView(R.id.item_view_1);
        View view2 = baseViewHolder.getView(R.id.item_view_2);
            if (baseViewHolder.getLayoutPosition()==0){
                view1.setVisibility(View.INVISIBLE);
                baseViewHolder.setImageResource(R.id.item_cirimags,R.mipmap.d41_dian_p)
                        .setVisible(R.id.item_iscurrt,false)
                        .setVisible(R.id.item_iscurrt_evaluate,false)
                        .setVisible(R.id.item_iscurrt_reylerview,false);
            } else if (baseViewHolder.getLayoutPosition() == 6) {
                baseViewHolder.setVisible(R.id.item_iscurrt,true)
                        .setVisible(R.id.item_iscurrt,true)
                        .setVisible(R.id.item_iscurrt_evaluate,false)
                        .setVisible(R.id.item_iscurrt_reylerview,true);
                List<CommentModel> data=new ArrayList<>();
                for (int i = 0; i <4 ; i++) {
                    CommentModel commentModel = new CommentModel();
                    data.add(commentModel);
                }
                OrderDetailShouliCommentApdater orderDetailShouliCommentApdater =new OrderDetailShouliCommentApdater(data);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(orderDetailShouliCommentApdater);

            } else {
                if (baseViewHolder.getLayoutPosition() == getItemCount() - 1) {
                    view2.setVisibility(View.INVISIBLE);
                    baseViewHolder.setImageResource(R.id.item_cirimags, R.mipmap.d41_dian_p)
                            .setVisible(R.id.item_iscurrt, true)
                            .setVisible(R.id.item_iscurrt_evaluate, true)
                            .setVisible(R.id.item_iscurrt_reylerview, false);
                } else {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    baseViewHolder.setImageResource(R.id.item_cirimags, R.mipmap.d41_dian)
                            .setVisible(R.id.item_iscurrt, false)
                            .setVisible(R.id.item_iscurrt_evaluate, false)
                            .setVisible(R.id.item_iscurrt_reylerview, false);
                }
            }

    }
}

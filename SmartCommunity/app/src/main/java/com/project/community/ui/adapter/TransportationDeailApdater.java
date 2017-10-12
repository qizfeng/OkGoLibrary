package com.project.community.ui.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 公交列表适配器
 */

public class TransportationDeailApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public TransportationDeailApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_transportation_detil, data);
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
            if (model.id.equals("1")){
                baseViewHolder.setVisible(R.id.item_iscurrt,true)
                        .setTextColor(R.id.item_name,mContext.getResources().getColor(R.color.login_bottom_txt));
            }else {
                baseViewHolder.setVisible(R.id.item_iscurrt,false)
                        .setTextColor(R.id.item_name,mContext.getResources().getColor(R.color.black));
            }

        View view1 = baseViewHolder.getView(R.id.item_view_1);
        View view2 = baseViewHolder.getView(R.id.item_view_2);
            if (baseViewHolder.getLayoutPosition()-1==0){
                view1.setVisibility(View.INVISIBLE);
                baseViewHolder.setImageResource(R.id.item_cirimags,R.mipmap.d41_dian_p)
                        .setVisible(R.id.view_line_bottom,true);
            }else if (baseViewHolder.getLayoutPosition()==getItemCount()-1){
                view2.setVisibility(View.INVISIBLE);
                baseViewHolder.setImageResource(R.id.item_cirimags,R.mipmap.d41_dian_p)
                        .setVisible(R.id.view_line_bottom,false);
            }else {
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                baseViewHolder.setImageResource(R.id.item_cirimags,R.mipmap.d41_dian)
                        .setVisible(R.id.view_line_bottom,true);
            }
    }
}

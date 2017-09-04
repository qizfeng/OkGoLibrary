package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.model.PaymentHistroyModel;

import java.util.List;

/**
 * Created by qizfeng on 17/8/21.
 * 缴费历史适配器
 */

public class PaymentHistoryAdapter extends BaseSectionQuickAdapter<PaymentHistroyModel, BaseViewHolder> {
    OnAdapterItemClickListener onClickListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public PaymentHistoryAdapter(int layoutResId, int sectionHeadResId, List data, OnAdapterItemClickListener onClickListener) {
        super(layoutResId, sectionHeadResId, data);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final PaymentHistroyModel item) {
        if(item.isHeader){
            if ("水费".equals(item.header)){
                helper.setImageResource(R.id.iv_pay_icon,R.mipmap.d21_icon1);
            }else if("燃气费".equals(item.header)){
                helper.setImageResource(R.id.iv_pay_icon,R.mipmap.d21_icon2);
            }
            helper.setText(R.id.tv_pay_name,item.header);
        }
    }


    @Override
    protected void convert(final BaseViewHolder helper, final PaymentHistroyModel item) {
        if(!item.isHeader){
            helper.setText(R.id.tv_pay_no,item.payNo);
            helper.setOnClickListener(R.id.layout_item, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onItemClick(item,helper.getLayoutPosition());
                }
            });

            helper.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onDeleteClick(mData,helper.getLayoutPosition());
                }
            });
        }
    }


    public interface OnAdapterItemClickListener {
        void onDeleteClick(List<PaymentHistroyModel> list, int position);
        void onItemClick(PaymentHistroyModel item,int position);
    }

}

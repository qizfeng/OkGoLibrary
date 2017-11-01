package com.project.community.ui.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.Event.RepairsTypeEvent;
import com.project.community.R;
import com.project.community.bean.RepairsRecordBean;
import com.project.community.ui.me.RepairsEvaluateActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * author：fangkai on 2017/10/24 17:23
 * em：617716355@qq.com
 */
public class RepairsRecordAdapter extends BaseQuickAdapter<RepairsRecordBean, BaseViewHolder> {
    public RepairsRecordAdapter(@LayoutRes int layoutResId, @Nullable List<RepairsRecordBean> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RepairsRecordBean item) {

        helper.setOnClickListener(R.id.tv_evaluate, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = helper.getView(R.id.tv_evaluate);
                if (textView.getText().toString().trim().equals("去评价")) {
//                    mContext.startActivity(new Intent(mContext, RepairsEvaluateActivity.class));
                    RepairsEvaluateActivity.startActivity(mContext, item.getOrderNo());

                } else if (textView.getText().toString().trim().equals("取消报修")) {
                    EventBus.getDefault().post(new RepairsTypeEvent(1, item.getOrderNo()));
                }else  if (textView.getText().toString().trim().equals("完成")){
                    EventBus.getDefault().post(new RepairsTypeEvent(2, item.getOrderNo()));
                }
            }
        });
        if (item.getCreateDate() != null && item.getCreateDate().length() >= 10)
            helper.setText(R.id.tv_time, item.getCreateDate().substring(0, 5));
        if (item.getCreateDate() != null && item.getCreateDate().length() >= 10)
            helper.setText(R.id.tv_times, item.getCreateDate().substring(5, 10));
        helper.setText(R.id.tv_orderType, "【" + item.getOrderType() + "】");
        helper.setText(R.id.tv_orderNo, "订单号：" + item.getOrderNo());
        helper.setText(R.id.tv_roomNo, "房屋编号：" + item.getRoomNo());
        helper.setText(R.id.tv_roomAddress, item.getRoomAddress());


        if (item.getRepairStatus() != null) {

            if (item.getRepairStatus().equals("0")) {
                //订单状态正常
                if (item.getOrderStatus() != null)
                    if (item.getOrderStatus().equals("0")) {
                        //提交成功
                        helper.setVisible(R.id.tv_orderStatus, true);
                        helper.setText(R.id.tv_orderStatus, "待派单");
                        helper.setVisible(R.id.rl_evaluate, true);
                        helper.setVisible(R.id.tv_evaluate, true);
                        helper.setText(R.id.tv_evaluate, "取消报修");
                        helper.setVisible(R.id.tv_cancel, false);
                    } else if (item.getOrderStatus().equals("1")) {
                        //后台指派订单给维修人员
                        helper.setVisible(R.id.tv_orderStatus, true);
                        helper.setText(R.id.tv_orderStatus, "待服务");
                        helper.setVisible(R.id.rl_evaluate, false);
                        helper.setVisible(R.id.tv_evaluate, false);
                        helper.setText(R.id.tv_evaluate, "取消报修");
                        helper.setVisible(R.id.tv_cancel, false);
                    } else if (item.getOrderStatus().equals("2")) {
                        //维修人员点击处理订单订单变为服务中
                        helper.setVisible(R.id.tv_orderStatus, true);
                        helper.setText(R.id.tv_orderStatus, "服务中");
                        helper.setVisible(R.id.rl_evaluate, false);
                        helper.setVisible(R.id.tv_evaluate, false);
                        helper.setText(R.id.tv_evaluate, "去评价");
                        helper.setVisible(R.id.tv_cancel, false);
                    } else if (item.getOrderStatus().equals("3")) {
                        //维修人员提交处理过程提交
                        helper.setVisible(R.id.tv_orderStatus, true);
                        helper.setText(R.id.tv_orderStatus, "已处理");
                        helper.setVisible(R.id.rl_evaluate, true);
                        helper.setVisible(R.id.tv_evaluate, true);
                        helper.setText(R.id.tv_evaluate, "完成");
                        helper.setVisible(R.id.tv_cancel, false);
                    } else if (item.getOrderStatus().equals("4")) {
                        //已完成
                        helper.setVisible(R.id.tv_orderStatus, true);
                        helper.setText(R.id.tv_orderStatus, "已完成");
                        helper.setVisible(R.id.rl_evaluate, true);
                        helper.setVisible(R.id.tv_evaluate, true);
                        helper.setText(R.id.tv_evaluate, "去评价");
                        helper.setVisible(R.id.tv_cancel, false);
                    } else if (item.getOrderStatus().equals("5")) {
                        //已评价
                        helper.setVisible(R.id.tv_orderStatus, true);
                        helper.setText(R.id.tv_orderStatus, "已评价");
                        helper.setVisible(R.id.rl_evaluate, false);
                        helper.setVisible(R.id.tv_evaluate, false);
                        helper.setText(R.id.tv_evaluate, "取消报修");
                        helper.setVisible(R.id.tv_cancel, false);
                    }

            } else {
                //订单状态已取消
                helper.setVisible(R.id.tv_orderStatus, true);
                helper.setText(R.id.tv_orderStatus, "已取消");
                helper.setVisible(R.id.rl_evaluate, true);
                helper.setVisible(R.id.tv_evaluate, false);
                helper.setText(R.id.tv_evaluate, "取消报修");
                helper.setVisible(R.id.tv_cancel, true);
            }


        }


    }
}
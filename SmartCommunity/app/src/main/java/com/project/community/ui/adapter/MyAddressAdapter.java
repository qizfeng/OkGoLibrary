package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.Event.DefaultAddressEvent;
import com.project.community.Event.DelAddressEvent;
import com.project.community.R;
import com.project.community.bean.AddressListBean;
import com.project.community.ui.me.AddAddressActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * author：fangkai on 2017/10/23 15:06
 * em：617716355@qq.com
 */
public class MyAddressAdapter extends BaseQuickAdapter<AddressListBean, BaseViewHolder> {
    public MyAddressAdapter(@LayoutRes int layoutResId, @Nullable List<AddressListBean> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AddressListBean item) {

        helper.setText(R.id.tv_name, item.getConsignee());
        helper.setText(R.id.tv_phone, item.getContactPhone());
        helper.setText(R.id.tv_address, item.getUserArea() + item.getAddress());

        if (item.getIsDefault().equals("0")) {
            helper.setImageResource(R.id.iv_set_address, R.mipmap.d10_btn1_p);
        } else {
            helper.setImageResource(R.id.iv_set_address, R.mipmap.d10_btn1);

        }

        //修改
        helper.setOnClickListener(R.id.iv_change, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAddressActivity.startActivity(mContext, item);
            }
        });

        //删除
        helper.setOnClickListener(R.id.iv_del, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DelAddressEvent(item));
            }
        });

        //设为默认地址
        helper.setOnClickListener(R.id.iv_set_address, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            EventBus.getDefault().post(new DefaultAddressEvent(item));
            }
        });

    }

}

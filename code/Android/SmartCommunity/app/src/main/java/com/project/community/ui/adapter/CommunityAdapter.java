package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.bean.CommunityBean;

import java.util.List;

/**
 * author：fangkai on 2017/10/23 17:53
 * em：617716355@qq.com
 */
public class CommunityAdapter extends BaseQuickAdapter<CommunityBean, BaseViewHolder> {
    public CommunityAdapter(@LayoutRes int layoutResId, @Nullable List<CommunityBean> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityBean item) {


        helper.setText(R.id.tv_title, "【"+item.getCategory()+"】"+item.getTitle());
        helper.setText(R.id.tv_content, item.getDescription());
        helper.setText(R.id.tv_name, item.getUserName());
        helper.setText(R.id.tv_time, item.getCreateDate());

    }

}


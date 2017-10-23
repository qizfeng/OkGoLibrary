package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * author：fangkai on 2017/10/23 17:39
 * em：617716355@qq.com
 */
public class GovernmentAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {
    public GovernmentAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {


    }

}

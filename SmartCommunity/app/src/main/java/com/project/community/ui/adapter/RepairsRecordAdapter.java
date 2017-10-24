package com.project.community.ui.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.ui.me.RepairsEvaluateActivity;

import java.util.List;

/**
 * author：fangkai on 2017/10/24 17:23
 * em：617716355@qq.com
 */
public class RepairsRecordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RepairsRecordAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setOnClickListener(R.id.tv_evaluate, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, RepairsEvaluateActivity.class));

            }
        });

    }
}
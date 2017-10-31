package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.bean.GovernmentBean;
import com.project.community.constants.AppConstants;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * author：fangkai on 2017/10/23 17:39
 * em：617716355@qq.com
 */
public class GovernmentAdapter extends BaseQuickAdapter<GovernmentBean, BaseViewHolder> {
    public GovernmentAdapter(@LayoutRes int layoutResId, List<GovernmentBean> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, GovernmentBean item) {

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_time, item.getCreateDate());
        helper.setText(R.id.tv_comments, item.getComments() + "评论");


        if (item.getExpired().equals("0")) {
            helper.getView(R.id.iv_image).setVisibility(View.GONE);
            if (item.getWeight().equals("0")) {
                helper.getView(R.id.iv_image).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.iv_image).setVisibility(View.VISIBLE);
                helper.setImageResource(R.id.iv_image,R.mipmap.c1_bq1);

            }
        }else {
            helper.setImageResource(R.id.iv_image,R.mipmap.d64_bg);

        }

        Glide.with(mContext)
                .load(AppConstants.HOST + item.getImage())
                .into((ImageView) helper.getView(R.id.iv_img));

    }

}

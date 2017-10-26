package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.bean.ForumListBean;
import com.project.community.constants.AppConstants;

import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * author：fangkai on 2017/10/23 20:11
 * em：617716355@qq.com
 */
public class ForumAdapter extends BaseQuickAdapter<ForumListBean, BaseViewHolder> {
    public ForumAdapter(@LayoutRes int layoutResId, @Nullable List<ForumListBean> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumListBean item) {


        helper.setText(R.id.bbs_item_name, item.getUserName());
        helper.setText(R.id.bbs_item_time, item.getCreateDate());
        helper.setText(R.id.bbs_item_content, item.getContent());
        helper.setText(R.id.bbs_item_comment, item.getComments() + "评论");
        Glide.with(mContext)
                .load(AppConstants.HOST + item.getUserPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into((ImageView) helper.getView(R.id.bbs_item_head));
        List<String> result = Arrays.asList(item.getImagesUrl().split(","));

        if (result.size() == 1) {

            helper.getView(R.id.bbs_item_big_img).setVisibility(View.VISIBLE);
            helper.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            helper.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);

            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) helper.getView(R.id.bbs_item_big_img));

        } else if (result.size() == 2) {
            helper.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
            helper.getView(R.id.bbs_item_ll_two_img).setVisibility(View.VISIBLE);
            helper.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) helper.getView(R.id.bbs_item_ll_two_img_1));
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(1))
                    .into((ImageView) helper.getView(R.id.bbs_item_ll_two_img_2));

        } else if (result.size() == 3) {
            helper.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
            helper.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            helper.getView(R.id.bbs_item_ll_three_img).setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) helper.getView(R.id.bbs_item_ll_three_img_1));
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(1))
                    .into((ImageView) helper.getView(R.id.bbs_item_ll_three_img_2));
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(2))
                    .into((ImageView) helper.getView(R.id.bbs_item_ll_three_img_3));
        }


    }

}


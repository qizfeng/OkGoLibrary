package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.bean.ArticleIndexBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.ImageBrowseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * author：fangkai on 2017/10/24 13:38
 * em：617716355@qq.com
 */
public class IssueForumAdapter extends BaseQuickAdapter<ArticleIndexBean, BaseViewHolder> {
    public IssueForumAdapter(@LayoutRes int layoutResId, @Nullable List<ArticleIndexBean> data) {

        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ArticleIndexBean model) {


        baseViewHolder.setText(R.id.bbs_item_name, model.getUserName() + "");
        baseViewHolder.setText(R.id.bbs_item_time, model.getCreateDate() + "");
        baseViewHolder.setText(R.id.bbs_item_content, model.getContent() + "");
        baseViewHolder.setText(R.id.bbs_item_comment, model.getComments() + "评论");

        Glide.with(mContext)
                .load(AppConstants.HOST + model.getUserPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into((ImageView) baseViewHolder.getView(R.id.bbs_item_head));


        if (model.getIsShow() == 0) {
            baseViewHolder.setVisible(R.id.iv_is_show, false);
        } else {
            baseViewHolder.setVisible(R.id.iv_is_show, true);
        }


        List<String> result = Arrays.asList(model.getImagesUrl().split(","));

        final List<String>  img=new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            img.add(AppConstants.HOST +result.get(i).toString());
        }

        if (result.size() == 1) {

            baseViewHolder.getView(R.id.bbs_item_big_img).setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);

            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) baseViewHolder.getView(R.id.bbs_item_big_img));


            baseViewHolder.getView(R.id.bbs_item_big_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(mContext,new ArrayList<String> (img));
                }
            });

        } else if (result.size() == 2) {
            baseViewHolder.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) baseViewHolder.getView(R.id.bbs_item_ll_two_img_1));
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(1))
                    .into((ImageView) baseViewHolder.getView(R.id.bbs_item_ll_two_img_2));


            baseViewHolder.getView(R.id.bbs_item_ll_two_img_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(mContext,new ArrayList<String> (img));
                }
            });
            baseViewHolder.getView(R.id.bbs_item_ll_two_img_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(mContext,new ArrayList<String> (img));
                }
            });

        } else if (result.size() == 3) {
            baseViewHolder.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(0))
                    .into((ImageView) baseViewHolder.getView(R.id.bbs_item_ll_three_img_1));
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(1))
                    .into((ImageView) baseViewHolder.getView(R.id.bbs_item_ll_three_img_2));
            Glide.with(mContext)
                    .load(AppConstants.HOST + result.get(2))
                    .into((ImageView) baseViewHolder.getView(R.id.bbs_item_ll_three_img_3));
            baseViewHolder.getView(R.id.bbs_item_ll_three_img_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(mContext, new ArrayList<String> (img));
                }
            });
            baseViewHolder.getView(R.id.bbs_item_ll_three_img_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(mContext, new ArrayList<String> (img));
                }
            });
            baseViewHolder.getView(R.id.bbs_item_ll_three_img_3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(mContext, new ArrayList<String> (img));
                }
            });
        } else if (result.size() == 0) {
            baseViewHolder.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
        }

    }
}

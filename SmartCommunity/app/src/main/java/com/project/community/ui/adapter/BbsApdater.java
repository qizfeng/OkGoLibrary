package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.Event.BbsCollectEvent;
import com.project.community.R;
import com.project.community.bean.BbsBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.ui.ImageBrowseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by cj on 17/9/25
 * 论坛适配器
 */

public class BbsApdater extends BaseQuickAdapter<BbsBean.ArtListBean, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public BbsApdater(List<BbsBean.ArtListBean> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_bbs, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final BbsBean.ArtListBean model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });




        baseViewHolder.setOnClickListener(R.id.bbs_item_like_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        });

        baseViewHolder.getView(R.id.bbs_item_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BbsCollectEvent(model.getId()));
            }
        });


        baseViewHolder.setText(R.id.bbs_item_name, model.getUserName() + "");
        baseViewHolder.setText(R.id.bbs_item_time, model.getCreateDate() + "");
        baseViewHolder.setText(R.id.bbs_item_content, model.getContent() + "");
        baseViewHolder.setText(R.id.bbs_item_comment, model.getComments() + "评论");

        Glide.with(mContext)
                .load(AppConstants.HOST + model.getUserPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into((ImageView) baseViewHolder.getView(R.id.bbs_item_head));

        if (model.getStatus() != 0) {
            baseViewHolder.setImageResource(R.id.bbs_item_like, R.mipmap.c1_icon9_p);
        } else {
            baseViewHolder.setImageResource(R.id.bbs_item_like, R.mipmap.c1_icon9);
        }


        baseViewHolder.setText(R.id.bbs_item_like_num, model.getCollections() + "");
        baseViewHolder.setText(R.id.bbs_item_like_comment_num, model.getComments() + "");


        final List<String> result = Arrays.asList(model.getImagesUrl().split(","));

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

package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.model.NewsModel;
import com.project.community.model.ShopModel;
import com.project.community.ui.adapter.listener.MinshengAdapterItemListener;
import com.project.community.util.StringUtils;

import java.util.List;

/**
 * Created by qizfeng on 17/7/17.
 * 民生列表适配器
 */

public class MinshengAdapter extends BaseQuickAdapter<ShopModel, BaseViewHolder> {
    public MinshengAdapterItemListener itemClickListener;

    public MinshengAdapter(List<ShopModel> data, MinshengAdapterItemListener itemClick) {
        super(R.layout.layout_item_minsheng, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final ShopModel model) {
        baseViewHolder.setText(R.id.tv_title, StringUtils.ToDBC(model.shopsName));//
        GlideImageLoader glide = new GlideImageLoader();
        glide.onDisplayImageWithDefault(mContext, (ImageView) baseViewHolder.getView(R.id.iv_image),
                AppConstants.HOST + model.shopPhoto, R.mipmap.c1_image2);
        baseViewHolder.setText(R.id.tv_address, "地址:" + model.businessAddress);
        baseViewHolder.setText(R.id.tv_distance, "距离" + model.distance + "KM");
        double starLevel = model.starLevel;
        ImageView iv_star1 = (ImageView) baseViewHolder.getView(R.id.iv_star1);
        ImageView iv_star2 = (ImageView) baseViewHolder.getView(R.id.iv_star2);
        ImageView iv_star3 = (ImageView) baseViewHolder.getView(R.id.iv_star3);
        ImageView iv_star4 = (ImageView) baseViewHolder.getView(R.id.iv_star4);
        ImageView iv_star5 = (ImageView) baseViewHolder.getView(R.id.iv_star5);
        if (starLevel <= 0) {
            iv_star1.setImageResource(R.mipmap.d29_icon1_p);
            iv_star2.setImageResource(R.mipmap.d29_icon1_p);
            iv_star3.setImageResource(R.mipmap.d29_icon1_p);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel > 0 && starLevel < 1.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1_p);
            iv_star3.setImageResource(R.mipmap.d29_icon1_p);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 1.5 && starLevel < 2.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1_p);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 2.5 && starLevel < 3.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 3.5 && starLevel < 4.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1);
            iv_star4.setImageResource(R.mipmap.d29_icon1);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 4.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1);
            iv_star4.setImageResource(R.mipmap.d29_icon1);
            iv_star5.setImageResource(R.mipmap.d29_icon1);
        }
        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition() - 1;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, position);
            }
        });
    }

}

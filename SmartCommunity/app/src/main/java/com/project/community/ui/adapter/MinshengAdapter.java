package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hedgehog.ratingbar.RatingBar;
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
        if (model.isOpen==0) baseViewHolder.setVisible(R.id.tv_status,false);
        else baseViewHolder.setVisible(R.id.tv_status,true);
        RatingBar ratingBar = baseViewHolder.getView(R.id.ratingBar);
        double starLevel = model.starLevel;
        ratingBar.setStar((float) starLevel);
        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, position);
            }
        });
    }
}

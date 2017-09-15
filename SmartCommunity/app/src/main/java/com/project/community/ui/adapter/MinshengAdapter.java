package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.model.NewsModel;
import com.project.community.ui.adapter.listener.MinshengAdapterItemListener;
import com.project.community.util.StringUtils;

import java.util.List;

/**
 * Created by qizfeng on 17/7/17.
 * 民生列表适配器
 *
 */

public class MinshengAdapter extends BaseQuickAdapter<NewsModel, BaseViewHolder> {
    public MinshengAdapterItemListener itemClickListener;

    public MinshengAdapter(List<NewsModel> data, MinshengAdapterItemListener itemClick) {
        super(R.layout.layout_item_minsheng, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final NewsModel model) {
        baseViewHolder.setText(R.id.tv_title, StringUtils.ToDBC(model.title));//
        GlideImageLoader glide = new GlideImageLoader();
        glide.onDisplayImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_image), model.picUrl);
        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition()-1;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, position);
            }
        });
    }

}

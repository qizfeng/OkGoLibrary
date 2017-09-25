package com.project.community.ui.adapter;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.DateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.util.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by cj on 17/9/25
 * 公交列表适配器
 */

public class TransportationApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public TransportationApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_transportation, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentModel model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });

    }
}

package com.project.community.ui.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.DateUtil;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.NewsModel;
import com.project.community.model.SearchModel;
import com.project.community.util.StringUtils;

import java.util.List;

/**
 * Created by qizfeng on 17/8/13.
 */

public class SearchAdapter extends BaseQuickAdapter<SearchModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件
    public int index;

    public SearchAdapter(List<SearchModel> data, int index, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_search, data);
        itemClickListener = itemClick;
        this.index = index;
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (index == 0)
            return super.createBaseViewHolder(parent, R.layout.layout_item_search);
        else if (index == 1)
            return super.createBaseViewHolder(parent, R.layout.layout_item_search2);
        else if (index == 3)
            return super.createBaseViewHolder(parent, R.layout.layout_item_search3);//D32-02搜索页面
        return super.createBaseViewHolder(parent, R.layout.layout_item_search);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final SearchModel model) {
        /**
         *对指定位置加粗
         */
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //加粗
            result = Html.fromHtml(StringUtils.ToDBC("<b>[政务] </b>" + model.title), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(StringUtils.ToDBC(Html.fromHtml("<b>[资讯] </b>") + model.title));
        }
        /**
         *  * 设置黑色
         * 0代表从第几个字符开始变颜色，注意第一个字符序号是０
         * 3代表变色到第几个字符．
         */
        SpannableStringBuilder style = new SpannableStringBuilder(result);
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        baseViewHolder.setText(R.id.tv_content, model.description).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //****************************//
        baseViewHolder.setText(R.id.tv_comment_num, model.comments + "评论");
        try {
            if (index == 1)
                baseViewHolder.setText(R.id.tv_title, "【" + model.articleName + "】" + model.title);
            else if (index == 0) {
                baseViewHolder.setText(R.id.tv_content, "【" + model.articleName + "】" + model.title);
                if (TextUtils.isEmpty(model.surveyId)) {
                    baseViewHolder.setVisible(R.id.tv_wenjuan, false);
                } else {
                    if (!TextUtils.isEmpty(model.image))
                        baseViewHolder.setVisible(R.id.tv_wenjuan, true);
                    else
                        baseViewHolder.setVisible(R.id.tv_wenjuan, false);
                }
            }
            baseViewHolder.setText(R.id.tv_time, DateUtil.getCustomDateStr(Long.parseLong(model.createDate), "MM-dd HH:mm"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlideImageLoader glide = new GlideImageLoader();
        glide.onDisplayImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_image), AppConstants.HOST + model.image);
        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition() - 1;//去掉header的点击事件
        //item点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition());
            }
        });
    }

}

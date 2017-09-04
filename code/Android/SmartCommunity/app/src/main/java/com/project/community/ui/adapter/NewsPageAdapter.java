package com.project.community.ui.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.listener.DiggClickListener;
import com.project.community.model.NewsModel;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.util.StringUtils;

import java.util.List;

/**
 * Created by qizfeng on 17/7/13.
 * 首页瀑布流适配器
 */

public class NewsPageAdapter extends BaseQuickAdapter<NewsModel, BaseViewHolder> {
    public IndexAdapterItemListener itemClickListener;//点击事件
    private DiggClickListener diggClickListener;//点赞
    public NewsPageAdapter(List<NewsModel> data, IndexAdapterItemListener itemClick, DiggClickListener diggClickListener) {
        super(R.layout.layout_item_news, data);
        itemClickListener = itemClick;
        this.diggClickListener = diggClickListener;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final NewsModel model) {
        /**
         *对指定位置加粗
         */
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //加粗
            result = Html.fromHtml(StringUtils.ToDBC("【资讯】 " + model.title), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(StringUtils.ToDBC(Html.fromHtml("【资讯】 ") + model.title));
        }

        /**
         *  * 设置黑色
         * 0代表从第几个字符开始变颜色，注意第一个字符序号是０
         * 3代表变色到第几个字符．
         */
        SpannableStringBuilder style = new SpannableStringBuilder(result);
//        style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        baseViewHolder.setText(R.id.tv_content, result);

        //****************************//
        baseViewHolder.setText(R.id.tv_comment, baseViewHolder.getLayoutPosition() + "");
        baseViewHolder.setText(R.id.tv_zan, model.zanNum + "");
        GlideImageLoader glide = new GlideImageLoader();
        glide.onDisplayImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_imageView), model.picUrl);
        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition() - 1;//去掉header的点击事件
        //item点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition());
            }
        });

        //点赞
        baseViewHolder.getView(R.id.layout_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diggClickListener.onDiggClick((ImageView) baseViewHolder.getView(R.id.iv_zan_icon), (TextView) baseViewHolder.getView(R.id.tv_zan), baseViewHolder.getLayoutPosition());
            }
        });

        //评论
        baseViewHolder.getView(R.id.layout_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCommentClick(view, baseViewHolder.getLayoutPosition());
            }
        });


    }

}

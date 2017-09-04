package com.project.community.ui.adapter;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.DateUtil;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.DiggClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.NewsModel;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.util.StringUtils;

import java.util.List;

/**
 * Created by qizfeng on 17/8/28.
 */

public class ArticlePageAdapter extends BaseQuickAdapter<ArticleModel, BaseViewHolder> {
    public IndexAdapterItemListener itemClickListener;//点击事件
    private DiggClickListener diggClickListener;//点赞

    public ArticlePageAdapter(List<ArticleModel> data, IndexAdapterItemListener itemClick, DiggClickListener diggClickListener) {
        super(R.layout.layout_item_news, data);
        itemClickListener = itemClick;
        this.diggClickListener = diggClickListener;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final ArticleModel model) {
        /**
         *对指定位置加粗
         */
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //加粗
            result = Html.fromHtml(StringUtils.ToDBC("【" + model.articleName + "】 " + model.title), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(StringUtils.ToDBC(Html.fromHtml("【" + model.articleName + "】 ") + model.title));
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
        baseViewHolder.setText(R.id.tv_comment, model.comments + "");
        baseViewHolder.setText(R.id.tv_zan, model.collections + "");
        if (0 == model.status)
            baseViewHolder.setImageResource(R.id.iv_zan_icon, R.mipmap.c1_icon9);
        else if (1 == model.status)
            baseViewHolder.setImageResource(R.id.iv_zan_icon, R.mipmap.c1_icon9_p);
        try {
            long timeDiff = Long.parseLong(model.weightDate) - DateUtil.millis();
            if (timeDiff > 0) {
                if (999 == model.weight) {//权重999显示置顶
                    baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq1);
                    baseViewHolder.setVisible(R.id.iv_status, true);
                } else {
                    baseViewHolder.setVisible(R.id.iv_status, false);
                }
            } else {//失效
                baseViewHolder.setVisible(R.id.iv_status, true);
                baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (999 == model.weight) {//权重999显示置顶
                baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq1);
                baseViewHolder.setVisible(R.id.iv_status, true);
            } else {
                baseViewHolder.setVisible(R.id.iv_status, false);
            }
        }

        GlideImageLoader glide = new GlideImageLoader();
        glide.onDisplayImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_imageView), AppConstants.HOST + model.image);
        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition();//去掉header的点击事件
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

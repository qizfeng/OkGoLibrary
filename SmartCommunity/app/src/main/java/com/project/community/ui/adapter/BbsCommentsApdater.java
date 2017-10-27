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
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.CommentsListBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.util.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by qizfeng on 17/8/3.
 * 评论列表适配器
 */

public class BbsCommentsApdater extends BaseQuickAdapter<CommentsListBean.CommentsBean, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件
    public int total;

    public BbsCommentsApdater(List<CommentsListBean.CommentsBean> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_comment, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentsListBean.CommentsBean model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });
        baseViewHolder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onCustomClick(v, baseViewHolder.getLayoutPosition());
            }
        });


        Glide.with(mContext)
                .load(AppConstants.HOST + model.getPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into((ImageView) baseViewHolder.getView(R.id.iv_header));
        baseViewHolder.setText(R.id.tv_comment, model.getContent());
        baseViewHolder.setText(R.id.tv_name, model.getUserName());


        if (mData.size() > 0)
            if (mData.get(0).getId().equals(model.getId())) {
                baseViewHolder.setVisible(R.id.tv_comment_num, true);
//                baseViewHolder.setText(R.id.tv_comment_num, "评论(" + getTotalComments() + ")");
            } else {
                baseViewHolder.setVisible(R.id.tv_comment_num, false);

            }


        if (((BaseActivity) mContext).isLogin(mContext)) {
            String userId = ((BaseActivity) mContext).getUser(mContext).id;
            if (model.getUserId().equals(userId)) {
                baseViewHolder.setVisible(R.id.iv_delete, true);
            } else {
                baseViewHolder.setVisible(R.id.iv_delete, false);
            }
        } else {
            baseViewHolder.setVisible(R.id.iv_delete, false);
        }

        if (TextUtils.isEmpty(model.getTargetId()))
            baseViewHolder.setText(R.id.tv_comment, StringUtils.ToDBC(model.getContent()));
        else {
            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(StringUtils.ToDBC(mContext.getResources().getString(R.string.txt_receive) + model.getTargetName() + ":" + model.getContent()), Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(StringUtils.ToDBC(mContext.getResources().getString(R.string.txt_receive) + model.getTargetName() + ":" + model.getContent()));
            }
            /**
             *  * 设置蓝
             * 3代表从第几个字符开始变颜色，注意第一个字符序号是０
             * 3代表变色到第几个字符．
             */
            SpannableStringBuilder style = new SpannableStringBuilder(result);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), 3, 3 + model.getTargetName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            baseViewHolder.setText(R.id.tv_comment, style);
        }

//
//        baseViewHolder.setVisible(R.id.tv_comment_num, false);
//        if (baseViewHolder.getLayoutPosition() == 0) {
//            baseViewHolder.setVisible(R.id.tv_comment_num, true)
//                    .setText(R.id.tv_comment_num, "评论(" + getTotalComments() + ")");
//        }
//        Glide.with(mContext)
//                .load(AppConstants.HOST + model.photo)
//                .placeholder(R.mipmap.d54_tx)
//                .bitmapTransform(new CropCircleTransformation(mContext))
//                .into((ImageView) baseViewHolder.getView(R.id.iv_header));
//        baseViewHolder.setText(R.id.tv_comment, model.content);
//        if (TextUtils.isEmpty(model.targetId))
//            baseViewHolder.setText(R.id.tv_comment, StringUtils.ToDBC(model.content));
//        else {
//            Spanned result;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                result = Html.fromHtml(StringUtils.ToDBC(mContext.getResources().getString(R.string.txt_receive) + model.targetName + ":" + model.content), Html.FROM_HTML_MODE_LEGACY);
//            } else {
//                result = Html.fromHtml(StringUtils.ToDBC(mContext.getResources().getString(R.string.txt_receive) + model.targetName + ":" + model.content));
//            }
//            /**
//             *  * 设置蓝
//             * 3代表从第几个字符开始变颜色，注意第一个字符序号是０
//             * 3代表变色到第几个字符．
//             */
//            SpannableStringBuilder style = new SpannableStringBuilder(result);
//            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), 3, 3 + model.targetName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            baseViewHolder.setText(R.id.tv_comment, style);
//        }
//        try {
//            baseViewHolder.setText(R.id.tv_date, DateUtil.getCustomDateStr(Long.parseLong(model.createDate), "MM-dd HH:mm"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        baseViewHolder.setText(R.id.tv_name, model.userName);
    }

    /**
     * 设置评论总条数
     *
     * @param total
     */
    public void setTotalComments(int total) {
        this.total = total;
    }


    /**
     * 获取评论总条数
     *
     * @return
     */
    public int getTotalComments() {
        return this.total;
    }


}

package com.project.community.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.library.okgo.utils.DateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.model.UserModel;
import com.project.community.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by qizfeng on 17/7/24.
 * 弹窗出现的评论列表适配器
 */

public class CommentsPopwinAdapter extends BaseAdapter {
    public RecycleItemClickListener itemClickListener;
    private Context mContext;
    private List<CommentModel> mData;

    public CommentsPopwinAdapter(Context context, List<CommentModel> data, RecycleItemClickListener itemClick) {
        this.mData = data;
        this.mContext = context;
        itemClickListener = itemClick;

    }

    public void setData(List<CommentModel> data) {
        mData = new ArrayList<>();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CommentModel getItem(int i) {
        return mData.get(i);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_comment, null);
        ImageView iv_header = (ImageView) convertView.findViewById(R.id.iv_header);
        TextView tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_date);
        ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
        TextView tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);

        if (position == 0) {
            tv_comment_num.setVisibility(View.VISIBLE);
            tv_comment_num.setText("评论(" + this.getCount() + ")");
        } else
            tv_comment_num.setVisibility(View.GONE);
        final CommentModel item = mData.get(position);
        UserModel userModel = ((BaseActivity) mContext).getUser(mContext);
        String userId = "";
        if (userModel != null)
            userId = userModel.id;
        if (item.userId.equals(userId)) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(AppConstants.HOST+item.photo)
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(iv_header);
        if (TextUtils.isEmpty(item.targetId))
            tv_comment.setText(StringUtils.ToDBC(item.content));
        else {
            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                //加粗
                result = Html.fromHtml(StringUtils.ToDBC(mContext.getResources().getString(R.string.txt_receive)+ item.targetName + ":" + item.content), Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(StringUtils.ToDBC(mContext.getResources().getString(R.string.txt_receive) + item.targetName + ":" + item.content));
            }
            /**
             *  * 设置蓝
             * 3代表从第几个字符开始变颜色，注意第一个字符序号是０
             * 3代表变色到第几个字符．
             */
            SpannableStringBuilder style = new SpannableStringBuilder(result);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), 3, 3 + item.targetName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_comment.setText(style);
        }
        try {
            tv_time.setText(DateUtil.getCustomDateStr(Long.parseLong(item.createDate), "MM-dd HH:mm"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_name.setText(item.userName);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onCustomClick(v, position);
            }
        });
        return convertView;
    }
}


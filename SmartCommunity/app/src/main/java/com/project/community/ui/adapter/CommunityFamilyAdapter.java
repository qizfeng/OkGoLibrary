package com.project.community.ui.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommunityFamilyModel;
import com.project.community.model.FamilyModel;
import com.project.community.model.FamilyPersonModel;
import com.project.community.ui.community.CommunityFamilyActivity;
import com.project.community.ui.community.CommunityPersonActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by zipingfang on 17/9/15.
 */

public class CommunityFamilyAdapter extends BaseQuickAdapter<CommunityFamilyModel, BaseViewHolder> {
    private AdapterItemClickListener itemClickListener;

    public CommunityFamilyAdapter(List<CommunityFamilyModel> data, AdapterItemClickListener itemClick) {
        super(R.layout.layout_item_community_family, data);
        itemClickListener = itemClick;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final CommunityFamilyModel item) {
        List<FamilyPersonModel> personModels = item.memberList;
        final LinearLayout layoutFamily = helper.getView(R.id.layout_content);
        layoutFamily.removeAllViews();
        if (personModels.size() > 0) {
            for (int i = 0; i < personModels.size(); i++) {
                View personsView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_community_person, null);
                TextView tvHouseNo = (TextView) personsView.findViewById(R.id.tv_houseno);
                View view = personsView.findViewById(R.id.view_line);
                ImageView iv_header = personsView.findViewById(R.id.iv_header);
                TextView tv_name = personsView.findViewById(R.id.tv_name);
                TextView tv_phone = personsView.findViewById(R.id.tv_phone);
                TextView tv_tag1 = personsView.findViewById(R.id.tv_tag1);
                TextView tv_tag2 = personsView.findViewById(R.id.tv_tag2);
                TextView tv_tag3 = personsView.findViewById(R.id.tv_tag3);
                ImageView iv_tag = personsView.findViewById(R.id.iv_tag);
                if (i == personModels.size() - 1)
                    view.setVisibility(View.INVISIBLE);
                else
                    view.setVisibility(View.VISIBLE);
                if (i != 0)
                    tvHouseNo.setVisibility(View.INVISIBLE);
                else {
                    tvHouseNo.setVisibility(View.VISIBLE);
                    tvHouseNo.setText(item.number);
                }
                Glide.with(mContext)
                        .load(AppConstants.HOST + personModels.get(i).photo)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .placeholder(R.mipmap.d54_tx)
                        .into(iv_header);
                tv_name.setText(personModels.get(i).realName);
                if ("1".equals(personModels.get(i).headRelation))//是户主
                    iv_tag.setVisibility(View.VISIBLE);
                else//不是户主
                    iv_tag.setVisibility(View.GONE);
                tv_phone.setText(personModels.get(i).phone);
                if (!TextUtils.isEmpty(personModels.get(i).memberTag)) {
                    String[] tagArr = personModels.get(i).memberTag.split(",");
                    if (tagArr.length == 0) {
                        tv_tag1.setVisibility(View.GONE);
                        tv_tag2.setVisibility(View.GONE);
                        tv_tag3.setVisibility(View.GONE);
                    } else if (tagArr.length == 1) {
                        tv_tag1.setVisibility(View.VISIBLE);
                        tv_tag1.setText(tagArr[0]);
                        tv_tag2.setVisibility(View.GONE);
                        tv_tag3.setVisibility(View.GONE);
                    } else if (tagArr.length == 2) {
                        tv_tag1.setVisibility(View.VISIBLE);
                        tv_tag1.setText(tagArr[0]);
                        tv_tag2.setVisibility(View.VISIBLE);
                        tv_tag2.setText(tagArr[1]);
                        tv_tag3.setVisibility(View.GONE);
                    } else if (tagArr.length == 3) {
                        tv_tag1.setVisibility(View.VISIBLE);
                        tv_tag1.setText(tagArr[0]);
                        tv_tag2.setVisibility(View.VISIBLE);
                        tv_tag2.setText(tagArr[1]);
                        tv_tag3.setVisibility(View.VISIBLE);
                        tv_tag3.setText(tagArr[2]);
                    }

                } else {
                    tv_tag1.setVisibility(View.GONE);
                    tv_tag2.setVisibility(View.GONE);
                    tv_tag3.setVisibility(View.GONE);
                }
                final int index = i;
                personsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view, helper.getLayoutPosition(), index);
                    }
                });
                layoutFamily.addView(personsView);
            }
        }
    }

    public interface AdapterItemClickListener {

        /**
         * 社区-成员列表
         * 整个item的点击事件
         *
         * @param view
         * @param position
         */
        void onItemClick(View view, int position, int childPosition);

        /**
         * 文字部分点击事件
         *
         * @param view
         * @param position
         */
        void onCustomClick(View view, int position);


    }
}

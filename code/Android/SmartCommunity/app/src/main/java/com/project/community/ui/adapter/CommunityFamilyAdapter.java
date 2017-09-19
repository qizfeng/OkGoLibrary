package com.project.community.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.FamilyModel;
import com.project.community.model.FamilyPersonModel;
import com.project.community.ui.community.CommunityFamilyActivity;
import com.project.community.ui.community.CommunityPersonActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/9/15.
 */

public class CommunityFamilyAdapter extends BaseQuickAdapter<FamilyModel, BaseViewHolder> {
    private AdapterItemClickListener itemClickListener;
    private List<FamilyModel> mData = new ArrayList<>();

    public CommunityFamilyAdapter(List<FamilyModel> data, AdapterItemClickListener itemClick) {
        super(R.layout.layout_item_community_family, data);
        itemClickListener = itemClick;
        this.mData = data;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final FamilyModel item) {
        List<FamilyPersonModel> personModels = item.memberList;
        final LinearLayout layoutFamily = helper.getView(R.id.layout_content);
        layoutFamily.removeAllViews();
        if (personModels.size() > 0) {
            for (int i = 0; i < personModels.size(); i++) {
                View personsView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_community_person, null);
                TextView tvHouseNo = (TextView) personsView.findViewById(R.id.tv_houseno);
                View view = personsView.findViewById(R.id.view_line);
                if (i == personModels.size() - 1)
                    view.setVisibility(View.INVISIBLE);
                else
                    view.setVisibility(View.VISIBLE);
                if (i != 0)
                    tvHouseNo.setVisibility(View.INVISIBLE);
                else {
                    tvHouseNo.setVisibility(View.VISIBLE);

                }
                final int index = i;
                personsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view,helper.getLayoutPosition(),index);
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
         * @param view
         * @param position
         */
        void onItemClick(View view, int position,int childPosition);
        /**
         * 文字部分点击事件
         * @param view
         * @param position
         */
        void onCustomClick(View view, int position);


    }
}

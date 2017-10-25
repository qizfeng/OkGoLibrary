package com.project.community.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.project.community.R;
import com.project.community.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/25 18:11
 * em：617716355@qq.com
 */
public class RepairsDetailsAdapter extends BaseRecyclerAdapter<Object, RepairsDetailsAdapter.RepairsDetailsHolder> {


    private List<String> item = new ArrayList<>();


    public RepairsDetailsAdapter(Context context) {
        super(context);
    }

    public RepairsDetailsAdapter(Context context, List datas) {
        super(context, datas);
    }

    public void setThisItem(List<String> item) {
        this.item = item;
        notifyDataSetChanged();
    }

    public RepairsDetailsAdapter(Context context, Object[] datas) {
        super(context, datas);
    }


    @Override
    public RepairsDetailsAdapter.RepairsDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repairs_details, parent, false);
        return new RepairsDetailsAdapter.RepairsDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepairsDetailsAdapter.RepairsDetailsHolder holder, final int position) {


    }


    static class RepairsDetailsHolder extends BaseViewHolder {

        @Bind(R.id.ll_title)
        LinearLayout llTitle;
        @Bind(R.id.v_one)
        View vOne;
        @Bind(R.id.iv_on)
        ImageView ivOn;
        @Bind(R.id.v_tow)
        View vTow;
        @Bind(R.id.rv_data)
        RecyclerView rvData;
        @Bind(R.id.ratingBar)
        XLHRatingBar ratingBar;
        @Bind(R.id.v_bottom)
        View vBottom;

        RepairsDetailsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

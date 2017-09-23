package com.project.community.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.model.DictionaryModel;
import com.project.community.model.UnitModel;
import com.project.community.model.UserModel;

import java.util.List;

/**
 * Created by qizfeng on 17/9/18.
 * 选择单元适配器
 */

public class CommunityUnitSingleChoiceAdapter extends RecyclerView.Adapter<CommunityUnitSingleChoiceAdapter.InternalViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<DictionaryModel> data;
    /**
     * 默认为-1，没有选择任何item
     */
    private int currentCheckedItemPosition;
    private OnItemClickListener onItemClickListener;

    public CommunityUnitSingleChoiceAdapter(Context context, List<DictionaryModel> data,OnItemClickListener onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
        currentCheckedItemPosition = -1;
        this.onItemClickListener=onItemClickListener;
    }

    public DictionaryModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public InternalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InternalViewHolder(layoutInflater.inflate(R.layout.layout_item_communtiy_family_select, parent, false));
    }

    @Override
    public void onBindViewHolder(InternalViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position, getItemId(position));
                }
            }
        });

        if (currentCheckedItemPosition == position) {
            holder.ivCheck.setImageResource(R.mipmap.d10_btn1_p);
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }
        holder.textView.setText(getItem(position).label);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setDefaultCheckedItemPosition(int position) {
        currentCheckedItemPosition = position;
    }

    public int getCheckedItemPosition() {
        return currentCheckedItemPosition;
    }

    public void check(int position) {
        setDefaultCheckedItemPosition(position);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@NonNull OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class InternalViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView ivCheck;

        public InternalViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_unit);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, long id);
    }

}

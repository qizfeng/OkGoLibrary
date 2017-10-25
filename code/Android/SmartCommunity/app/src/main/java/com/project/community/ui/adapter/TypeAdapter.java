package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.Event.AddhouseEvent;
import com.project.community.Event.TypeEvent;
import com.project.community.R;
import com.project.community.base.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/25 15:53
 * em：617716355@qq.com
 */
public class TypeAdapter extends BaseRecyclerAdapter<Object, HomeNumberAdapter.HouseViewHolder> {



    private List<String> item=new ArrayList<>();


    public TypeAdapter(Context context) {
        super(context);
    }

    public TypeAdapter(Context context, List datas) {
        super(context, datas);
    }

    public void setThisItem(List<String> item) {
        this.item = item;
        notifyDataSetChanged();
    }

    public TypeAdapter(Context context, Object[] datas) {
        super(context, datas);
    }


    @Override
    public HomeNumberAdapter.HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        return new HomeNumberAdapter.HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeNumberAdapter.HouseViewHolder holder, final int position) {


        if (item.size()>0) {
            if (item.contains(mDatas.get(position))) {
//                holder.i.setChecked(true);
                holder.ivCheck.setImageResource(R.mipmap.d10_btn2_p);
                holder.llHouseNumber.setBackground(mContext.getDrawable(R.drawable.shape_blue_5dp));
                holder.tvHouseNuber.setTextColor(mContext.getColor(R.color.login_bottom_txt));

            } else {
                holder.ivCheck.setImageResource(R.mipmap.d10_btn2);
                holder.llHouseNumber.setBackground(mContext.getDrawable(R.drawable.shape_gray_5dp));
                holder.tvHouseNuber.setTextColor(mContext.getColor(R.color.color_gray_333333));


//                holder.cbCheck.setChecked(false);
            }
        }


        holder.tvHouseNuber.setText(String.valueOf(mDatas.get(position)));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new TypeEvent((String) mDatas.get(position)));

            }
        });

//        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
////                if (holder.cbCheck.isChecked()){
//                    EventBus.getDefault().post(new AddhouseEvent((String) mDatas.get(position)));
////                }else {
////                    EventBus.getDefault().post(new AddhouseEvent((String) mDatas.get(position)));
//
////                    EventBus.getDefault().post(new AddhouseEvent(position));
////                }
//            }
//        });


    }


    static class HouseViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_house_nuber)
        TextView tvHouseNuber;
        @Bind(R.id.iv_check)
        ImageView ivCheck;
        @Bind(R.id.ll_house_number)
        LinearLayout llHouseNumber;

        HouseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

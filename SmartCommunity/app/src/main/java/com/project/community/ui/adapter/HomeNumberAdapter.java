package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.Event.AddhouseEvent;
import com.project.community.R;
import com.project.community.base.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/25 13:57
 * em：617716355@qq.com
 */
public class HomeNumberAdapter extends BaseRecyclerAdapter<Object, HomeNumberAdapter.HouseViewHolder> {



    private String item;


    public HomeNumberAdapter(Context context) {
        super(context);
    }

    public HomeNumberAdapter(Context context, List datas) {
        super(context, datas);
    }

    public void setThisItem(String item) {
        this.item = item;
        notifyDataSetChanged();
    }

    public HomeNumberAdapter(Context context, Object[] datas) {
        super(context, datas);
    }


    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_house_number, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HouseViewHolder holder, final int position) {


        if (!item.equals("")) {
            if (item.equals(mDatas.get(position))) {
//                holder.i.setChecked(true);
                holder.ivCheck.setImageResource(R.mipmap.d10_btn1_p);
                holder.llHouseNumber.setBackground(mContext.getDrawable(R.drawable.shape_blue_5dp));
                holder.tvHouseNuber.setTextColor(mContext.getColor(R.color.login_bottom_txt));

            } else {
                holder.ivCheck.setImageResource(R.mipmap.d10_btn1);
                holder.llHouseNumber.setBackground(mContext.getDrawable(R.drawable.shape_gray_5dp));
                holder.tvHouseNuber.setTextColor(mContext.getColor(R.color.color_gray_333333));


//                holder.cbCheck.setChecked(false);
            }
        }


        holder.tvHouseNuber.setText("房屋编号" + String.valueOf(mDatas.get(position)));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new AddhouseEvent((String) mDatas.get(position)));

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

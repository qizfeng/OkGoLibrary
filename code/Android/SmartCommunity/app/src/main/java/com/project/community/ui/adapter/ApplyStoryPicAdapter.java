package com.project.community.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.community.App;
import com.project.community.R;

import java.util.List;

/**
 * Created by ZPF on 2016/9/26.
 */
public class ApplyStoryPicAdapter extends BaseAdapter {
    private List<String> datas;
    private Context context;
    private OnUrlListener onUrlListener;
    private ImageView mImagsPhoto,mImagsDel,mImagsAdd;
    private boolean isVisble =true;


    public interface OnUrlListener {
        void uoData(int pos);
    }
    public ApplyStoryPicAdapter(List<String> data, Context context) {
        this.datas = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (isVisble==true){
            return datas.size() + 1;
        }else {
            return datas.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_imags_close, null);
        mImagsPhoto = (ImageView) convertView.findViewById(R.id.imags_photo);
        mImagsDel = (ImageView) convertView.findViewById(R.id.imags_del);
        mImagsAdd = (ImageView) convertView.findViewById(R.id.imags_add);
        mImagsDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                isVisble=true;
                notifyDataSetChanged();
//                onUrlListener.uoData(position);
            }
        });

//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.height = (App.W - 30 * 2 - 32) / 5;
//        mImagsPhoto.setLayoutParams(layoutParams);

        if (position == getCount() - 1 && isVisble==true) {
            mImagsAdd.setVisibility(View.VISIBLE);
            mImagsDel.setVisibility(View.GONE);
        } else {
            mImagsAdd.setVisibility(View.GONE);
            mImagsDel.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    public void setGoneAdd(int yx){
        if (yx==1){
            isVisble=true;
        }else {
            isVisble=false;
        }
    }
}

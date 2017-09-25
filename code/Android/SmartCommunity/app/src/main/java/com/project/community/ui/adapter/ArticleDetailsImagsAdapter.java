package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.community.App;
import com.project.community.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zpf 何秉鸿 Cj  on 2017/9/25.
 */

public class ArticleDetailsImagsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> img_list;

    public ArticleDetailsImagsAdapter(Context context, List<String> img_list) {
        this.context = context;
        this.img_list = img_list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return img_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item_imags, null);
            holder = new ViewHolder(convertView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (App.W-30) / 3;
            layoutParams.height = (App.W-30) / 3;
            holder.imagPhoto.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.imag_photo)
        ImageView imagPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}

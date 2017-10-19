package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.model.MenuModel;

import java.util.List;

/**
 * Created by zipingfang on 17/8/28.
 */

public class MenuModuleAdapter extends BaseAdapter {
    Context context;
    List<MenuModel> list;

    public MenuModuleAdapter(Context _context, List<MenuModel> _list) {
        this.list = _list;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.layout_item_life_top, null);
        TextView tv_title =(TextView) convertView.findViewById(R.id.tv_title);
        ImageView ivIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
        MenuModel item = list.get(position);
        tv_title.setText(item.label);
//        ivIcon.setImageResource(item.res);

        return convertView;
    }
}



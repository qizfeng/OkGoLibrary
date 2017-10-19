package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.model.ModuleModel;

import java.util.List;

/**
 * Created by qizfeng on 17/8/13.
 */

public class ModuleAdapter extends BaseAdapter {
    Context context;
    List<ModuleModel> list;

    public ModuleAdapter(Context _context, List<ModuleModel> _list) {
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
        TextView tv_title = (TextView)convertView.findViewById(R.id.tv_title);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        ModuleModel moduleModel = list.get(position);
        tv_title.setText(moduleModel.title);
        ivIcon.setImageResource(moduleModel.res);
        return convertView;
    }
}


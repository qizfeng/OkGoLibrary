package com.project.community.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.ImageBrowserAdapter;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by qizfeng on 17/7/25.
 * 图片浏览
 */

public class ImageBrowseActivity extends BaseActivity {

    private ViewPager search_viewpager;

    private List<String> imgs;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_browse);

        this.position = getIntent().getIntExtra("position", 0);

        this.imgs = getIntent().getStringArrayListExtra("imgs");

        search_viewpager = (ViewPager) this.findViewById(R.id.imgs_viewpager);
//        search_viewpager.setOffscreenPageLimit(2);
        PagerAdapter adapter = new ImageBrowserAdapter(this, imgs);
        search_viewpager.setAdapter(adapter);
        search_viewpager.setCurrentItem(position);
    }

    public void onBack(View view) {
        finish();
    }
}
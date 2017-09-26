package com.project.community.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.ImageBrowserAdapter;

import java.util.ArrayList;
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

    public static void startActivity(Context context,ArrayList<String> imgs){
        Intent intent = new Intent(context,ImageBrowseActivity.class);
        intent.putStringArrayListExtra("imgs",imgs);
        context.startActivity(intent);
    }

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
        search_viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });
    }

    public void onBack(View view) {
        finish();
    }
}
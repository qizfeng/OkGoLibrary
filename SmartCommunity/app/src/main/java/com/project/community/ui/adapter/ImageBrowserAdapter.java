package com.project.community.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.library.okgo.utils.GlideImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by qifeng on 17/7/25.
 * 图片浏览适配器
 */

public class ImageBrowserAdapter extends PagerAdapter {

    List<String> imgs;

    Context mContext;

    public ImageBrowserAdapter(Context context, List<String> imgs) {

        this.mContext = context;
        this.imgs = imgs;

    }

    @Override
    public int getCount() { // 获得size
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        String imgUrl = imgs.get(position);
//        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.img_browse, null);
//        PhotoView img = (PhotoView) view.findViewById(R.id.img_plan);
        PhotoView photoView = new PhotoView(mContext);
        GlideImageLoader glideImageLoader = new GlideImageLoader();
        glideImageLoader.onDisplayImage(mContext, photoView, imgs.get(position));
        photoView.setTag(imgUrl);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;

    }


}
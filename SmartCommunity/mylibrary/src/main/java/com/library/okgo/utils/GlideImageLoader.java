package com.library.okgo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.library.okgo.R;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.ninegrid.NineGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

//import com.bumptech.glide.request.RequestOptions;
//import com.library.okgo.R;

/**
 * ================================================
 * 作    者：qizfeng Github地址：https://github.com/qizfeng
 * 版    本：1.0
 * 创建日期：16/9/5
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GlideImageLoader implements ImageLoader, NineGridView.ImageLoader {
    //4.0版本使用以下代码
    /*RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_default_color)
            .error(R.drawable.ic_default_color)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
*/

    /**
     * 圆形图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public void onDisplayImageCircle(Context context, ImageView imageView, Object url) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ((Bitmap) url).compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        Glide.with(context)
                .load(bytes)
                //4.0版本glide不在有以下三行方法,改为在RequestOptions中
                .placeholder(R.drawable.ic_default_color)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                .error(R.drawable.ic_default_color)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(context))
                // .apply(options)//4.0在requestoptions中使用
                .into(imageView);
    }

    /**
     * 设置默认图
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultRes
     */
    public void onDisplayImageWithDefault(Context context, ImageView imageView, String url, int defaultRes) {
        Glide.with(context).load(url)
                //4.0版本glide不在有以下三行方法,改为在RequestOptions中
                .placeholder(defaultRes)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                .error(defaultRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                // .apply(options)
                .into(imageView);

    }


    /**
     * 设置默认图
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultRes
     */
    public void onDisplayImageWithDefault(Context context, ImageView imageView, String url, int defaultRes, boolean isCircle) {
            Glide.with(context).load(url)
                    //4.0版本glide不在有以下三行方法,改为在RequestOptions中
                    .placeholder(defaultRes)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                    .error(defaultRes)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(context))
                    // .apply(options)
                    .into(imageView);
    }

    /**
     * 高斯模糊效果
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius
     */
    public void onDisplayImageBlur(Context context, ImageView imageView, Object url, int radius) {
        Glide.with(context)
                .load(url)
                //4.0版本glide不在有以下三行方法,改为在RequestOptions中
                .placeholder(R.drawable.ic_default_color)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                .error(R.drawable.ic_default_color)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(context, radius))
                // .apply(options)//4.0在requestoptions中使用
                .into(imageView);
    }

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        //  imageView.setTag(url);
        Glide.with(context)
                .load(url)
                //4.0版本glide不在有以下三行方法,改为在RequestOptions中
//                .placeholder(R.drawable.ic_default_color)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
//                .error(R.drawable.ic_default_color)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //  .centerCrop()
                //  .priority(Priority.HIGH)
                // .apply(options)//4.0在requestoptions中使用
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(new File(path))//
                //4.0版本glide不在有以下三行方法,改为在RequestOptions中
                .placeholder(R.drawable.ic_default_color)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                .error(R.drawable.ic_default_color)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                // .apply(options)
                .into(imageView);

    }


    @Override
    public void clearMemoryCache() {

    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public void clearMemoryCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

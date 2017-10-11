package com.project.community.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.DateUtil;
import com.library.okgo.utils.DeviceUtil;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.DiggClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.util.ScreenUtils;
import com.project.community.util.StringUtils;
import com.project.community.util.Utils;

import java.util.HashMap;
import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by qizfeng on 17/8/28.
 */

public class ArticlePageAdapter extends BaseQuickAdapter<ArticleModel, BaseViewHolder> {
    public IndexAdapterItemListener itemClickListener;//点击事件
    private DiggClickListener diggClickListener;//点赞
    private final static float SIZE_SCALE_01 = 4 / 3f;
    private final static float SIZE_SCALE_02 = 4 / 4f;
    private int screenWidth;
    private HashMap<Integer, Float> indexMap = new HashMap<>();
    private HashMap<Integer, Integer> imageHeightMap = new HashMap<>();

    public ArticlePageAdapter(List<ArticleModel> data, IndexAdapterItemListener itemClick, DiggClickListener diggClickListener) {
        super(R.layout.layout_item_news, data);
        itemClickListener = itemClick;
        this.diggClickListener = diggClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final ArticleModel model) {
        baseViewHolder.setIsRecyclable(false);
        /**
         *对指定位置加粗
         */
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //加粗
            result = Html.fromHtml(StringUtils.ToDBC("【" + model.articleName + "】 " + model.title), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(StringUtils.ToDBC(Html.fromHtml("【" + model.articleName + "】 ") + model.title));
        }

        /**
         *  * 设置黑色
         * 0代表从第几个字符开始变颜色，注意第一个字符序号是０
         * 3代表变色到第几个字符．
         */
        SpannableStringBuilder style = new SpannableStringBuilder(result);
//        style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        baseViewHolder.setText(R.id.tv_content, result);

        //****************************//
        baseViewHolder.setText(R.id.tv_comment, model.comments + "");
        baseViewHolder.setText(R.id.tv_zan, model.collections + "");
        if (0 == model.status)
            baseViewHolder.setImageResource(R.id.iv_zan_icon, R.mipmap.c1_icon9);
        else if (1 == model.status)
            baseViewHolder.setImageResource(R.id.iv_zan_icon, R.mipmap.c1_icon9_p);
        try {
            if (!TextUtils.isEmpty(model.weightDate)) {//失效时间不为空
                long timeDiff = Long.parseLong(model.weightDate) - DateUtil.millis();
                if (timeDiff > 0) {//失效时间大于当前时间
                    if (999 == model.weight) {//权重999显示hot
                        baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq1);
                        baseViewHolder.setVisible(R.id.iv_status, true);
                    } else {//不显示hot
                        baseViewHolder.setVisible(R.id.iv_status, false);
                    }
                } else {//显示失效
                    baseViewHolder.setVisible(R.id.iv_status, true);
                    baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq2);
                }
            } else {//失效时间为空
                if (999 == model.weight) {//权重999显示hot
                    baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq1);
                    baseViewHolder.setVisible(R.id.iv_status, true);
                } else {//不显示标签
                    baseViewHolder.setVisible(R.id.iv_status, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (999 == model.weight) {//权重999显示置顶
                baseViewHolder.setImageResource(R.id.iv_status, R.mipmap.c1_bq1);
                baseViewHolder.setVisible(R.id.iv_status, true);
            } else {
                baseViewHolder.setVisible(R.id.iv_status, false);
            }
        }

        if (TextUtils.isEmpty(model.surveyId)) {
            baseViewHolder.setVisible(R.id.tv_wenjuan, false);
        } else {
            if (!TextUtils.isEmpty(model.image))
                baseViewHolder.setVisible(R.id.tv_wenjuan, true);
            else
                baseViewHolder.setVisible(R.id.tv_wenjuan, false);
        }
        int imageWidth = (DeviceUtil.getDeviceWidth(mContext) - 30) / 2;
//        GlideImageLoader glide = new GlideImageLoader();
//        glide.onDisplayImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_imageView), AppConstants.HOST + model.image);
        if (model.imageWidth != 0 && model.imageHeight != 0) {
            if (!imageHeightMap.containsKey(baseViewHolder.getLayoutPosition())) {
                //当首次加载图片时，调用 loadImageFirst()，保存图片高度
                loadImageFirst(baseViewHolder.getView(R.id.iv_imageView), baseViewHolder.getLayoutPosition());
            } else {
                //非首次加载，直接根据保存的长宽，获取图片
                Glide.with(mContext)
                        .load(AppConstants.HOST + model.image).override(imageWidth, imageHeightMap.get(baseViewHolder.getLayoutPosition()))
                        .into((ImageView) baseViewHolder.getView(R.id.iv_imageView));

            }
        }
//        resizeItemView((ImageView) baseViewHolder.getView(R.id.iv_imageView), (float) model.imageWidth / (float) model.imageHeight);


        View view = baseViewHolder.getConvertView();
        final int position = baseViewHolder.getLayoutPosition();//去掉header的点击事件
        //item点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition());
            }
        });

        //点赞
        baseViewHolder.getView(R.id.layout_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diggClickListener.onDiggClick((ImageView) baseViewHolder.getView(R.id.iv_zan_icon), (TextView) baseViewHolder.getView(R.id.tv_zan), baseViewHolder.getLayoutPosition());
            }
        });

        //评论
        baseViewHolder.getView(R.id.layout_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCommentClick(view, baseViewHolder.getLayoutPosition());
            }
        });


    }

    private float getScaleType(int position) {
        if (!indexMap.containsKey(position)) {
            float scaleType;
            LogUtils.e("header:" + getHeaderLayoutCount());
            if (getHeaderLayoutCount() > 0) {
                if (position == 1) {
                    scaleType = SIZE_SCALE_01;
                } else if (position == 2) {
                    scaleType = SIZE_SCALE_02;
                } else {
                    scaleType = Utils.getRandomInt() % 2 == 0 ? SIZE_SCALE_01 : SIZE_SCALE_02;
                }
            } else {
                if (position == 0) {
                    scaleType = SIZE_SCALE_01;
                } else if (position == 1) {
                    scaleType = SIZE_SCALE_02;
                } else {
                    scaleType = Utils.getRandomInt() % 2 == 0 ? SIZE_SCALE_01 : SIZE_SCALE_02;
                }
            }
            indexMap.put(position, scaleType);
        }

        return indexMap.get(position);
    }

    private void resizeItemView(ImageView frontCoverImage, float scaleType) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frontCoverImage.getLayoutParams();
        screenWidth = ScreenUtils.getScreenWidth(mContext) - Utils.dp2px(mContext, 8) * 3;
        params.width = screenWidth / 2;
        params.height = (int) (params.width / scaleType) - Utils.dp2px(mContext, 8);
        LogUtils.e("height1:" + ((int) (params.width / scaleType) - Utils.dp2px(mContext, 8)));
        frontCoverImage.setLayoutParams(params);
    }


    public void loadImageFirst(View view, final int position) {
        final int imageWidth = (DeviceUtil.getDeviceWidth(mContext) - 30) / 2;
        //构造方法中参数view,就是回调方法中的this.view
        ViewTarget<View, Bitmap> target = new ViewTarget<View, Bitmap>(view) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //加载图片成功后调用
                float scaleType = ((float) resource.getHeight()) / resource.getWidth();
                int imageHeight = (int) (imageWidth * scaleType);
                //获取图片高度，保存在Map中
                imageHeightMap.put(position, imageHeight);
                //设置图片布局的长宽，Glide会根据布局的自动加载适应大小的图片
                ViewGroup.LayoutParams lp = this.view.getLayoutParams();
                lp.width = imageWidth;
                lp.height = imageHeight;
                this.view.setLayoutParams(lp);
                //resource就是加载成功后的图片资源
                ((ImageView) view).setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                //加载图片失败后调用
                super.onLoadFailed(e, errorDrawable);
                int imageHeight = imageWidth;
                imageHeightMap.put(position, imageHeight);
                ViewGroup.LayoutParams lp = this.view.getLayoutParams();
                lp.width = imageWidth;
                lp.height = imageHeight;
                this.view.setLayoutParams(lp);
                ((ImageView) view).setImageResource(R.mipmap.ic_launcher);
            }
        };
        Glide.with(mContext)
                .load(AppConstants.HOST + mData.get(position).image)
                .asBitmap()                 //作为Bitmap加载，对应onResourceReady回调中第一个参数的类型
                .into(target);
    }

}

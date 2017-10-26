package com.project.community.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.bean.MerchantBean;
import com.project.community.constants.AppConstants;
import com.project.community.util.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * author：fangkai on 2017/10/23 18:12
 * em：617716355@qq.com
 */
public class MerchantAdapter extends BaseQuickAdapter<MerchantBean, BaseViewHolder> {
    String[] address;

    public MerchantAdapter(@LayoutRes int layoutResId, @Nullable List<MerchantBean> data) {
        super(layoutResId, data);
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
//        AutoUtils.auto(view);
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantBean model) {
        helper.setText(R.id.tv_title, StringUtils.ToDBC(model.getShopsName()));//
        GlideImageLoader glide = new GlideImageLoader();
        glide.onDisplayImageWithDefault(mContext, (ImageView) helper.getView(R.id.iv_image),
                AppConstants.HOST + model.getShopPhoto(), R.mipmap.c1_image2);
        helper.setText(R.id.tv_address, "地址:" + model.getBusinessAddress());
//        helper.setText(R.id.tv_distance, "距离" + model. + "KM");
//        helper.setText(R.id.tv_distance, "距离" + "KM");


        if (address[0] != null && address[1] != null) {
            LatLng latLng = new LatLng(Double.valueOf(address[0]), Double.valueOf(address[1]));
            LatLng latLng1 = new LatLng(model.getLatitude(), model.getLongitude());
//
//
//
            double distance = DistanceUtil.getDistance(latLng, latLng1);

            Log.e("距离", distance + "米");


            DecimalFormat df = new DecimalFormat("0.00");

            String dz = String.valueOf(df.format((float) distance / 1000));

            helper.setText(R.id.tv_distance, "距离" + dz + "KM");
        }

        //判断是否通过审核
        if (model.getAuditStatus().equals("2")) {
            helper.getView(R.id.tv_state).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_state).setVisibility(View.GONE);

        }


        double starLevel = model.getStarLevel();
        ImageView iv_star1 = (ImageView) helper.getView(R.id.iv_star1);
        ImageView iv_star2 = (ImageView) helper.getView(R.id.iv_star2);
        ImageView iv_star3 = (ImageView) helper.getView(R.id.iv_star3);
        ImageView iv_star4 = (ImageView) helper.getView(R.id.iv_star4);
        ImageView iv_star5 = (ImageView) helper.getView(R.id.iv_star5);
        if (starLevel <= 0) {
            iv_star1.setImageResource(R.mipmap.d29_icon1_p);
            iv_star2.setImageResource(R.mipmap.d29_icon1_p);
            iv_star3.setImageResource(R.mipmap.d29_icon1_p);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel > 0 && starLevel < 1.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1_p);
            iv_star3.setImageResource(R.mipmap.d29_icon1_p);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 1.5 && starLevel < 2.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1_p);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 2.5 && starLevel < 3.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1);
            iv_star4.setImageResource(R.mipmap.d29_icon1_p);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 3.5 && starLevel < 4.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1);
            iv_star4.setImageResource(R.mipmap.d29_icon1);
            iv_star5.setImageResource(R.mipmap.d29_icon1_p);
        } else if (starLevel >= 4.5) {
            iv_star1.setImageResource(R.mipmap.d29_icon1);
            iv_star2.setImageResource(R.mipmap.d29_icon1);
            iv_star3.setImageResource(R.mipmap.d29_icon1);
            iv_star4.setImageResource(R.mipmap.d29_icon1);
            iv_star5.setImageResource(R.mipmap.d29_icon1);
        }
        View view = helper.getConvertView();
        final int position = helper.getLayoutPosition();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemClickListener.onItemClick(v, position);
            }
        });

    }

}

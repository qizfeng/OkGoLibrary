package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.DeviceUtil;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ModuleModel;
import com.project.community.model.WenjuanModel;

import java.util.List;

/**
 * Created by zipingfang on 17/10/17.
 */

public class ShopMuduleAdapter extends BaseQuickAdapter<ModuleModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public ShopMuduleAdapter(List<ModuleModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_payment_way, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final ModuleModel model) {
        baseViewHolder.setText(R.id.tv_payment_way, model.title).setTextColor(R.id.tv_payment_way,mContext.getResources().getColor(R.color.color_gray_666666));
        ImageView imageView = baseViewHolder.getView(R.id.iv_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.getLayoutParams().width=(int) DeviceUtil.dipToPx(mContext,40);
       // imageView.getLayoutParams().height=(int) DeviceUtil.dipToPx(mContext,40);
        imageView.setImageResource(model.res);
        final int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

}

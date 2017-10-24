package com.project.community.ui.me.shop_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.life.minsheng.AdrressActivity;
import com.project.community.ui.life.minsheng.ApplyStoreActivity;
import com.project.community.view.MyButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cj on 17/10/24.
 * 商铺资料
 */

public class ShopDataActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.shop_data_cover)
    ImageView shopDataCover;
    @Bind(R.id.shop_data_title)
    TextView shopDataTitle;
    @Bind(R.id.shop_data_type)
    TextView shopDataType;
    @Bind(R.id.shop_data_people)
    TextView shopDataPeople;
    @Bind(R.id.shop_data_address)
    TextView shopDataAddress;
    @Bind(R.id.shop_data_tv_zuobiao)
    TextView shopDataTvZuobiao;
    @Bind(R.id.tv_hospital_arrow)
    TextView tvHospitalArrow;
    @Bind(R.id.shop_data_ll_zuobiao)
    RelativeLayout shopDataLlZuobiao;
    @Bind(R.id.shop_data_tv_type)
    TextView shopDataTvType;
    @Bind(R.id.shop_data_tv_zhuyingyewu)
    TextView shopDataTvZhuyingyewu;
    @Bind(R.id.shop_data_tv_qiye_name)
    TextView shopDataTvQiyeName;
    @Bind(R.id.shop_data_tv_yingye_phone)
    TextView shopDataTvYingyePhone;
    @Bind(R.id.shop_data_tv_faren_name)
    TextView shopDataTvFarenName;
    @Bind(R.id.shop_data_yingye_photo_1)
    ImageView shopDataYingyePhoto1;
    @Bind(R.id.shop_data_yingye_photo_2)
    ImageView shopDataYingyePhoto2;
    @Bind(R.id.shop_data_faren_photo_1)
    ImageView shopDataFarenPhoto1;
    @Bind(R.id.shop_data_faren_photo_2)
    ImageView shopDataFarenPhoto2;
    @Bind(R.id.shop_data_btn_edit)
    MyButton shopDataBtnEdit;
    @Bind(R.id.shop_data_reason)
    TextView shop_data_reason;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopDataActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_data);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.shop_data_title), R.mipmap.iv_back);
    }

    @OnClick({R.id.shop_data_btn_edit,R.id.shop_data_ll_zuobiao})
    public void onViewClicked(View v) {

        switch (v.getId()){
            case R.id.shop_data_btn_edit:
                break;
            case R.id.shop_data_ll_zuobiao:
                Intent intent = new Intent(ShopDataActivity.this, AdrressActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    private double mLongitude; //
    private double mLatitude;   //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 100 && resultCode == 100) {
            mLatitude = data.getDoubleExtra("latitude", 0.0);
            mLongitude = data.getDoubleExtra("longitude", 0.0);
//            shopDataTvZuobiao.setText(data.getStringExtra("result"));
            shopDataTvZuobiao.setText(data.getStringExtra("result"));
        }
    }
}

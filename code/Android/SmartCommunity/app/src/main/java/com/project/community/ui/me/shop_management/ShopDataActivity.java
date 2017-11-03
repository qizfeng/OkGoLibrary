package com.project.community.ui.me.shop_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.ShopModel;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.ui.life.minsheng.AdrressActivity;
import com.project.community.ui.life.minsheng.ApplyStoreActivity;
import com.project.community.view.MyButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/10/24.
 * 商铺资料
 */

public class ShopDataActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.shop_data_cover)
    ImageView shopDataCover;
    @Bind(R.id.shop_data_title)
    TextView shopDataTitle;
    @Bind(R.id.shop_data_type)
    TextView shopDataType;
    @Bind(R.id.shop_data_people)
    TextView shopDataPeople;
    @Bind(R.id.shop_data_phone)
    TextView shopDataPhone;
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

    private double mLongitude; // 经度
    private double mLatitude;   //纬度
    private ShopModel mShopModel;
    private ArrayList<String> imgs= new ArrayList<>();

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
        getPropShops();
    }
    /**
     * 申请店铺
     *
     * @param
     */
    private void getPropShops() {
        progressDialog.show();
        serverDao.getPropShops(getUser(this).id, new JsonCallback<BaseResponse<ShopModel>>() {
                    @Override
                    public void onSuccess(BaseResponse<ShopModel> baseResponse, Call call, Response response) {
//                        showToast(baseResponse.message);
                        progressDialog.dismiss();
                        mShopModel=baseResponse.retData;
                        new GlideImageLoader().onDisplayImageWithDefault(ShopDataActivity.this, shopDataCover, AppConstants.URL_BASE+baseResponse.retData.shopPhoto, R.mipmap.c1_image2);
                        shopDataTitle.setText(baseResponse.retData.shopsName);
                        shopDataPeople.setText(getResources().getString(R.string.goods_order_lianxiren)+baseResponse.retData.contactName);
                        shopDataPhone.setText(getResources().getString(R.string.goods_order_lianxiphone)+baseResponse.retData.contactPhone);
                        shopDataAddress.setText(getResources().getString(R.string.goods_order_yingyedizhi)+baseResponse.retData.businessAddress);
//                        tvHospitalArrow.setText(baseResponse.retData.latitude+","+baseResponse.retData.longitude);
                        mLongitude= Double.parseDouble(baseResponse.retData.longitude);
                        mLatitude= Double.parseDouble(baseResponse.retData.latitude);
                        shopDataTvType.setText(baseResponse.retData.shopsCategory);
                        shopDataTvZhuyingyewu.setText(baseResponse.retData.mainBusiness);
                        shopDataTvQiyeName.setText(baseResponse.retData.entName);
                        shopDataTvYingyePhone.setText(baseResponse.retData.licenseNo);
                        imgs.add(AppConstants.URL_BASE+baseResponse.retData.licensePositive);
                        imgs.add(AppConstants.URL_BASE+baseResponse.retData.licenseReverse);
                        imgs.add(AppConstants.URL_BASE+baseResponse.retData.legalCardPositive);
                        imgs.add(AppConstants.URL_BASE+baseResponse.retData.legalCardReverse);
                        new GlideImageLoader().onDisplayImageWithDefault(ShopDataActivity.this, shopDataYingyePhoto1, AppConstants.URL_BASE+baseResponse.retData.licensePositive, R.mipmap.c1_image2);
                        new GlideImageLoader().onDisplayImageWithDefault(ShopDataActivity.this, shopDataYingyePhoto2, AppConstants.URL_BASE+baseResponse.retData.licenseReverse, R.mipmap.c1_image2);
                        shopDataTvFarenName.setText(baseResponse.retData.legalPerson);
                        new GlideImageLoader().onDisplayImageWithDefault(ShopDataActivity.this, shopDataFarenPhoto1, AppConstants.URL_BASE+baseResponse.retData.legalCardPositive, R.mipmap.c1_image2);
                        new GlideImageLoader().onDisplayImageWithDefault(ShopDataActivity.this, shopDataFarenPhoto2, AppConstants.URL_BASE+baseResponse.retData.legalCardReverse, R.mipmap.c1_image2);

                        shopDataType.setVisibility(View.VISIBLE);
                        if (baseResponse.retData.auditStatus.equals("1")){//未审核
                            shopDataType.setText(getResources().getString(R.string.goods_order_shenghei));
                            shop_data_reason.setText("");
                            shopDataBtnEdit.setVisibility(View.GONE);
                        }else if (baseResponse.retData.auditStatus.equals("2")){//审核通过
                            shopDataType.setText(getResources().getString(R.string.goods_order_shengheitongguo));
                            shop_data_reason.setText("");
                            shopDataBtnEdit.setVisibility(View.VISIBLE);
                        }else {//审核未通过
                            shopDataType.setText(getResources().getString(R.string.goods_order_shengheiweitongguo));
                            shop_data_reason.setText(getResources().getString(R.string.goods_order_shengheiweitongguoyuanyin)+baseResponse.retData.auditContent);
                            shopDataBtnEdit.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast(e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }
    @OnClick({R.id.shop_data_btn_edit,R.id.shop_data_ll_zuobiao,R.id.shop_data_yingye_photo_1,R.id.shop_data_yingye_photo_2,R.id.shop_data_faren_photo_1,R.id.shop_data_faren_photo_2})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.shop_data_btn_edit:
                ApplyStoreActivity.startActivity(this,mShopModel);
                break;
            case R.id.shop_data_ll_zuobiao:
                AdrressActivity.startActivity(ShopDataActivity.this,mLongitude+"",mLatitude+"");
                break;
            case R.id.shop_data_yingye_photo_1:
                ImageBrowseActivity.startActivity(this, imgs,0);
                break;
            case R.id.shop_data_yingye_photo_2:
                ImageBrowseActivity.startActivity(this, imgs,1);
                break;
            case R.id.shop_data_faren_photo_1:
                ImageBrowseActivity.startActivity(this, imgs,2);
                break;
            case R.id.shop_data_faren_photo_2:
                ImageBrowseActivity.startActivity(this, imgs,3);
                break;

        }
    }



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

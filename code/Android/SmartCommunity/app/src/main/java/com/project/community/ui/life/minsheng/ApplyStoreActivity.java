package com.project.community.ui.life.minsheng;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.ValidateUtil;
import com.library.okgo.utils.photo.PhotoUtils;
import com.library.okgo.view.loopview.LoopView;
import com.library.okgo.view.loopview.OnItemSelectedListener;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.DictionaryModel;
import com.project.community.model.DictionaryResponse;
import com.project.community.model.FileUploadModel;
import com.project.community.model.ShopModel;
import com.project.community.ui.SplashActivity;
import com.project.community.ui.adapter.CommunityUnitSingleChoiceAdapter;
import com.project.community.ui.community.CommunityFamilyActivity;
import com.project.community.ui.life.family.FamilyAddPersonActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.util.StringUtils;
import com.project.community.view.MyButton;
import com.project.community.view.crop.CropImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;
/**
 * Created by cj on 17/9/24.
 * 申请店铺
 */
public class ApplyStoreActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {


    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    @Bind(R.id.apply_store_img_business_license_zheng)
    ImageView applyStoreImgBusinessLicenseZheng;
    @Bind(R.id.apply_store_del_business_license_zheng)
    ImageView applyStoreDelBusinessLicenseZheng;
    @Bind(R.id.apply_store_img_add_business_license_zheng)
    FrameLayout applyStoreImgAddBusinessLicenseZheng;
    @Bind(R.id.apply_store_img_business_license_fan)
    ImageView applyStoreImgBusinessLicenseFan;
    @Bind(R.id.apply_store_del_business_license_fan)
    ImageView applyStoreDelBusinessLicenseFan;
    @Bind(R.id.apply_store_img_add_business_license_fan)
    FrameLayout applyStoreImgAddBusinessLicenseFan;
    @Bind(R.id.apply_store_img_legal_person_zheng)
    ImageView applyStoreImgLegalPersonZheng;
    @Bind(R.id.apply_store_del_legal_person_zheng)
    ImageView applyStoreDelLegalPersonZheng;
    @Bind(R.id.apply_store_img_add_legal_person_zheng)
    FrameLayout applyStoreImgAddLegalPersonZheng;
    @Bind(R.id.apply_store_img_legal_person_fan)
    ImageView applyStoreImgLegalPersonFan;
    @Bind(R.id.apply_store_del_legal_person_fan)
    ImageView applyStoreDelLegalPersonFan;
    @Bind(R.id.apply_store_img_add_legal_person_fan)
    FrameLayout applyStoreImgAddLegalPersonFan;


    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;


    private int iSCode = 0; // 1点击店铺,2点击营业执照正面,3.点击营业执照反面4,点击法人身份证正面5.点击法人身份证反面

    private String mStoreCoverUri;
    private String mBusinessLicenseZUri;
    private String mBusinessLicenseFUri;
    private String mLegalPersonZUri;
    private String mLegalPersonFUri;

    private double mLongitude; //
    private double mLatitude;   //

    private String shopsCategory;

    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.apply_store_et_title)
    EditText applyStoreEtTitle;
    @Bind(R.id.apply_store_et_name)
    EditText applyStoreEtName;
    @Bind(R.id.apply_store_et_tel)
    EditText applyStoreEtTel;
    @Bind(R.id.apply_store_tv_address)
    TextView applyStoreTvAddress;
    @Bind(R.id.apply_store_tv_type)
    TextView applyStoreTvType;
    @Bind(R.id.apply_store_et_important)
    EditText applyStoreEtImportant;
    @Bind(R.id.apply_store_et_company_name)
    EditText applyStoreEtCompanyName;
    @Bind(R.id.apply_store_et_business_license)
    EditText applyStoreEtBusinessLicense;
    //    @Bind(R.id.apply_store_gv_business_license)
//    MyGridView applyStoreGvBusinessLicense;
    @Bind(R.id.apply_store_et_legal_person)
    EditText applyStoreEtLegalPerson;
    //    @Bind(R.id.apply_store_gv_legal_person)
//    MyGridView applyStoreGvLegalPerson;
    @Bind(R.id.apply_store_img_cover)
    ImageView applyStoreImgCover;
    @Bind(R.id.apply_store_img_del)
    ImageView applyStoreImgDel;
    @Bind(R.id.apply_store_img_add)
    FrameLayout applyStoreImgAdd;
    @Bind(R.id.ll_apply_store_tv_address)
    LinearLayout llApplyStoreTvAddress;
    @Bind(R.id.ll_apply_store_tv_type)
    LinearLayout llApplyStoreTvType;
    @Bind(R.id.apply_store_btn_confire)
    MyButton applyStoreBtnConfire;

    private LoopView mLoopView;
    private List<DictionaryModel> mDictionaryModelList = new ArrayList<>();
    private List<String> strings =new ArrayList<>();

    private boolean isData=false;
    private String id="";

//    private ApplyStoryPicAdapter mApplyStoryPicAdapterBusinessLicense;
//    private ApplyStoryPicAdapter mApplyStoryPicAdapterGvLegalPerson;
//    private List<String> mImagsBusinessLicense = new ArrayList<>(); // 营业执照照片集合
//    private List<String> mImagsGvLegalPersone = new ArrayList<>();//法人照片集合

    public static void startActivity(Context context, ShopModel shopModel) {
        Intent intent = new Intent(context, ApplyStoreActivity.class);
        intent.putExtra("value",shopModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.store_Apply), R.mipmap.iv_back);

        if (getIntent().getExtras()!=null){
            mTvTitle.setText(getResources().getString(R.string.store_Apply_edit));
            ShopModel shopModel = (ShopModel) getIntent().getSerializableExtra("value");
            applyStoreEtTitle.setText(shopModel.shopsName);
            if (!TextUtils.isEmpty(applyStoreEtTitle.getText().toString())){
                applyStoreEtTitle.setSelection(applyStoreEtTitle.getText().length());
            }
            applyStoreEtName.setText(shopModel.contactName);
            applyStoreEtTel.setText(shopModel.contactPhone);
            applyStoreTvAddress.setText(shopModel.businessAddress);
            applyStoreTvType.setText(shopModel.shopsCategory);
            applyStoreEtImportant.setText(shopModel.mainBusiness);
            applyStoreEtCompanyName.setText(shopModel.entName);
            applyStoreEtBusinessLicense.setText(shopModel.licenseNo);

            id=shopModel.id;
            mStoreCoverUri=shopModel.shopPhoto;//店铺封面
            if (!TextUtils.isEmpty(mStoreCoverUri)){
                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgCover, AppConstants.URL_BASE+mStoreCoverUri, R.mipmap.c1_image2);
                applyStoreImgAdd.setVisibility(View.GONE);
                applyStoreImgDel.setVisibility(View.VISIBLE);
            }
            mBusinessLicenseZUri=shopModel.licensePositive;//
            if (!TextUtils.isEmpty(mBusinessLicenseZUri)){
                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgBusinessLicenseZheng, AppConstants.URL_BASE+mBusinessLicenseZUri, R.mipmap.c1_image2);
                applyStoreImgAddBusinessLicenseZheng.setVisibility(View.GONE);
                applyStoreDelBusinessLicenseZheng.setVisibility(View.VISIBLE);
            }
            mBusinessLicenseFUri=shopModel.licenseReverse;//
            if (!TextUtils.isEmpty(mBusinessLicenseFUri)){
                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgBusinessLicenseFan, AppConstants.URL_BASE+mBusinessLicenseFUri, R.mipmap.c1_image2);
                applyStoreImgAddBusinessLicenseFan.setVisibility(View.GONE);
                applyStoreDelBusinessLicenseFan.setVisibility(View.VISIBLE);
            }
            mLegalPersonZUri=shopModel.legalCardPositive;//
            if (!TextUtils.isEmpty(mLegalPersonZUri)){
                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgLegalPersonZheng, AppConstants.URL_BASE+mLegalPersonZUri, R.mipmap.c1_image2);
                applyStoreImgAddLegalPersonZheng.setVisibility(View.GONE);
                applyStoreDelLegalPersonZheng.setVisibility(View.VISIBLE);
            }
            mLegalPersonFUri=shopModel.legalCardReverse;//
            if (!TextUtils.isEmpty(mLegalPersonFUri)){
                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgLegalPersonFan, AppConstants.URL_BASE+mLegalPersonFUri, R.mipmap.c1_image2);
                applyStoreImgAddLegalPersonFan.setVisibility(View.GONE);
                applyStoreDelLegalPersonFan.setVisibility(View.VISIBLE);
            }

        }

        applyStoreEtImportant.setOnTouchListener(this);

        RxView.clicks(applyStoreBtnConfire)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked(applyStoreBtnConfire);
                    }
                });
        RxView.clicks(llApplyStoreTvType)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked(llApplyStoreTvType);
                    }
                });
        RxView.clicks(llApplyStoreTvAddress)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked(llApplyStoreTvAddress);
                    }
                });

//        /**
//         * 营业执照照片
//         */
//        mApplyStoryPicAdapterBusinessLicense = new ApplyStoryPicAdapter(mImagsBusinessLicense, this);
//        applyStoreGvBusinessLicense.setAdapter(mApplyStoryPicAdapterBusinessLicense);
//        applyStoreGvBusinessLicense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (mImagsBusinessLicense.size() >= 2) {
//                    return;
//                }
//                if (i == mApplyStoryPicAdapterBusinessLicense.getCount() - 1) {
//                    mImagsBusinessLicense.add("");
//                    if (mImagsBusinessLicense.size() >= 2) {
//                        mApplyStoryPicAdapterBusinessLicense.setGoneAdd(0);
//                    } else {
//                        mApplyStoryPicAdapterBusinessLicense.setGoneAdd(1);
//                    }
//                    mApplyStoryPicAdapterBusinessLicense.notifyDataSetChanged();
//                }
//
//            }
//        });
//
//        /**
//         * 法人身份照片
//         */
//        mApplyStoryPicAdapterGvLegalPerson = new ApplyStoryPicAdapter(mImagsGvLegalPersone, this);
//        applyStoreGvLegalPerson.setAdapter(mApplyStoryPicAdapterGvLegalPerson);
//        applyStoreGvLegalPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (mImagsGvLegalPersone.size() >= 2) {
//                    return;
//                }
//                if (i == mApplyStoryPicAdapterGvLegalPerson.getCount() - 1) {
//                    mImagsGvLegalPersone.add("");
//                    if (mImagsGvLegalPersone.size() >= 2) {
//                        mApplyStoryPicAdapterGvLegalPerson.setGoneAdd(0);
//                    } else {
//                        mApplyStoryPicAdapterGvLegalPerson.setGoneAdd(1);
//                    }
//                    mApplyStoryPicAdapterGvLegalPerson.notifyDataSetChanged();
//                }
//
//            }
//        });

        getUnitData();
    }


    @OnClick({R.id.ll_apply_store_tv_address, R.id.ll_apply_store_tv_type, R.id.apply_store_btn_confire, R.id.apply_store_img_add, R.id.apply_store_img_del,
            R.id.apply_store_del_business_license_zheng, R.id.apply_store_img_add_business_license_zheng, R.id.apply_store_del_business_license_fan,
            R.id.apply_store_img_add_business_license_fan, R.id.apply_store_del_legal_person_zheng, R.id.apply_store_img_add_legal_person_zheng,
            R.id.apply_store_del_legal_person_fan, R.id.apply_store_img_add_legal_person_fan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_apply_store_tv_address:
                Intent intent = new Intent(ApplyStoreActivity.this, AdrressActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_apply_store_tv_type:
//                if (!isData){
//                    getUnitData();
//                }else {
                    showDialog(getResources().getString(R.string.send_message_change_type),mDictionaryModelList);
//                }
                break;
            case R.id.apply_store_btn_confire:
                if (TextUtils.isEmpty(mStoreCoverUri)){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_upcover_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtTitle.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_title_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtName.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_name_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtTel.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_tel_toast));
                    return;
                }
                if (!ValidateUtil.isPhone(applyStoreEtTel.getText().toString())) {
                    showToast(getString(R.string.toast_error_phone));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreTvAddress.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_address_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreTvType.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_type_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtImportant.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_important_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtCompanyName.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_company_name_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtBusinessLicense.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_business_license_toast));
                    return;
                }
                if (TextUtils.isEmpty(mBusinessLicenseZUri)){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_business_license_photo_zheng_toast));
                    return;
                }
                if (TextUtils.isEmpty(mBusinessLicenseFUri)){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_business_license_photo_fan_toast));
                    return;
                }
                if (TextUtils.isEmpty(applyStoreEtLegalPerson.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_legal_person_toast));
                    return;
                }
                if (TextUtils.isEmpty(mLegalPersonZUri)){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_legal_person_photo_zheng_toast));
                    return;
                }
                if (TextUtils.isEmpty(mLegalPersonFUri)){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.store_apply_legal_person_photo_fan_toast));
                    return;
                }
                propShops(id);
                break;
            case R.id.apply_store_img_add: //点击添加店铺封面
                iSCode = 1;
                showPhotoDialog();
                break;
            case R.id.apply_store_img_del: //点击删除店铺封面
                applyStoreImgAdd.setVisibility(View.VISIBLE);
                applyStoreImgDel.setVisibility(View.GONE);
                break;
            case R.id.apply_store_del_business_license_zheng: // 点击删除营业执照正面
                applyStoreImgAddBusinessLicenseZheng.setVisibility(View.VISIBLE);
                applyStoreDelBusinessLicenseZheng.setVisibility(View.GONE);
                break;
            case R.id.apply_store_img_add_business_license_zheng:// 点击添加营业执照正面
                iSCode = 2;
                showPhotoDialog();
                break;
            case R.id.apply_store_del_business_license_fan: // 点击删除营业执照反面
                applyStoreImgAddBusinessLicenseFan.setVisibility(View.VISIBLE);
                applyStoreDelBusinessLicenseFan.setVisibility(View.GONE);
                break;
            case R.id.apply_store_img_add_business_license_fan: // 点击添加营业执照反面面
                iSCode = 3;
                showPhotoDialog();
                break;
            case R.id.apply_store_del_legal_person_zheng:  // 点击删除法人正面
                applyStoreImgAddLegalPersonZheng.setVisibility(View.VISIBLE);
                applyStoreDelLegalPersonZheng.setVisibility(View.GONE);
                break;
            case R.id.apply_store_img_add_legal_person_zheng: // 点击添加法人正面
                iSCode = 4;
                showPhotoDialog();
                break;
            case R.id.apply_store_del_legal_person_fan:  // 点击删除法人反面
                applyStoreImgAddLegalPersonFan.setVisibility(View.VISIBLE);
                applyStoreDelLegalPersonFan.setVisibility(View.GONE);
                break;
            case R.id.apply_store_img_add_legal_person_fan: // 点击添加法人反面
                iSCode = 5;
                showPhotoDialog();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_take_photo:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                autoObtainCameraPermission();
                break;
            case R.id.tv_pick_photo:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                autoObtainStoragePermission();
                break;
            case R.id.tv_cancel:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 100 && resultCode == 100) {
            mLatitude = data.getDoubleExtra("latitude",0.0);
            mLongitude = data.getDoubleExtra("longitude",0.0);
            applyStoreTvAddress.setText(data.getStringExtra("result"));
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
//                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
//                    uploadFile(fileCropUri);//上传图片
                    CropImageActivity.startCrop(this, imageUri.toString(), output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        try {
                            cropImageUri = Uri.fromFile(fileCropUri);
                            Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(this, "com.project.community", new File(newUri.getPath()));
                            }
//                            PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                            CropImageActivity.startCrop(this, newUri.toString(), output_X, output_Y, CODE_RESULT_REQUEST);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast(getString(R.string.toast_error_photo));
                        }
//                        uploadFile(new File( Uri.parse(PhotoUtils.getPath(this, data.getData())).getPath()));
                    } else {
                        showToast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if (data != null) {
                        switch (iSCode){
                            case 1:
//                                mStoreCoverUri=data.getStringExtra("uri");
                                Log.e("onActivityResult: ", mStoreCoverUri);
                                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgCover, data.getStringExtra("uri"), R.mipmap.c1_image2);
                                applyStoreImgAdd.setVisibility(View.GONE);
                                applyStoreImgDel.setVisibility(View.VISIBLE);
                                break;
                            case 2:
//                                mBusinessLicenseZUri=data.getStringExtra("uri");
                                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgBusinessLicenseZheng, data.getStringExtra("uri"), R.mipmap.c1_image2);
                                applyStoreImgAddBusinessLicenseZheng.setVisibility(View.GONE);
                                applyStoreDelBusinessLicenseZheng.setVisibility(View.VISIBLE);
                                break;
                            case 3:
//                                mBusinessLicenseFUri=data.getStringExtra("uri");
                                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgBusinessLicenseFan, data.getStringExtra("uri"), R.mipmap.c1_image2);
                                applyStoreImgAddBusinessLicenseFan.setVisibility(View.GONE);
                                applyStoreDelBusinessLicenseFan.setVisibility(View.VISIBLE);
                                break;
                            case 4:
//                                mLegalPersonZUri=data.getStringExtra("uri");
                                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgLegalPersonZheng, data.getStringExtra("uri"), R.mipmap.c1_image2);
                                applyStoreImgAddLegalPersonZheng.setVisibility(View.GONE);
                                applyStoreDelLegalPersonZheng.setVisibility(View.VISIBLE);
                                break;
                            case 5:
//                                mLegalPersonFUri=data.getStringExtra("uri");
                                new GlideImageLoader().onDisplayImageWithDefault(this, applyStoreImgLegalPersonFan, data.getStringExtra("uri"), R.mipmap.c1_image2);
                                applyStoreImgAddLegalPersonFan.setVisibility(View.GONE);
                                applyStoreDelLegalPersonFan.setVisibility(View.VISIBLE);
                                break;
                        }

                        Uri uri = Uri.parse(data.getStringExtra("uri"));
                        Bitmap bitmap = PhotoUtils.getBitmapFromUri(uri, this);
                        if (bitmap != null) {
                            uploadFile(new File(StringUtils.getRealFilePath(ApplyStoreActivity.this, uri)));
                            LogUtils.e("uri:" + StringUtils.getRealFilePath(ApplyStoreActivity.this, uri));
                        }
//                        mImags.add(data.getStringExtra("uri"));
//                        if (mImags.size() >= 9) {
//                            mApplyStoryPicAdapter.setGoneAdd(0);
//                        } else {
//                            mApplyStoryPicAdapter.setGoneAdd(1);
//                        }
//                        mApplyStoryPicAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showToast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this, "com.project.community", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                showToast("设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private static final int output_X = 480;
    private static final int output_Y = 480;
    private PopupWindow mPopupWindow;


    public void showPhotoDialog() {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_photo, null);
        TextView tv_take_photo = (TextView) inflate.findViewById(R.id.tv_take_photo);
        TextView tv_pick_photo = (TextView) inflate.findViewById(R.id.tv_pick_photo);
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_pick_photo.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        mPopupWindow = new PopupWindow(this);
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(mLayoutRoot, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }

    private int mCurrtindex=0;
    private void showDialog(String title, final List<DictionaryModel> list) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        if (!TextUtils.isEmpty(applyStoreTvType.getText().toString())){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).label.equals(applyStoreTvType.getText().toString())){
                    mCurrtindex=i;
                }
            }
        }else {
            strings.clear();
            for (int i = 0; i < list.size(); i++) {
                strings.add(list.get(i).label);
            }
        }


        //初始化控件
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyStoreTvType.setText(list.get(mCurrtindex).label);
                shopsCategory= list.get(mCurrtindex).value;
                mPopupWindow.dismiss();
            }
        });

        mLoopView = (LoopView) inflate.findViewById(R.id.loopView);
        mLoopView.setItems(strings);
        mLoopView.setInitPosition(mCurrtindex);
        mLoopView.setNotLoop();
        mLoopView.setCenterTextColor(getResources().getColor(R.color.txt_color));
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurrtindex=index;

            }
        });
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = new PopupWindow(this);
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(mLayoutRoot, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.e("onTouch---->",canVerticalScroll(applyStoreEtImportant)+"" );
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if (view.getId() == R.id.apply_store_et_important && canVerticalScroll(applyStoreEtImportant)){
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


    /**
     * 文件上传
     *
     * @param file
     */
    private void uploadFile(File file) {
        serverDao.uploadFile(file, new JsonCallback<BaseResponse<FileUploadModel>>() {
            @Override
            public void onSuccess(BaseResponse<FileUploadModel> baseResponse, Call call, Response response) {
                switch (iSCode){
                    case 1:
                        mStoreCoverUri=baseResponse.retData.filePath;
                        break;
                    case 2:
                        mBusinessLicenseZUri=baseResponse.retData.filePath;
                        break;
                    case 3:
                        mBusinessLicenseFUri=baseResponse.retData.filePath;
                        break;
                    case 4:
                        mLegalPersonZUri=baseResponse.retData.filePath;
                        break;
                    case 5:
                        mLegalPersonFUri=baseResponse.retData.filePath;
                        break;

                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
                switch (iSCode){
                    case 1:
                        mStoreCoverUri="";
                        applyStoreImgAdd.setVisibility(View.VISIBLE);
                        applyStoreImgDel.setVisibility(View.GONE);
                        break;
                    case 2:
                        mBusinessLicenseZUri="";
                        applyStoreImgAddBusinessLicenseZheng.setVisibility(View.VISIBLE);
                        applyStoreDelBusinessLicenseZheng.setVisibility(View.GONE);
                        break;
                    case 3:
                        mBusinessLicenseFUri="";
                        applyStoreImgAddBusinessLicenseFan.setVisibility(View.VISIBLE);
                        applyStoreDelBusinessLicenseFan.setVisibility(View.GONE);
                        break;
                    case 4:
                        mLegalPersonZUri="";
                        applyStoreImgAddLegalPersonZheng.setVisibility(View.VISIBLE);
                        applyStoreDelLegalPersonZheng.setVisibility(View.GONE);
                        break;
                    case 5:
                        mLegalPersonFUri="";
                        applyStoreImgAddLegalPersonFan.setVisibility(View.VISIBLE);
                        applyStoreDelLegalPersonFan.setVisibility(View.GONE);
                        break;

                }
            }
        });
    }

    /**
     * 获取分类
     */
    private void getUnitData() {
        serverDao.getDictionaryData("prop_shops_type", new JsonCallback<BaseResponse<DictionaryResponse>>() {
            @Override
            public void onSuccess(BaseResponse<DictionaryResponse> baseResponse, Call call, Response response) {
                mDictionaryModelList.clear();
                mDictionaryModelList.addAll(baseResponse.retData.dictList);
//                if (isData){
//                    showDialog(getResources().getString(R.string.send_message_change_type),mDictionaryModelList);
//                }
//                isData=true;
            }
        });
    }
    /**
     * 申请店铺
     *
     * @param
     */
    private void propShops(String id) {
        progressDialog.show();
        Log.e("propShops: ", "--------"+shopsCategory);
        serverDao.propShops(id,getUser(this).id,
                mLongitude,
                mLatitude,
                applyStoreEtTitle.getText().toString(),
                mStoreCoverUri,
                applyStoreEtName.getText().toString(),
                applyStoreTvAddress.getText().toString(),
                shopsCategory,
                applyStoreEtImportant.getText().toString(),
                applyStoreEtCompanyName.getText().toString(),
                applyStoreEtBusinessLicense.getText().toString(),
                mBusinessLicenseZUri,
                mBusinessLicenseFUri,
                applyStoreEtLegalPerson.getText().toString(),
                mLegalPersonZUri,
                mLegalPersonFUri,
                new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                ToastUtils.showLongToast(ApplyStoreActivity.this,baseResponse.message);
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
                progressDialog.dismiss();
            }
        });
    }
}



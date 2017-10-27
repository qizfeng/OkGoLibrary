package com.project.community.ui.me.shop_management;

import android.Manifest;
import android.app.Activity;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.library.okgo.utils.photo.PhotoUtils;
import com.project.community.Event.AddGoodsEvent;
import com.project.community.Event.AddHouseEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.FileUploadModel;
import com.project.community.model.GoodsManagerModel;
import com.project.community.ui.life.SearchActivity;
import com.project.community.ui.life.minsheng.ApplyStoreActivity;
import com.project.community.ui.life.minsheng.BBSActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.util.StringUtils;
import com.project.community.view.crop.CropImageActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by cj on 17/10/24.
 * 添加商品
 */

public class BuildNewGoodsActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {

    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.build_new_et_name)
    EditText buildNewEtName;
    @Bind(R.id.build_new_img_cover)
    ImageView buildNewImgCover;
    @Bind(R.id.build_new_img_del)
    ImageView buildNewImgDel;
    @Bind(R.id.build_new_img_add)
    FrameLayout buildNewImgAdd;
    @Bind(R.id.build_new_et_describe)
    EditText buildNewEtDescribe;
    @Bind(R.id.build_new_tv_describe_num)
    TextView buildNewTvDescribeNum;
    @Bind(R.id.build_new_et_sell_price)
    EditText buildNewEtSellPrice;
    @Bind(R.id.build_new_et_price)
    EditText buildNewEtPrice;
    @Bind(R.id.build_new_et_unit)
    EditText buildNewEtUnit;
    @Bind(R.id.build_new_et_inventory)
    EditText buildNewEtInventory;

    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private String mStoreCoverUri;
    private String shopId;
    private String goodId="";

    public static void startActivity(Context context){
        Intent intent = new Intent(context,BuildNewGoodsActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, GoodsManagerModel goodsManagerModel,String shopId ){
        Intent intent = new Intent(context,BuildNewGoodsActivity.class);
        intent.putExtra("cj",goodsManagerModel);
        intent.putExtra("shopId",shopId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_new_goods);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.build_new_goods_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        if (getIntent().getExtras()!=null && getIntent().getSerializableExtra("cj")!=null){
            mTvTitle.setText(getResources().getString(R.string.build_new_goods_title_edit));
            GoodsManagerModel model = (GoodsManagerModel) getIntent().getSerializableExtra("cj");
            goodId=model.goodId;
            buildNewEtName.setText(model.name);
            if (!TextUtils.isEmpty(buildNewEtName.getText().toString())){
                buildNewEtName.setSelection(buildNewEtName.getText().length());
            }
            mStoreCoverUri=model.images;
            if (!TextUtils.isEmpty(mStoreCoverUri)){
                new GlideImageLoader().onDisplayImageWithDefault(this, buildNewImgCover, AppConstants.URL_BASE+mStoreCoverUri, R.mipmap.c1_image2);
                buildNewImgAdd.setVisibility(View.GONE);
                buildNewImgDel.setVisibility(View.VISIBLE);
            }
            buildNewEtDescribe.setText(model.description);
            buildNewTvDescribeNum.setText(buildNewEtDescribe.getText().length()+"/60");
            buildNewEtSellPrice.setText(model.price);
            buildNewEtPrice.setText(model.originalPrice);
            buildNewEtUnit.setText(model.unit);
            buildNewEtInventory.setText(model.stock);
        }
        shopId=getIntent().getStringExtra("shopId");
        buildNewEtDescribe.setOnTouchListener(this);
        buildNewEtDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                buildNewTvDescribeNum.setText(editable.length()+"/60");
            }
        });
        RxView.clicks(buildNewImgAdd)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked(buildNewImgAdd);
                    }
                });
    }

    @OnClick({R.id.build_new_img_add,R.id.build_new_img_del})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.build_new_img_add:
                showPhotoDialog();
                break;
            case R.id.build_new_img_del:
                mStoreCoverUri="";
                buildNewImgAdd.setVisibility(View.VISIBLE);
                buildNewImgDel.setVisibility(View.GONE);
                break;
        }
    }

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int output_X = 480;
    private static final int output_Y = 480;

    private PopupWindow mPopupWindow;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    CropImageActivity.startCrop(this, imageUri.toString(), output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        try {
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
//                        mStoreCoverUri=data.getStringExtra("uri");
                        new GlideImageLoader().onDisplayImageWithDefault(this, buildNewImgCover, data.getStringExtra("uri"), R.mipmap.c1_image2);
                        buildNewImgAdd.setVisibility(View.GONE);
                        buildNewImgDel.setVisibility(View.VISIBLE);
                        Uri uri = Uri.parse(data.getStringExtra("uri"));
                        Bitmap bitmap = PhotoUtils.getBitmapFromUri(uri, this);
                        if (bitmap != null) {
                            uploadFile(new File(StringUtils.getRealFilePath(BuildNewGoodsActivity.this, uri)));
                            LogUtils.e("uri:" + StringUtils.getRealFilePath(BuildNewGoodsActivity.this, uri));
                        }

                    }

                    break;
            }
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.e("onTouch---->",canVerticalScroll(buildNewEtDescribe)+"" );
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if (view.getId() == R.id.build_new_et_describe && canVerticalScroll(buildNewEtDescribe)){
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setTitle(R.string.build_new_goods_issue);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/
            case R.id.action_favorite:
                if (TextUtils.isEmpty(buildNewEtName.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_name_hit));
                    return false;
                }
                if (TextUtils.isEmpty(mStoreCoverUri)){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_photo_hit));
                    return false;
                }
                if (TextUtils.isEmpty(buildNewEtDescribe.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_character_describe_hit));
                    return false;
                }
                if (TextUtils.isEmpty(buildNewEtSellPrice.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_sell_price_hit));
                    return false;
                }
                if (TextUtils.isEmpty(buildNewEtPrice.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_price_hit));
                    return false;
                }
                if (TextUtils.isEmpty(buildNewEtUnit.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_unit_hit));
                    return false;
                }
                if (TextUtils.isEmpty(buildNewEtInventory.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.build_new_goods_inventory_hit));
                    return false;
                }
                propShops(goodId,shopId);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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
                mStoreCoverUri=baseResponse.retData.filePath;

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
                mStoreCoverUri="";
                buildNewImgAdd.setVisibility(View.VISIBLE);
                buildNewImgDel.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 添加或者保存商品
     *
     * @param
     */
    private void propShops(String goodId,String shopId) {
        progressDialog.show();
        serverDao.addGoods(getUser(this).id,
                goodId,
                shopId,
                buildNewEtName.getText().toString(),
                mStoreCoverUri,
                buildNewEtDescribe.getText().toString(),
                buildNewEtSellPrice.getText().toString(),
                buildNewEtPrice.getText().toString(),
                buildNewEtUnit.getText().toString(),
                buildNewEtInventory.getText().toString(),
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                        ToastUtils.showLongToast(BuildNewGoodsActivity.this,baseResponse.message);
                        progressDialog.dismiss();
                        EventBus.getDefault().post(new AddGoodsEvent("cj"));
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

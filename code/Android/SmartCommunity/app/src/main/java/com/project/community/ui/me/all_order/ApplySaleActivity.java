package com.project.community.ui.me.all_order;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.photo.PhotoUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.GoodsOrderDetailApdater;
import com.project.community.ui.adapter.SendMessageAdapter;
import com.project.community.util.ScreenUtils;
import com.project.community.view.MyGridView;
import com.project.community.view.crop.CropImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cj on 17/9/27.
 * 申请售后页面
 */

public class ApplySaleActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int output_X = 480;
    private static final int output_Y = 480;

    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;

    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.recylerview)
    RecyclerView recylerview;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.build_new_tv_describe_num)
    TextView buildNewTvDescribeNum;
    @Bind(R.id.gridview)
    MyGridView gridview;

    private GoodsOrderDetailApdater mAdapter;//商品详情订单适配器
    List<CommentModel> list =new ArrayList<>();

    private List<String> mImags = new ArrayList<>(); //上传照片
    private SendMessageAdapter mApplyStoryPicAdapter;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplySaleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_sale);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.apply_sale_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        etComment.setOnTouchListener(this);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                buildNewTvDescribeNum.setText(editable.length()+"/350");
            }
        });
        for (int i = 0; i < 2; i++) {
            CommentModel commentModel =new CommentModel();
            commentModel.id="0";
            list.add(commentModel);
        }
        recylerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GoodsOrderDetailApdater(list, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onCustomClick(View view, int position) {
            }
        });
        recylerview.setAdapter(mAdapter);


        mApplyStoryPicAdapter = new SendMessageAdapter(mImags, this);
        gridview.setAdapter(mApplyStoryPicAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImags.size() >= 3) {
                    return;
                }
                if (i == mApplyStoryPicAdapter.getCount() - 1) {
                    showPhotoDialog();

                }

            }
        });
    }
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.e("onTouch---->",canVerticalScroll(etComment)+"" );
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ( ((view.getId() == R.id.et_comment && canVerticalScroll(etComment)))){
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    Log.e("onActivityResult: ",imageUri.toString() );
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
                        mImags.add(data.getStringExtra("uri"));
                        if (mImags.size() >= 3) {
                            mApplyStoryPicAdapter.setGoneAdd(0);
                        } else {
                            mApplyStoryPicAdapter.setGoneAdd(1);
                        }
                        mApplyStoryPicAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setTitle(R.string.take_delivery_of_goods_commit);
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
                if (TextUtils.isEmpty(etComment.getText().toString())){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.apply_sale_tuihuo_hit));
                    return false;
                }
                if (mImags.size()==0){
                    ToastUtils.showLongToast(this,getResources().getString(R.string.apply_sale_pingzheng_hit));
                    return false;
                }
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.project.community.ui.life.family;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.B;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ValidateUtil;
import com.library.okgo.utils.photo.PhotoUtils;
import com.library.okgo.view.loopview.LoopView;
import com.library.okgo.view.loopview.OnItemSelectedListener;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/8/25.
 * 添加人口
 */

public class FamilyAddPersonActivity extends BaseActivity implements View.OnClickListener {
    public static final int CHOISE_RELATIVE = 1;//选择和户主关系
    public static final int CHOISE_GENDER = 2;//选择性别
    public static final int CHOISE_NATION = 3;//选择民族
    public static final int CHOISE_RELIGION = 4;//选择宗教信仰
    public static final int CHOISE_PARTY = 5;//选择党派

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.layout_header)
    RelativeLayout mLayoutHeader;
    @Bind(R.id.layout_name)
    RelativeLayout mLayoutName;
    @Bind(R.id.layout_relative)
    RelativeLayout mLayoutRelative;
    @Bind(R.id.layout_position)
    RelativeLayout mLayoutPosition;
    @Bind(R.id.layout_gender)
    RelativeLayout mLayotuGender;
    @Bind(R.id.layout_idcard)
    RelativeLayout mLayoutIdcard;
    @Bind(R.id.layout_age)
    RelativeLayout mLayoutAge;
    @Bind(R.id.layout_birthday)
    RelativeLayout mLayoutBirthday;
    @Bind(R.id.layout_nation)
    RelativeLayout mLayoutNation;
    @Bind(R.id.layout_religion)
    RelativeLayout mLayoutReligion;
    @Bind(R.id.layout_party)
    RelativeLayout mLayoutParty;
    @Bind(R.id.layout_mobile)
    RelativeLayout mLayoutMobile;
    @Bind(R.id.layout_address)
    RelativeLayout mLayoutAddress;

    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.tv_name)
    EditText mEtName;
    @Bind(R.id.tv_position)
    EditText mEtPosition;
    @Bind(R.id.tv_idcard)
    EditText mEtIdcard;
    @Bind(R.id.tv_mobile)
    EditText mEtMobile;
    @Bind(R.id.tv_address)
    EditText mEtAddress;

    @Bind(R.id.tv_relative)
    TextView mTvRelative;
    @Bind(R.id.tv_gender)
    TextView mTvGender;
    @Bind(R.id.tv_age)
    TextView mTvAge;
    @Bind(R.id.tv_birthday)
    TextView mTvBirthday;
    @Bind(R.id.tv_nation)
    TextView mTvNation;
    @Bind(R.id.tv_religion)
    TextView mTvReligion;
    @Bind(R.id.tv_party)
    TextView mTvParty;
    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.scrollView)
    NestedScrollView mScrollView;
    private PopupWindow mPopupWindow;
    private LoopView mLoopView;
    private Dialog mDialog;
    private int choiseFlag = 1;//选择框标记

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FamilyAddPersonActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_add_person);
        initView();
    }

    private void initView() {
        KeyBoardUtils.closeKeybord(mEtName,this);
        mTvRelative.setOnClickListener(this);
        mTvGender.setOnClickListener(this);
        mTvNation.setOnClickListener(this);
        mTvReligion.setOnClickListener(this);
        mTvParty.setOnClickListener(this);
        mLayoutHeader.setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            initToolBar(mToolBar, mTvTitle, true, bundle.getString("title"), R.mipmap.iv_back);
        }

        mEtIdcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ValidateUtil.personIdValidation(charSequence.toString())) {
                    String age = ValidateUtil.getUserAgeByCardId(charSequence.toString());
                    String birthday = ValidateUtil.getUserBrithdayByCardId(charSequence.toString());
                    mTvAge.setText(age);
                    mTvBirthday.setText(birthday);
                } else {
                    mTvAge.setText("");
                    mTvBirthday.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                KeyBoardUtils.closeKeybord(mEtName,FamilyAddPersonActivity.this);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_header:
                KeyBoardUtils.closeKeybord(mEtName,this);
                showPhotoDialog();
                break;
            case R.id.tv_relative:
                LogUtils.e("======");
                showDialog(getString(R.string.txt_choise_relative));
                choiseFlag = CHOISE_RELATIVE;
                KeyBoardUtils.closeKeybord(mEtName, FamilyAddPersonActivity.this);
                break;
            case R.id.tv_gender:
                showDialog(getString(R.string.txt_choise_gender));
                choiseFlag = CHOISE_GENDER;
                KeyBoardUtils.closeKeybord(mEtName, FamilyAddPersonActivity.this);
                break;
            case R.id.tv_nation:
                showDialog(getString(R.string.txt_chose_nation));
                choiseFlag = CHOISE_NATION;
                KeyBoardUtils.closeKeybord(mEtName, FamilyAddPersonActivity.this);
                break;
            case R.id.tv_religion:
                showDialog(getString(R.string.txt_choise_religion));
                choiseFlag = CHOISE_RELIGION;
                KeyBoardUtils.closeKeybord(mEtName, FamilyAddPersonActivity.this);
                break;
            case R.id.tv_party:
                showDialog(getString(R.string.txt_choise_party));
                choiseFlag = CHOISE_PARTY;
                KeyBoardUtils.closeKeybord(mEtName, FamilyAddPersonActivity.this);
                break;
            case R.id.tv_cancel:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.tv_confirm:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    switch (choiseFlag) {
                        case CHOISE_RELATIVE:
                            mTvRelative.setText(mLoopView.getCurrentItem().toString());
                            break;
                        case CHOISE_GENDER:
                            mTvGender.setText(mLoopView.getCurrentItem().toString());
                            break;
                        case CHOISE_NATION:
                            mTvNation.setText(mLoopView.getCurrentItem().toString());
                            break;
                        case CHOISE_RELIGION:
                            mTvReligion.setText(mLoopView.getCurrentItem().toString());
                            break;
                        case CHOISE_PARTY:
                            mTvParty.setText(mLoopView.getCurrentItem().toString());
                            break;
                    }
                }
                break;
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
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                onSave();
                return true;
            case android.R.id.home:
                if (checkValue()) {
                    showAlertDialog(getString(R.string.txt_unsave));
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setTitle(getString(R.string.txt_save));
        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * 保存
     */
    private void onSave() {
        String name = mEtName.getText().toString();
        String idcard = mEtIdcard.getText().toString();
        String mobile = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast(getString(R.string.toast_empty_name));
            return;
        }

        if (name.length() > 25) {
            showToast(getString(R.string.toast_error_name));
            return;
        }

        if (TextUtils.isEmpty(idcard)) {
            showToast(getString(R.string.toast_empty_idcard));
            return;
        }

        if (!ValidateUtil.personIdValidation(idcard)) {
            showToast(getString(R.string.toast_error_idcard));
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            showToast(getString(R.string.toast_empty_mobile));
            return;
        }
        if (!ValidateUtil.isMobileNO(mobile)) {
            showToast(getString(R.string.toast_error_phone));
            return;
        }

        finish();
    }

    private void showDialog(String title) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        List<String> strings = new ArrayList<>();
        strings.add("接口数据1");
        strings.add("接口数据2");

        //初始化控件
        TextView tv_cancel = (TextView)inflate.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView)inflate.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView)inflate.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        mLoopView = (LoopView) inflate.findViewById(R.id.loopView);
        mLoopView.setItems(strings);
        mLoopView.setNotLoop();
        mLoopView.setCenterTextColor(getResources().getColor(R.color.txt_color));
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

            }
        });
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


    private boolean checkValue() {
        boolean isCheck = false;
        String name = mEtName.getText().toString();
        String idcard = mEtIdcard.getText().toString();
        String mobile = mEtMobile.getText().toString();
        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(idcard) || !TextUtils.isEmpty(mobile)) {
            isCheck = true;
        }
        return isCheck;
    }


    public void showAlertDialog(String content) {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content =(TextView) mDialog.getWindow().findViewById(R.id.tv_content);
        tv_content.setText(content);
        Button btn_confirm = (Button) mDialog.getWindow().findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button)mDialog.getWindow().findViewById(R.id.btn_cancel);
        btn_cancel.setText(getString(R.string.txt_dont_leave));
        btn_confirm.setText(getString(R.string.txt_leave));
        ImageView iv_close = (ImageView) mDialog.getWindow().findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                finish();
            }
        });
    }


    public void showPhotoDialog() {

        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_photo, null);
        TextView tv_take_photo = (TextView)inflate.findViewById(R.id.tv_take_photo);
        TextView tv_pick_photo =(TextView) inflate.findViewById(R.id.tv_pick_photo);
        TextView tv_cancel = (TextView)inflate.findViewById(R.id.tv_cancel);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (checkValue()) {
                showAlertDialog(getString(R.string.txt_unsave));
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(FamilyAddPersonActivity.this, "com.project.community", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        showToast("设备没有SD卡！");
                    }
                } else {
                    showToast("请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    showToast("请允许操作SDCard！！");
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

    private static final int output_X = 480;
    private static final int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.project.community", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        showToast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
    }

    private void showImages(Bitmap bitmap) {
//        mIvHeader.setImageBitmap(bitmap);
        glide.onDisplayImageCircle(this,mIvHeader,bitmap);

    }
}
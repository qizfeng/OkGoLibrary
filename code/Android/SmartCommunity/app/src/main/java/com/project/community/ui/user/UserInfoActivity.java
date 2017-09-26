package com.project.community.ui.user;

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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
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
import com.project.community.model.UserModel;
import com.project.community.model.UserResponse;
import com.project.community.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/25.
 * 个人资料
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_NICKNAME = 10001;
    public static final int REQUEST_CODE_HOUSENO = 10002;
    public static final int REQUEST_CODE_POSITION = 10003;
    public static final int CHOISE_RELATIVE = 1;//选择和户主关系
    public static final int CHOISE_OWNER = 2;//是否是业主
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
    @Bind(R.id.layout_nickname)
    RelativeLayout mLayoutNickname;
    @Bind(R.id.layout_name)
    RelativeLayout mLayoutName;
    @Bind(R.id.layout_bind_mobile)
    RelativeLayout mLayoutBindMobile;
    @Bind(R.id.layout_modify_pwd)
    RelativeLayout mLayoutModifyPwd;
    @Bind(R.id.layout_houseno)
    RelativeLayout mLayoutHouseNo;
    @Bind(R.id.layout_is_owner)
    RelativeLayout mLayoutIsOwner;
    @Bind(R.id.layout_relative)
    RelativeLayout mLayoutRelative;
    @Bind(R.id.layout_position)
    RelativeLayout mLayoutPosition;
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

    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.tv_nickname)
    TextView mTvNikename;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_bind_mobile)
    TextView mTvBindMobile;
    @Bind(R.id.tv_modify_pwd)
    TextView mTvModifyPwd;
    @Bind(R.id.tv_houseno)
    TextView mTvHouseNo;
    @Bind(R.id.tv_is_owner)
    TextView mTvIsOwner;
    @Bind(R.id.tv_position)
    TextView mTvPosition;
    @Bind(R.id.tv_idcard)
    TextView mTvIdcard;
    @Bind(R.id.tv_relative)
    TextView mTvRelative;
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
    private PopupWindow mPopupWindow;
    private LoopView mLoopView;
    private AlertDialog mDialog;
    private int choiseFlag = 1;//选择框标记
    private List<DictionaryModel> dictionaryModels = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();
        loadData();
    }

    private void initView() {
        mTvRelative.setOnClickListener(this);
        mTvNation.setOnClickListener(this);
        mTvReligion.setOnClickListener(this);
        mTvParty.setOnClickListener(this);
        mLayoutHeader.setOnClickListener(this);
        mLayoutNickname.setOnClickListener(this);
        mLayoutHouseNo.setOnClickListener(this);
        mLayoutPosition.setOnClickListener(this);
        mLayoutModifyPwd.setOnClickListener(this);
        mLayoutIsOwner.setOnClickListener(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_userinfo), R.mipmap.iv_back);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ModifyUserInfoActivity.class);
        switch (view.getId()) {
            case R.id.layout_header://修改头像
                showPhotoDialog();
                break;
            case R.id.layout_nickname://修改昵称
                intent.putExtra("title", getString(R.string.activity_modify_nickname));
                intent.putExtra("type", 1);
                intent.putExtra("content", mTvNikename.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_NICKNAME);
                break;
            case R.id.layout_modify_pwd://修改密码
                ModifyPwdActivity.startActivity(this);
                break;
            case R.id.layout_houseno://修改房屋编号
                intent.putExtra("title", getString(R.string.activity_modify_houseno));
                intent.putExtra("type", 2);
                intent.putExtra("content", mTvHouseNo.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_HOUSENO);
                break;
            case R.id.layout_position://修改职业
                intent.putExtra("title", getString(R.string.activity_modify_position));
                intent.putExtra("type", 3);
                intent.putExtra("content", mTvPosition.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_POSITION);
                break;
            case R.id.tv_relative://修改关系
                // showDialog(getString(R.string.txt_choise_relative));
                choiseFlag = CHOISE_RELATIVE;
                break;
            case R.id.layout_is_owner:
                choiseFlag = CHOISE_OWNER;
                getDictionary(getString(R.string.txt_choise_owner), "sys_user_is_owner");
                break;
            case R.id.tv_nation://修改民族
                getDictionary(getString(R.string.txt_chose_nation), "sys_user_nation");
                choiseFlag = CHOISE_NATION;
                break;
            case R.id.tv_religion://修改宗教信仰
                getDictionary(getString(R.string.txt_choise_religion), "sys_user_religion");
                choiseFlag = CHOISE_RELIGION;
                break;
            case R.id.tv_party://修改党派
                getDictionary(getString(R.string.txt_choise_party), "sys_user_party");
                choiseFlag = CHOISE_PARTY;
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
                        case CHOISE_NATION:
                            mTvNation.setText(mLoopView.getCurrentItem().toString());
                            doEditUserInfo(getUser(UserInfoActivity.this).id, "", "", "", "", "", "", "", dictionaryModels.get(mLoopView.getSelectedItem()).value, "", "");
                            break;
                        case CHOISE_RELIGION:
                            mTvReligion.setText(mLoopView.getCurrentItem().toString());
                            doEditUserInfo(getUser(UserInfoActivity.this).id, "", "", "", "", "", "", "", "", dictionaryModels.get(mLoopView.getSelectedItem()).value, "");
                            break;
                        case CHOISE_PARTY:
                            mTvParty.setText(mLoopView.getCurrentItem().toString());
                            doEditUserInfo(getUser(UserInfoActivity.this).id, "", "", "", "", "", "", "", "", "", dictionaryModels.get(mLoopView.getSelectedItem()).value);
                            break;
                        case CHOISE_OWNER:
                            mTvIsOwner.setText(mLoopView.getCurrentItem().toString());
                            doEditUserInfo(getUser(UserInfoActivity.this).id, "", "", "", "", "", dictionaryModels.get(mLoopView.getSelectedItem()).value, "", "", "", "");
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

    private void showDialog(String title, List<DictionaryModel> data) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            strings.add(data.get(i).label);
        }
        //初始化控件
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.project.community", fileUri);//通过FileProvider创建一个content类型的Uri
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
//                    uploadFile(fileCropUri);//上传图片
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        try {
                            cropImageUri = Uri.fromFile(fileCropUri);
                            Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(this, "com.project.community", new File(newUri.getPath()));
                            }
                            LogUtils.e("cropImageUri>>:"+newUri.toString()+","+cropImageUri.toString());
                            PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                        }catch (Exception e){
                            e.printStackTrace();
                            showToast(getString(R.string.toast_error_photo));
                        }
//                        uploadFile(new File( Uri.parse(PhotoUtils.getPath(this, data.getData())).getPath()));
                    } else {
                        showToast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:

                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        uploadFile(new File(cropImageUri.getPath()));
                        showImages(bitmap);
                    }
                    break;
                case REQUEST_CODE_NICKNAME://修改昵称
                    if (data != null) {
                        mTvNikename.setText(data.getStringExtra("result"));
                    }
                    break;
                case REQUEST_CODE_HOUSENO://修改房屋编号
                    if (data != null) {
                        mTvHouseNo.setText(data.getStringExtra("result"));
                    }
                    break;
                case REQUEST_CODE_POSITION://修改职业
                    if (data != null) {
                        mTvPosition.setText(data.getStringExtra("result"));
                    }
                    break;
            }
        }
    }

    private void showImages(Bitmap bitmap) {
//        mIvHeader.setImageBitmap(bitmap);
        glide.onDisplayImageCircle(this, mIvHeader, bitmap);

    }


    private void loadData() {
        serverDao.getUserInfo(getUser(this).id, new DialogCallback<BaseResponse<UserModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<UserModel> userResponseBaseResponse, Call call, Response response) {
                Gson gson = new Gson();
                String userStr = gson.toJson(userResponseBaseResponse.retData);
                saveUser(UserInfoActivity.this, userStr);
                bindData(userResponseBaseResponse.retData);
            }
        });
    }

    private void bindData(UserModel user) {

        glide.onDisplayImageWithDefault(UserInfoActivity.this, mIvHeader, AppConstants.HOST + user.photo, R.mipmap.d54_tx, true);
        //昵称
        if (!TextUtils.isEmpty(user.loginName)) {
            mTvNikename.setText(user.loginName);
        } else {
            mTvNikename.setText("");
        }

        //姓名
        if (!TextUtils.isEmpty(user.name)) {
            mTvName.setText(user.name);
        } else {
            mTvName.setText("");
        }

        //绑定手机号
        if (!TextUtils.isEmpty(user.phone)) {
            mTvBindMobile.setText(user.phone);
        } else {
            mTvBindMobile.setText("");
        }

        //房屋编号
        if (!TextUtils.isEmpty(user.roomNo)) {
            mTvHouseNo.setText(user.roomNo);
        } else {
            mTvHouseNo.setText("");
        }


        //是否是业主
        if (!TextUtils.isEmpty(user.isOwner)) {
            mTvIsOwner.setText(user.isOwner);
        } else {
            mTvIsOwner.setText("");
        }

        //职业
        if (!TextUtils.isEmpty(user.occupation)) {
            mTvPosition.setText(user.occupation);
        } else {
            mTvPosition.setText("");
        }


        //身份证号
        if (!TextUtils.isEmpty(user.idCard)) {
            mTvIdcard.setText(user.idCard);
            mTvAge.setText(ValidateUtil.getUserAgeByCardId(user.idCard));
            mTvBirthday.setText(ValidateUtil.getUserBrithdayByCardId(user.idCard));
        } else {
            mTvIdcard.setText("");
            mTvAge.setText("");
            mTvBirthday.setText("");
        }

        //民族
        if (!TextUtils.isEmpty(user.nation) && !"0".equals(user.nation)) {
            mTvNation.setText(user.nation);
        } else {
            mTvNation.setText(user.nation);
        }


        //宗教信仰
        if (!TextUtils.isEmpty(user.religion) && !"0".equals(user.religion)) {
            mTvReligion.setText(user.religion);
        } else {
            mTvReligion.setText(user.religion);
        }


        //党派
        if (!TextUtils.isEmpty(user.party) && !"0".equals(user.party)) {
            mTvParty.setText(user.party);
        } else {
            mTvParty.setText(user.party);
        }

    }

    /**
     * 获取字典数据
     *
     * @param type
     */
    private void getDictionary(final String title, final String type) {

        serverDao.getDictionaryData(type, new DialogCallback<BaseResponse<DictionaryResponse>>(this) {
            @Override
            public void onSuccess(BaseResponse<DictionaryResponse> baseResponse, Call call, Response response) {
                dictionaryModels = new ArrayList<>();
                dictionaryModels.addAll(baseResponse.retData.dictList);
                showDialog(title, dictionaryModels);
            }
        });
    }


    /**
     * 修改个人信息
     *
     * @param id         用户id
     * @param photo      用户头像
     * @param loginName  昵称
     * @param name       姓名
     * @param idCard     身份证
     * @param roomNo     房屋编号
     * @param isOwner    是否业主
     * @param occupation 职业
     * @param nation     民族
     * @param religion   宗教信仰
     * @param party      党派
     */
    private void doEditUserInfo(String id, final String photo, String loginName, String name, String idCard,
                                String roomNo, String isOwner, String occupation, String nation,
                                String religion, String party) {
        serverDao.doEditUserInfo(id, photo, loginName, name, idCard, roomNo, isOwner, occupation, nation, religion, party,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                        if (!TextUtils.isEmpty(photo))
                            getUser(UserInfoActivity.this).photo = photo;
                        showToast(baseResponse.message);
                    }
                });
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
                uploadPhoto(baseResponse.retData.filePath);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });
    }

    private void uploadPhoto(String filePath) {
        serverDao.uploadUserPhoto(getUser(this).id, filePath, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });
    }

}

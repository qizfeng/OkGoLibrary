package com.project.community.ui.life.minsheng;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.photo.PhotoUtils;
import com.library.okgo.view.loopview.LoopView;
import com.library.okgo.view.loopview.OnItemSelectedListener;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.ClassifyBaseBean;
import com.project.community.model.FileUploadModel;
import com.project.community.ui.adapter.SendMessageAdapter;
import com.project.community.util.KeyBoardUtil;
import com.project.community.util.ListAdapterUtils;
import com.project.community.util.ScreenUtils;
import com.project.community.util.StringUtils;
import com.project.community.util.ToastUtil;
import com.project.community.view.MyButton;
import com.project.community.view.MyGridView;
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
 * Created by cj on 17/9/27.
 * 发信息页面
 */

public class SendMessageActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private LoopView mLoopView;

    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.send_message_tv_type)
    TextView sendMessageTvType;
    @Bind(R.id.send_message_ll_type)
    RelativeLayout sendMessageLlType;
    @Bind(R.id.send_message_et_title)
    EditText sendMessageEtTitle;
    @Bind(R.id.send_message_tv_title_num)
    TextView sendMessageTvTitleNum;
    @Bind(R.id.send_message_et_content)
    EditText sendMessageEtContent;
    @Bind(R.id.send_message_tv_content_num)
    TextView sendMessageTvContentNum;
    @Bind(R.id.send_message_gv)
    MyGridView sendMessageGv;
    @Bind(R.id.send_message_summit)
    MyButton sendMessageSummit;

    private SendMessageAdapter mApplyStoryPicAdapter;
    private List<String> mImags = new ArrayList<>(); //


    //分类
    private ClassifyBaseBean classifylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        ininData();
    }

    private void ininData() {

        initToolBar(mToolBar, mTvTitle, true, getString(R.string.send_message), R.mipmap.iv_back);


        mApplyStoryPicAdapter = new SendMessageAdapter(mImags, this);
        sendMessageGv.setAdapter(mApplyStoryPicAdapter);
        sendMessageGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        RxView.clicks(sendMessageLlType)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked(sendMessageLlType);
                    }
                });
        RxView.clicks(sendMessageSummit)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked(sendMessageSummit);
                    }
                });
        sendMessageEtContent.setOnTouchListener(this);
        sendMessageEtTitle.setOnTouchListener(this);
        sendMessageEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendMessageTvTitleNum.setText(editable.length() + "/35");
            }
        });
        sendMessageEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendMessageTvContentNum.setText(editable.length() + "/350");
            }
        });
    }

    /**
     * 获取分类列表
     */
    private void getClassify() {
        progressDialog.show();
        serverDao.getClassifyList("livelihood_article_category", new JsonCallback<BaseResponse<ClassifyBaseBean>>() {
            @Override
            public void onSuccess(BaseResponse<ClassifyBaseBean> listBaseResponse, Call call, Response response) {

                progressDialog.dismiss();
                if (listBaseResponse.errNum.equals("0")) {

                    classifylist = listBaseResponse.retData;
                    setSelectClassify();

                } else {
                    ToastUtil.showToast(SendMessageActivity.this, response.message());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();

            }
        });


    }

    private void setSelectClassify() {

        data.clear();


        for (int i = 0; i < classifylist.getDictList().size(); i++) {
            data.add(classifylist.getDictList().get(i).getLabel());
        }
        showDialog(getResources().getString(R.string.send_message_change_type), data);

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
//                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
//                    uploadFile(fileCropUri);//上传图片
                    Log.e("onActivityResult: ", imageUri.toString());
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

    List<String> data = new ArrayList<>();

    @OnClick({R.id.send_message_ll_type, R.id.send_message_summit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_message_ll_type:
                KeyBoardUtil.closeKeybord(this);

                if (classifylist == null)
                    getClassify();
                else
                    showDialog(getResources().getString(R.string.send_message_change_type), data);
//                data.clear();
//                data.add("邻居互动");
//                data.add("供求信息");
//                data.add("二手市场");
//                data.add("房屋出租");
//                showDialog(getResources().getString(R.string.send_message_change_type),data);
                break;
            case R.id.send_message_summit:
                KeyBoardUtil.closeKeybord(this);
                if (TextUtils.isEmpty(sendMessageTvType.getText().toString())) {
                    ToastUtils.showLongToast(this, getResources().getString(R.string.send_message_type_toast));
                    return;
                }
                if (TextUtils.isEmpty(sendMessageEtTitle.getText().toString())) {
                    ToastUtils.showLongToast(this, getResources().getString(R.string.send_message_title_toast));
                    return;
                }
                if (TextUtils.isEmpty(sendMessageEtContent.getText().toString())) {
                    ToastUtils.showLongToast(this, getResources().getString(R.string.send_message_content_toast));
                    return;
                }
                if (mImags.size() < 1) {
                    ToastUtils.showLongToast(this, getResources().getString(R.string.send_message_uoload_img_toast));
                    return;
                }

                save();

//                finish();
                break;
        }
    }


    /**
     * 8
     * 发布帖子
     */
    private void save() {

        //当前没有上传图片
//        if (imgS?ize==0){
        if (mImags.size() == 1)
//            uploadFile(new File(mImags.get(0)));
            uploadFile(new File(StringUtils.getRealFilePath(SendMessageActivity.this, Uri.parse(mImags.get(0)))));

        if (mImags.size() == 2)
            uploadFile(new File(StringUtils.getRealFilePath(SendMessageActivity.this, Uri.parse(mImags.get(1)))));
        if (mImags.size() == 3)
            uploadFile(new File(StringUtils.getRealFilePath(SendMessageActivity.this, Uri.parse(mImags.get(2)))));
//        }


    }

    private List<String> upImage = new ArrayList<String>();

    /**
     * 文件上传
     *
     * @param file
     */
    private void uploadFile(File file) {
        serverDao.uploadFile(file, new JsonCallback<BaseResponse<FileUploadModel>>() {
            @Override
            public void onSuccess(BaseResponse<FileUploadModel> baseResponse, Call call, Response response) {

                upImage.add(baseResponse.retData.filePath);

                if (upImage.size() < mImags.size()) {
                    save();
                } else {

                    releaseSendMessage();

                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());

            }
        });
    }


    /**
     * 8
     * 发布帖子
     */
    private void releaseSendMessage() {

        progressDialog.show();

        //分类
        String categoryId = "";
        for (int i = 0; i < classifylist.getDictList().size(); i++) {
            if (classifylist.getDictList().get(i).getLabel().equals(sendMessageTvType.getText().toString())) {
                categoryId = classifylist.getDictList().get(i).getValue();
            }
        }

        serverDao.releaseSendMessage(getUser(this).id, categoryId,
                sendMessageEtTitle.getText().toString().trim(),
                sendMessageEtContent.getText().toString(),
                ListAdapterUtils.toStr(",", upImage),
                new JsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(BaseResponse<Object> listBaseResponse, Call call, Response response) {

                        progressDialog.dismiss();
                        ToastUtil.showToast(SendMessageActivity.this, response.message());

                        if (listBaseResponse.errNum.equals("0")) {
                            finish();

                        } else {
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        progressDialog.dismiss();
                        Log.e("tag_f", e.getMessage().toString() + "");
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

    private int mCurrtindex = 0;

    private void showDialog(String title, final List<String> strings) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        if (!TextUtils.isEmpty(sendMessageTvType.getText().toString()))
            for (int i = 0; i < strings.size(); i++) {
                if (strings.get(i).equals(sendMessageTvType.getText().toString())) {
                    mCurrtindex = i;
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
                sendMessageTvType.setText(strings.get(mCurrtindex));
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
                mCurrtindex = index;

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
        Log.e("onTouch---->", canVerticalScroll(sendMessageEtContent) + "");
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if (((view.getId() == R.id.send_message_et_content && canVerticalScroll(sendMessageEtContent))) ||
                ((view.getId() == R.id.send_message_et_title && canVerticalScroll(sendMessageEtTitle)))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

}

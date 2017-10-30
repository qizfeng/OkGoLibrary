package com.project.community.ui.me;

import android.Manifest;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.photo.PhotoUtils;
import com.library.okgo.view.loopview.LoopView;
import com.project.community.Event.AddHouseEvent;
import com.project.community.Event.TypeEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.base.BasePopupWindow;
import com.project.community.bean.ClassifyBean;
import com.project.community.bean.RoomList;
import com.project.community.model.FileUploadModel;
import com.project.community.ui.adapter.HomeNumberAdapter;
import com.project.community.ui.adapter.SendMessageAdapter;
import com.project.community.ui.adapter.TypeAdapter;
import com.project.community.util.DataUtilsa;
import com.project.community.util.KeyBoardUtil;
import com.project.community.util.ListAdapterUtils;
import com.project.community.util.ScreenUtils;
import com.project.community.util.StringUtils;
import com.project.community.util.ToastUtil;
import com.project.community.view.MyGridView;
import com.project.community.view.crop.CropImageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rx.Subscription;

/**
 * author：fangkai on 2017/10/25 10:27
 * em：617716355@qq.com
 */
public class ImRepairsActivity extends BaseActivity {


    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.rc_repaors)
    RecyclerView rcRepaors;
    @Bind(R.id.tv_have_add)
    TextView tvHaveAdd;
    @Bind(R.id.ll_have)
    LinearLayout llHave;
    @Bind(R.id.tv_no_add)
    TextView tvNoAdd;
    @Bind(R.id.ll_add_repairs)
    LinearLayout llAddRepairs;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.ll_type)
    LinearLayout llType;
    @Bind(R.id.et_describe)
    EditText etDescribe;
    @Bind(R.id.tv_size)
    TextView tvSize;
    @Bind(R.id.send_message_gv)
    MyGridView sendMessageGv;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_time)
    LinearLayout llTime;
    @Bind(R.id.tv_repairs)
    TextView tvRepairs;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private LoopView mLoopView;


    private SendMessageAdapter mApplyStoryPicAdapter;
    private List<String> mImags = new ArrayList<>(); //


    private HomeNumberAdapter homeNumberAdapter;


    //选择了哪一个
    private List<String> typelist = new ArrayList<>();
    //最终的
    private List<String> typelists = new ArrayList<>();

    private String item = "";

    //房屋信息
    private List<RoomList> mData = new ArrayList<>();

    private List<ClassifyBean.DictListBean> mData2 = new ArrayList<>();


    private BasePopupWindow popupWindow;


    private TypeAdapter typeAdapter;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_repairs);
        ButterKnife.bind(this);
        ininData();
    }

    @OnClick({R.id.tv_have_add, R.id.tv_no_add, R.id.ll_type, R.id.ll_time, R.id.tv_repairs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //提交
            case R.id.tv_repairs:
                submit();
                break;
            case R.id.ll_time:
                showTime();
                break;
            case R.id.ll_type:

                getClassify();
                break;
            case R.id.tv_have_add:
                startActivity(new Intent(this, AddHouseNumberActivity.class));

                break;
            case R.id.tv_no_add:

                startActivity(new Intent(this, AddHouseNumberActivity.class));
                break;
        }
    }

    private void submit() {
        if (item.equals("")) {
            ToastUtil.showToast(this, "请选择房屋编号");
            return;
        }

        String str = "";
        if (typelist.size() > 0)
            for (int i = 0; i < mData2.size(); i++) {
                for (int j = 0; j < typelist.size(); j++) {
                    if (mData2.get(i).getValue().equals(typelist.get(j).toString()))
                        if (str.equals("")) {
                            str = mData2.get(i).getLabel();
                        } else {
                            str = str + "," + mData2.get(i).getLabel();
                        }
                }
            }
        if (str.equals("")) {
            ToastUtil.showToast(this, "请选报修类型");
            return;
        }

        if (TextUtils.isEmpty(etDescribe.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入文字描述");
        }

        if (mImags.size() <= 0) {
            ToastUtil.showToast(this, "请选择图片");
            return;
        }
        if (TextUtils.isEmpty(tvTime.getText().toString().trim())){
            ToastUtil.showToast(this, "请选预约时间");
            return;
        }

        if (mImags.size() == 1)
//            uploadFile(new File(mImags.get(0)));
            uploadFile(new File(StringUtils.getRealFilePath(ImRepairsActivity.this, Uri.parse(mImags.get(0)))));

        if (mImags.size() == 2)
            uploadFile(new File(StringUtils.getRealFilePath(ImRepairsActivity.this, Uri.parse(mImags.get(1)))));
        if (mImags.size() == 3)
            uploadFile(new File(StringUtils.getRealFilePath(ImRepairsActivity.this, Uri.parse(mImags.get(2)))));
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
                    submit();
                } else {
                    propRepairSave();

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
     * 提交报修
     */
    private void propRepairSave() {
        progressDialog.show();
        serverDao.propRepairSave(getUser(this).id, ListAdapterUtils.toStr(",", typelists),
                item, etDescribe.getText().toString().trim(),
                ListAdapterUtils.toStr(",", upImage), tvTime.getText().toString().trim(),
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        progressDialog.dismiss();
                        ToastUtil.showToast(ImRepairsActivity.this,listBaseResponse.message);
                        if (listBaseResponse.errNum.equals("0")) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        progressDialog.dismiss();
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getProRepairRoom();
    }

    /**
     * 获取分类列表
     */
    private void getClassify() {
        KeyBoardUtil.closeKeybord(this);
        if (mData2.size() <= 0) {
            progressDialog.show();
            serverDao.getRoomClassify("prop_repair_type",
                    new JsonCallback<BaseResponse<ClassifyBean>>() {
                        @Override
                        public void onSuccess(BaseResponse<ClassifyBean> listBaseResponse, Call call, Response response) {

                            progressDialog.dismiss();

                            mData2.addAll(listBaseResponse.retData.getDictList());
                            if (mData2.size() > 0) {
                                showPopuWindow();
                            } else {
                                ToastUtil.showToast(ImRepairsActivity.this, listBaseResponse.retMsg);
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Log.e("tag_f", e.getMessage().toString() + "");
                            progressDialog.dismiss();
                        }
                    });

        } else
            showPopuWindow();

    }

    private TimePickerView pvTime;

    private void showTime() {

        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(DataUtilsa.getYear(), DataUtilsa.getMonth(), DataUtilsa.getCurrentDayOfMonth());
//        startDate.set(1900, 12, 28);
        Calendar endDate = Calendar.getInstance();
        endDate.set(3000, 12, 31);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                /*btn_Time.setText(getTime(date));*/
                tvTime.setText(getTime(date));

            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "点", "分", "")
                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
                .setDividerColor(getResources().getColor(R.color.gray_line))
                .setCancelColor(getResources().getColor(R.color.gray_line))
                .setSubmitColor(getResources().getColor(R.color.colorPrimary))
                .setContentSize(21)
                .setSubCalSize(16)
                .setTitleSize(20)
                .setTitleText("选择预约时间")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取房屋编号
     */
    private void getProRepairRoom() {
        progressDialog.show();
        serverDao.getRoomList(getUser(this).id,
                new JsonCallback<BaseResponse<List<RoomList>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<RoomList>> listBaseResponse, Call call, Response response) {

                        progressDialog.dismiss();
                        Log.e("tag_f", listBaseResponse.retData.toString());

                        mData.clear();
                        mData.addAll(listBaseResponse.retData);
//                        homeNumberAdapter.updateItems(mData);
                        homeNumberAdapter.notifyDataSetChanged();
                        if (mData.size() > 0) {
                            llHave.setVisibility(View.VISIBLE);
                            llAddRepairs.setVisibility(View.GONE);
                        } else {
//                            ToastUtil.showToast(ImRepairsActivity.this, listBaseResponse.retMsg);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        progressDialog.dismiss();
                    }
                });


    }

//    private static int[] select;
//
//    //    private Disposable pvOptions_timeObser;
//    private void setTimes() {
//
//        pvOptions_time.show();
//
//        if (subscription == null)
//            //今天
////                            L.e("判断计时器 " + throwable.getMessage());
//            subscription = Observable.interval(500, TimeUnit.MILLISECONDS)
//                    .map(new Func1<Long, Boolean>() {
//                        @Override
//                        public Boolean call(Long aLong) {
//                            if (!pvOptions_time.isShowing()) return false;
//                            select = pvOptions_time.getCurrentItems();
//                            if (pvOptions_time.isShowing()) {
//                                Calendar calendar = Calendar.getInstance();
//                                int hour_of_day = calendar.get(Calendar.HOUR_OF_DAY);
//                                int checkHour = Integer.parseInt(optionsItems2.get(select[1]).replace("时", ""));
//                                int checkMinute = Integer.parseInt(optionsItems3.get(select[2]).replace("分", ""));
//                                int minutes = calendar.get(Calendar.MINUTE);
//                                //今天
//                                if (select[0] == 0) {
//                                    if (checkHour < hour_of_day + 1) {
//                                        select[1] = hour_of_day + 1;
//                                    }
//                                    if (checkHour == hour_of_day + 1 && checkMinute < minutes) {
//                                        select[2] = minutes < 5 ? 1 : (minutes / 5) + 1;
//                                    }
//                                    return true;
//                                }
//                            }
//                            return false;
//                        }
//                    }).subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Boolean>() {
//                        @Override
//                        public void call(Boolean aBoolean) {
//                            if (aBoolean && pvOptions_time.isShowing()) {
//                                pvOptions_time.setSelectOptions(select[0], select[1], select[2]);
//                            }
//                        }
//                    }, new Action1<Throwable>() {
//
//                        @Override
//                        public void call(Throwable throwable) {
////                            L.e("判断计时器 " + throwable.getMessage());
//                        }
//                    });
//
//
//    }


    private void showPopuWindow() {


        if (popupWindow == null) {
            popupWindow = new BasePopupWindow(this);

            View view = View.inflate(this, R.layout.popuwindow_type, null);

            RecyclerView recyclerView = view.findViewById(R.id.rc_data);

            typeAdapter = new TypeAdapter(this, mData2);
            typeAdapter.setThisItem(typelist);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(typeAdapter);


            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    typelists.clear();
                    typelists.addAll(typelist);
                    String str = "";
                    if (typelist.size() > 0)
                        for (int i = 0; i < mData2.size(); i++) {
                            for (int j = 0; j < typelist.size(); j++) {
                                if (mData2.get(i).getValue().equals(typelist.get(j).toString()))
                                    if (str.equals("")) {
                                        str = mData2.get(i).getLabel();
                                    } else {
                                        str = str + "," + mData2.get(i).getLabel();
                                    }
                            }
                        }
                    if (!str.equals(""))
                        tvType.setText(str);
                    popupWindow.dismiss();
                }
            });

            popupWindow.setContentView(view);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            typelist.clear();
            typelist.addAll(typelists);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            typeAdapter.setThisItem(typelists);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AddhouseEvent(AddHouseEvent addhouseEvent) {

//        if (this.item.equals(addhouseEvent.getItem())) {
//            this.item = "";
//        } else
        this.item = addhouseEvent.getItem();

        homeNumberAdapter.setThisItem(item);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TypeEvent(TypeEvent typeEvent) {


        if (typelist.contains(typeEvent.getName())) {
            typelist.remove(typeEvent.getName());
        } else {
            typelist.add(typeEvent.getName());
        }

        if (typeAdapter != null) {
            typeAdapter.setThisItem(typelist);
        }


    }


    private void ininData() {


        EventBus.getDefault().register(this);

        initToolBar(toolbar, tvTitle, true, "我要报修", R.mipmap.iv_back);

        homeNumberAdapter = new HomeNumberAdapter(this, mData);
        rcRepaors.setLayoutManager(new LinearLayoutManager(this));
        rcRepaors.setAdapter(homeNumberAdapter);


        mApplyStoryPicAdapter = new SendMessageAdapter(mImags, this);
        homeNumberAdapter.setThisItem(item);
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


        etDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvSize.setText(editable.length() + "/350");
            }
        });


    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (subscription != null) subscription.unsubscribe();
        super.onDestroy();
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

    private PopupWindow mPopupWindow;


    public void showPhotoDialog() {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_photo, null);
        TextView tv_take_photo = (TextView) inflate.findViewById(R.id.tv_take_photo);
        TextView tv_pick_photo = (TextView) inflate.findViewById(R.id.tv_pick_photo);
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        tv_pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                autoObtainStoragePermission();
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                autoObtainCameraPermission();
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
        mPopupWindow.showAtLocation(etDescribe, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }


}

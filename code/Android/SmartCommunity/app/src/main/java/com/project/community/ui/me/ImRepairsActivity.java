package com.project.community.ui.me;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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

import com.baidu.platform.comapi.map.F;
import com.bigkoo.pickerview.OptionsPickerView;
import com.library.okgo.utils.photo.PhotoUtils;
import com.library.okgo.view.loopview.LoopView;
import com.project.community.Event.AddhouseEvent;
import com.project.community.Event.TypeEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.base.BasePopupWindow;
import com.project.community.ui.MainActivity;
import com.project.community.ui.adapter.HomeNumberAdapter;
import com.project.community.ui.adapter.SendMessageAdapter;
import com.project.community.ui.adapter.TypeAdapter;
import com.project.community.util.KeyBoardUtil;
import com.project.community.util.ScreenUtils;
import com.project.community.view.CustomTimePickerView;
import com.project.community.view.MyGridView;
import com.project.community.view.crop.CropImageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private String item = "";

    //房屋信息
    private List<String> mData = new ArrayList<>();

    private List<String> mData2 = new ArrayList<>();


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

    @OnClick({R.id.tv_have_add, R.id.tv_no_add, R.id.ll_type, R.id.ll_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_time:
                showTime();
                break;
            case R.id.ll_type:
                showPopuWindow();
                break;
            case R.id.tv_have_add:
//                mData.add("s");
//                homeNumberAdapter.setThisItem(item);
//                homeNumberAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_no_add:
                mData.add("12345");
                mData.add("54321");
                homeNumberAdapter.setThisItem(item);
                if (mData.size() > 0) {
                    llHave.setVisibility(View.VISIBLE);
                    llAddRepairs.setVisibility(View.GONE);
                }
                break;
        }
    }

    private ArrayList<String> optionsItems = new ArrayList<>();
    private ArrayList<String> optionsItems2 = new ArrayList<>();
    private ArrayList<String> optionsItems3 = new ArrayList<>();

    private CustomTimePickerView pvOptions_time;

    private void showTime() {

        if (optionsItems.size() <= 0) {
            optionsItems.add("今天");
            optionsItems.add("明天");

            for (int i = 0; i < 24; i++) {
                optionsItems2.add(String.valueOf(i) + "时");
            }
            for (int j = 0; j < 12; j++) {
                optionsItems3.add(String.valueOf(j * 5) + "分");
            }

        }
//        final List<TimeBean> monthListData = timePickerModel.getMonthData();

//        pvOptions_time.setSelectOptions(select[0], select[1], select[2]);
        pvOptions_time = new CustomTimePickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvTime.setText(optionsItems.get(options1) + "  "
                        + optionsItems2.get(options2) + "  "
                        + optionsItems3.get(options3));

//                String year = optionsItems.get(options1).year;
//                String month = optionsItems2.get(options1).month;
//                String dayOfMonth = optionsItems3.get(options1).dayOfMonth;
//                String hour = hours.get(options2).replace("点", "");
//                String minutes = minute.get(options3).replace("分", "");
//                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
//
//                try {
//                    Date parse = dateFormat.parse(year + "年" +
//                            month + "月" +
//                            dayOfMonth + "日 " +
//                            hour + ":" +
//                            minutes);
//                    yukeTime = parse.getTime();
//                    tvTime.setText((month.length() == 1 ? "0" + month : month) + "月" +
//                            (dayOfMonth.length() == 1 ? "0" + dayOfMonth : dayOfMonth) + "日 " +
//                            (hour.length() == 1 ? "0" + hour : hour) + ":" +
//                            (minutes.length() == 1 ? "0" + minutes : minutes));
////                    L.e("yukeTime = " + yukeTime);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }


            }

        })
                .setTitleText("预约时间")
                .setTitleColor(Color.parseColor("#000000"))
                .setSubmitText("确认")
                .setSubCalSize(15)
                .setSubmitColor(Color.parseColor("#103375"))
                .setCancelColor(Color.parseColor("#103375"))
                .setTitleBgColor(Color.WHITE)
                .setLinkage(false)
                .setLayoutRes(R.layout.pickerview_options_time, null)
                .build();
        pvOptions_time.setNPicker(optionsItems, optionsItems2, optionsItems3);
//        pvOptions_time.show();


        setTimes();

    }

    private static int[] select;
//    private Disposable pvOptions_timeObser;
    private void setTimes() {

        pvOptions_time.show();

        if (subscription == null)
        //今天
//                            L.e("判断计时器 " + throwable.getMessage());
        subscription = Observable.interval(500, TimeUnit.MILLISECONDS)
                    .map(new Func1<Long, Boolean>() {
                        @Override
                        public Boolean call(Long aLong) {
                            if (!pvOptions_time.isShowing()) return false;
                            select = pvOptions_time.getCurrentItems();
                            if (pvOptions_time.isShowing()) {
                                Calendar calendar = Calendar.getInstance();
                                int hour_of_day = calendar.get(Calendar.HOUR_OF_DAY);
                                int checkHour = Integer.parseInt(optionsItems2.get(select[1]).replace("时", ""));
                                int checkMinute = Integer.parseInt(optionsItems3.get(select[2]).replace("分", ""));
                                int minutes = calendar.get(Calendar.MINUTE);
                                //今天
                                if (select[0] == 0) {
                                    if (checkHour < hour_of_day + 1) {
                                        select[1] = hour_of_day + 1;
                                    }
                                    if (checkHour == hour_of_day + 1 && checkMinute < minutes) {
                                        select[2] = minutes < 5 ? 1 : (minutes / 5) + 1;
                                    }
                                    return true;
                                }
                            }
                            return false;
                        }
                    }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean && pvOptions_time.isShowing()) {
                                pvOptions_time.setSelectOptions(select[0], select[1], select[2]);
                            }
                        }
                    }, new Action1<Throwable>() {

                        @Override
                        public void call(Throwable throwable) {
//                            L.e("判断计时器 " + throwable.getMessage());
                        }
                    });


    }


    private void showPopuWindow() {

        KeyBoardUtil.closeKeybord(this);
        if (mData2.size() <= 0) {
            mData2.add("水");
            mData2.add("电");
            mData2.add("其他");
        }
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
                String str = "";
                if (typelist.size() > 0)
                    for (int i = 0; i < typelist.size(); i++) {
                        if (i == 0) {
                            str = typelist.get(i);
                        } else {
                            str = str + "," + typelist.get(i);
                        }
                    }
                if (!str.equals(""))
                    tvType.setText(str);
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentView(view);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AddhouseEvent(AddhouseEvent addhouseEvent) {

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
                if (mImags.size() >= 6) {
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
        if(subscription!=null)subscription.unsubscribe();
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
                        if (mImags.size() >= 6) {
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

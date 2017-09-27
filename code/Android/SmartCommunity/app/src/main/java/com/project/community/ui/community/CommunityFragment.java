package com.project.community.ui.community;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.DeviceUtil;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.constants.AppConstants;
import com.project.community.model.CommunityCensusModel;
import com.project.community.model.CommunityDeviceFilterModel;
import com.project.community.model.CommunityModel;
import com.project.community.model.CommunityPersonFilterModel;
import com.project.community.model.CommunityShopFilterModel;
import com.project.community.model.DeviceModel;
import com.project.community.model.DictionaryModel;
import com.project.community.model.FamilyPersonModel;
import com.project.community.model.ShopModel;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/11.
 */

public class CommunityFragment extends BaseFragment implements View.OnClickListener, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private ImageView iv_current_poi;//点击回到当前位置
    /**
     * MapView 是地图主控件
     */
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;

    BitmapDescriptor bdGround;
    BitmapDescriptor bdGround2 = BitmapDescriptorFactory
            .fromResource(R.mipmap.yeats);
    BitmapDescriptor bdE = BitmapDescriptorFactory
            .fromResource(R.mipmap.transparent_bg);
    // 定位相关
    LocationClient mLocClient;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    public MyLocationListenner myListener = new MyLocationListenner();
    private float mCurrentAccracy;


    private MyLocationData locData;
    @Bind(R.id.iv_flash)
    ImageView mIvFlash;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.iv_drawer)
    ImageView mIvDrawer;
    @Bind(R.id.drawerRight)
    RelativeLayout mDrawerRight;
    @Bind(R.id.drawerRightContent)
    LinearLayout mDrawerRightContent;
    @Bind(R.id.drawerBottom)
    LinearLayout mDrawerBottom;
    @Bind(R.id.iv_shop)
    ImageView mIvShop;
    @Bind(R.id.iv_person)
    ImageView mIvPerson;
    @Bind(R.id.iv_device)
    ImageView mIvDevice;
    @Bind(R.id.tv_drawer_cancel)
    TextView mTvDrawerCancel;
    @Bind(R.id.tv_drawer_confirm)
    TextView mTvDrawerConfirm;
    private int currentIndex = 0;//0未选择 1商铺 2人员 3设施
    private List<LatLngBounds> latLngBoundsList = new ArrayList<>();
    private List<CommunityModel> communityModels = new ArrayList<>();
    private List<ShopModel> shopModels = new ArrayList<>();
    private List<FamilyPersonModel> personModels = new ArrayList<>();
    private List<DeviceModel> deviceModels = new ArrayList<>();
    private CommunityShopFilterModel shopFilterModel = new CommunityShopFilterModel();
    private CommunityPersonFilterModel personFilterModel = new CommunityPersonFilterModel();
    private CommunityDeviceFilterModel deviceFilterModel = new CommunityDeviceFilterModel();
    private PopupWindow mPopupWindow;
    private List<BitmapDescriptor> bitmaps = new ArrayList<>();
    DisplayMetrics metric = new DisplayMetrics();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_community, container, false);
        ButterKnife.bind(this, view);
        mMapView = (TextureMapView) view.findViewById(R.id.bmapView);
        iv_current_poi = (ImageView) view.findViewById(R.id.iv_current_poi);
        iv_current_poi.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvDrawer.setOnClickListener(this);
        mIvShop.setOnClickListener(this);
        mIvPerson.setOnClickListener(this);
        mIvDevice.setOnClickListener(this);
        mTvDrawerCancel.setOnClickListener(this);
        mTvDrawerConfirm.setOnClickListener(this);

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        ViewGroup.LayoutParams rightParams = mDrawerRight.getLayoutParams();
        rightParams.width = metric.widthPixels / 2;
        rightParams.height = metric.heightPixels - mDrawerBottom.getLayoutParams().height - 56;
        mDrawerRight.setLayoutParams(rightParams);

        mDrawerBottom.getLayoutParams().width = rightParams.width;
        mDrawerRightContent.getLayoutParams().width = rightParams.width;
        //关闭手动滑出
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //mDrawerLayout.openDrawer(Gravity.RIGHT);//侧滑打开  不设置则不会默认打开
        mIvDrawer.setVisibility(View.GONE);
        try {
            mCurrentLat = Double.parseDouble(getLocation(getActivity())[0]);
            mCurrentLon = Double.parseDouble(getLocation(getActivity())[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void showDrawerLayout() {
        initDrawerData(currentIndex);
        if (!mDrawerLayout.isDrawerOpen(Gravity.END)) {
            mDrawerLayout.openDrawer(Gravity.END);
        } else {
            mDrawerLayout.closeDrawer(Gravity.END);
            if (currentIndex == 1) {
                getShopList();
            } else if (currentIndex == 2) {
                getPersonList();
            } else if (currentIndex == 3) {
                getDeviceList();
            }
        }

    }

    private List<DictionaryModel> scopeList = new ArrayList<>();
    private List<RadioButton> mScopeBtnArr = new ArrayList<>();
    private List<RadioButton> mCheckBtnArr = new ArrayList<>();
    private List<ImageView> mTagBtnArr = new ArrayList<>();
    private List<RadioButton> mDeviceBtnArr = new ArrayList<>();
    private int lastScopeSelect = -1;//范围
    private int lastStatusSelect = -1;//审核状态
    private int lastDeviceSelect = -1;//设备类型
    private String scopeId;//范围id
    private String statusId;//审核状态id
    private String[] tagIdArr = new String[]{};
    private String deviceId;

    private void initDrawerData(int type) {
        mDrawerRightContent.removeAllViews();
        for (int i = 0; i < 2; i++) {
            LinearLayout inflater = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_drawer_right, null);
            if (i == 0) {
                if (currentIndex != 3) {
                    TextView tv_map_title = inflater.findViewById(R.id.tv_map_title);
                    LinearLayout layout_map_content = inflater.findViewById(R.id.layout_map_content);
                    tv_map_title.setText(R.string.txt_map_scope);
                    mScopeBtnArr = new ArrayList<>();
                    for (int j = 0; j < scopeList.size(); j++) {
                        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_map_single, null);
                        RadioButton button = contentView.findViewById(R.id.button);
                        button.setText(scopeList.get(j).label);
                        final Drawable nav_up = getResources().getDrawable(R.mipmap.d10_btn1_p);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        button.setCompoundDrawables(null, null, null, null);
                        button.setTextColor(getResources().getColor(R.color.color_gray_666666));
                        if (lastScopeSelect != -1) {
                            if (lastScopeSelect == j) {
                                button.setCompoundDrawables(null, null, nav_up, null);
                                button.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }

                        mScopeBtnArr.add(button);
                        final int clickPosition = j;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int j = 0; j < mScopeBtnArr.size(); j++) {
                                    mScopeBtnArr.get(j).setCompoundDrawables(null, null, null, null);
                                    mScopeBtnArr.get(j).setTextColor(getResources().getColor(R.color.color_gray_666666));
                                }
                                mScopeBtnArr.get(clickPosition).setCompoundDrawables(null, null, nav_up, null);
                                mScopeBtnArr.get(clickPosition).setTextColor(getResources().getColor(R.color.colorPrimary));
                                lastScopeSelect = clickPosition;
                                scopeId = scopeList.get(clickPosition).value;

                            }
                        });
                        layout_map_content.addView(contentView);
                    }
                    mDrawerRightContent.addView(inflater);
                }
            } else {
                if (type == 1) {//商铺
                    TextView tv_map_title = inflater.findViewById(R.id.tv_map_title);
                    LinearLayout layout_map_content = inflater.findViewById(R.id.layout_map_content);
                    tv_map_title.setText(R.string.txt_map_check_status);
                    mCheckBtnArr = new ArrayList<>();
                    for (int j = 0; j < shopFilterModel.status.size(); j++) {
                        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_map_single, null);
                        RadioButton button = contentView.findViewById(R.id.button);
                        button.setText(shopFilterModel.status.get(j).label);
                        final Drawable nav_up = getResources().getDrawable(R.mipmap.d10_btn1_p);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        button.setCompoundDrawables(null, null, null, null);
                        button.setTextColor(getResources().getColor(R.color.color_gray_666666));
                        if (lastStatusSelect != -1) {
                            if (lastStatusSelect == j) {
                                button.setCompoundDrawables(null, null, nav_up, null);
                                button.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }
                        mCheckBtnArr.add(button);
                        final int clickPosition = j;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int j = 0; j < mCheckBtnArr.size(); j++) {
                                    mCheckBtnArr.get(j).setCompoundDrawables(null, null, null, null);
                                    mCheckBtnArr.get(j).setTextColor(getResources().getColor(R.color.color_gray_666666));
                                }
                                mCheckBtnArr.get(clickPosition).setCompoundDrawables(null, null, nav_up, null);
                                mCheckBtnArr.get(clickPosition).setTextColor(getResources().getColor(R.color.colorPrimary));
                                lastStatusSelect = clickPosition;
                                statusId = shopFilterModel.status.get(clickPosition).value;
                            }
                        });
                        layout_map_content.addView(contentView);
                    }
                    mDrawerRightContent.addView(inflater);
                } else if (type == 2) {//人员
                    TextView tv_map_title = inflater.findViewById(R.id.tv_map_title);
                    LinearLayout layout_map_content = inflater.findViewById(R.id.layout_map_content);
                    tv_map_title.setText(R.string.txt_map_tag);
                    mTagBtnArr = new ArrayList<>();
                    for (int j = 0; j < personFilterModel.tag.size(); j++) {
                        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_map_multiple, null);
                        final ImageView button = contentView.findViewById(R.id.button);
                        TextView tv_area = contentView.findViewById(R.id.tv_area);
                        tv_area.setText(personFilterModel.tag.get(j).label);
                        final Drawable nav_up = getResources().getDrawable(R.mipmap.d10_btn2_p);
                        if (tagIdArr.length > 0) {
                            if (tagIdArr.length == personFilterModel.tag.size()) {
                                for (int index = 0; index < tagIdArr.length; index++) {
                                    button.setImageDrawable(nav_up);
                                }
                            } else {
                                for (int index = 0; index < tagIdArr.length; index++) {
                                    if (j != 0) {
                                        if (personFilterModel.tag.get(j).value.equals(tagIdArr[index])) {
                                            button.setImageDrawable(nav_up);
                                        }
                                    }
                                }
                            }
                        }
                        mTagBtnArr.add(button);

                        final int clickPosition = j;
                        contentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tagIdArr = new String[]{};
                                if (clickPosition == 0) {
                                    if (button.getDrawable() == null) {
                                        for (int i = 0; i < mTagBtnArr.size(); i++) {
                                            mTagBtnArr.get(i).setImageDrawable(nav_up);
                                            tagIdArr[i] = personFilterModel.tag.get(i).value;
                                        }
                                    } else {
                                        for (int i = 0; i < mTagBtnArr.size(); i++) {
                                            mTagBtnArr.get(i).setImageDrawable(null);
                                        }
                                        tagIdArr = new String[]{};
                                    }
                                } else {
                                    if (button.getDrawable() == null) {
                                        int count = 1;
                                        button.setImageDrawable(nav_up);
                                        for (int i = 0; i < mTagBtnArr.size(); i++) {
                                            if (mTagBtnArr.get(i).getDrawable() != null) {
                                                tagIdArr[i] = personFilterModel.tag.get(i).value;
                                                count++;
                                            }
                                        }
                                        if (count == mTagBtnArr.size()) {
                                            mTagBtnArr.get(0).setImageDrawable(nav_up);
                                        }
                                    } else {
                                        mTagBtnArr.get(0).setImageDrawable(null);
                                        button.setImageDrawable(null);
                                        for (int i = 0; i < mTagBtnArr.size(); i++) {
                                            if (mTagBtnArr.get(i).getDrawable() != null) {
                                                tagIdArr[i] = personFilterModel.tag.get(i).value;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        layout_map_content.addView(contentView);
                    }
                    mDrawerRightContent.addView(inflater);

                } else if (type == 3) {//物业设施
                    TextView tv_map_title = inflater.findViewById(R.id.tv_map_title);
                    LinearLayout layout_map_content = inflater.findViewById(R.id.layout_map_content);
                    tv_map_title.setText(R.string.txt_map_utility);
                    mDeviceBtnArr = new ArrayList<>();
                    for (int j = 0; j < deviceFilterModel.type.size(); j++) {
                        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_map_single, null);
                        RadioButton button = contentView.findViewById(R.id.button);
                        button.setText(deviceFilterModel.type.get(j).label);
                        final Drawable nav_up = getResources().getDrawable(R.mipmap.d10_btn1_p);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        button.setCompoundDrawables(null, null, null, null);
                        button.setTextColor(getResources().getColor(R.color.color_gray_666666));
                        if (lastDeviceSelect != -1) {
                            if (lastDeviceSelect == j) {
                                button.setCompoundDrawables(null, null, nav_up, null);
                                button.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }
                        mDeviceBtnArr.add(button);
                        final int clickPosition = j;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int j = 0; j < mDeviceBtnArr.size(); j++) {
                                    mDeviceBtnArr.get(j).setCompoundDrawables(null, null, null, null);
                                    mDeviceBtnArr.get(j).setTextColor(getResources().getColor(R.color.color_gray_666666));
                                }
                                mDeviceBtnArr.get(clickPosition).setCompoundDrawables(null, null, nav_up, null);
                                mDeviceBtnArr.get(clickPosition).setTextColor(getResources().getColor(R.color.colorPrimary));
                                deviceId = deviceFilterModel.type.get(clickPosition).value;
                            }
                        });
                        layout_map_content.addView(contentView);
                    }
                    mDrawerRightContent.addView(inflater);
                }
            }
        }

    }


    @Override
    protected void initData() {
        mBaiduMap = mMapView.getMap();
        // 隐藏logo
        try {
            View child = mMapView.getChildAt(1);
            if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
                child.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBaiduMap.setMapStatus(msu);
        //initOverlay();


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000 * 30);
        mLocClient.setLocOption(option);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
                .fromResource(R.mipmap.d32_icon1),
                accuracyCircleFillColor, accuracyCircleStrokeColor));
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            requestPermissions(requestPermissions, REQUEST_PERMISSION_CODE);
        } else {
            // 有权限了，去放肆吧。
            mLocClient.start();
        }
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mBaiduMap.hideInfoWindow();
            }
        });
        getCommunityData();
    }


    public static final int REQUEST_PERMISSION_CODE = 123;
    public static final int REQUEST_CALL_PERMISSION_CODE = 234;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final String[] requestPermissions = {
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_CALL_PHONE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    if (mLocClient != null)
                        mLocClient.start();
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    ToastUtils.showShortToast(getActivity(), getString(R.string.permission_tips));
                }
                return;
            case REQUEST_CALL_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限，申请权限。
                    ToastUtils.showShortToast(getActivity(), getString(R.string.permission_tips));
                } else {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // 权限被用户同意，可以去放肆了。
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + devicePhone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    break;
                }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_drawer_cancel:
                showDrawerLayout();
                break;
            case R.id.tv_drawer_confirm:
                showDrawerLayout();
                break;
            case R.id.iv_drawer:
                showDrawerLayout();
                break;
            case R.id.iv_current_poi:
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(mCurrentLat, mCurrentLon))
                        .zoom(18)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(u);

                break;
            case R.id.iv_back:
                mIvDrawer.setVisibility(View.GONE);
                mIvBack.setVisibility(View.VISIBLE);
                mIvBack.setImageResource(R.mipmap.d43_shuxin);
                currentIndex = 0;
                mIvShop.setImageResource(R.mipmap.d43_btn_2);
                mIvPerson.setImageResource(R.mipmap.d43_btn_3);
                mIvDevice.setImageResource(R.mipmap.d43_btn_4);
                try {

                    mMapStatus = new MapStatus.Builder()
                            .target(latLngBoundsList.get(0).getCenter())
                            .zoom(17)
                            .build();
                    u = MapStatusUpdateFactory
                            .newMapStatus(mMapStatus);
                    mBaiduMap.setMapStatus(u);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getCommunityData();
                break;
            case R.id.iv_shop:
                if (currentIndex != 1)
                    lastScopeSelect = -1;//范围
                //lastStatusSelect = -1;//审核状态
                lastDeviceSelect = -1;//设备类型

                //scopeId = "";//范围id
                // statusId = "";//审核状态id
                tagIdArr = new String[]{};
                deviceId = "";
                getShopFilter();
                mIvBack.setVisibility(View.VISIBLE);
                mIvBack.setImageResource(R.mipmap.d43_fanhui);
                mIvDrawer.setVisibility(View.VISIBLE);
                mIvShop.setImageResource(R.mipmap.d43_btn_2_p);
                mIvPerson.setImageResource(R.mipmap.d43_btn_3);
                mIvDevice.setImageResource(R.mipmap.d43_btn_4);
                mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(mCurrentLat, mCurrentLon))//39.948961, 116.491081
                        .zoom(12)
                        .build();
                u = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(u);
                currentIndex = 1;
                clearOverlay();
                getShopList();

                break;
            case R.id.iv_person:
                if (currentIndex != 2)
                    lastScopeSelect = -1;//范围
                lastStatusSelect = -1;//审核状态
                lastDeviceSelect = -1;//设备类型

                //scopeId = "";//范围id
                statusId = "";//审核状态id
                //tagId = "";
                deviceId = "";
                getPersonFilter();
                mIvBack.setVisibility(View.VISIBLE);
                mIvBack.setImageResource(R.mipmap.d43_fanhui);
                mIvDrawer.setVisibility(View.VISIBLE);
                mIvShop.setImageResource(R.mipmap.d43_btn_2);
                mIvPerson.setImageResource(R.mipmap.d43_btn_3_p);
                mIvDevice.setImageResource(R.mipmap.d43_btn_4);

                mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(mCurrentLat, mCurrentLon))//116.526366,39.961461
                        .zoom(12)
                        .build();
                u = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(u);
                currentIndex = 2;
                clearOverlay();
                getPersonList();
                break;
            case R.id.iv_device:
                //lastScopeSelect = -1;//范围
                lastStatusSelect = -1;//审核状态
                //lastDeviceSelect = -1;//设备类型

                //scopeId = "";//范围id
                statusId = "";//审核状态id
                tagIdArr = new String[]{};
                //deviceId = "";
                getDeviceFilter();
                mIvBack.setVisibility(View.VISIBLE);
                mIvBack.setImageResource(R.mipmap.d43_fanhui);
                mIvDrawer.setVisibility(View.VISIBLE);
                mIvShop.setImageResource(R.mipmap.d43_btn_2);
                mIvPerson.setImageResource(R.mipmap.d43_btn_3);
                mIvDevice.setImageResource(R.mipmap.d43_btn_4_p);
                mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(mCurrentLat, mCurrentLon))//39.912583, 116.405293
                        .zoom(10)
                        .build();
                u = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(u);
                currentIndex = 3;
                clearOverlay();
                getDeviceList();
                break;
        }
    }

    LatLngBounds bounds;

    private void clearOverlay() {
        try {
            mBaiduMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        InfoWindow.OnInfoWindowClickListener listener = null;
        if (currentIndex == 1) {
            View pop = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_map_shop, null);
            TextView tv_shop = pop.findViewById(R.id.tv_shop);
            final ShopModel shopModel = (ShopModel) marker.getExtraInfo().getSerializable("bundle");
            tv_shop.setText(shopModel.shopsName);
            listener = new InfoWindow.OnInfoWindowClickListener() {
                public void onInfoWindowClick() {
                    getShopDetail(shopModel.id);
                }
            };
            LatLng ll = marker.getPosition();
            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(pop), ll, -110, listener);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } else if (currentIndex == 2) {
            final View pop = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_map_person, null);
            final ImageView iv_header = pop.findViewById(R.id.iv_header);
            final TextView tv_name = pop.findViewById(R.id.tv_name);
            final TextView tv_mobile = pop.findViewById(R.id.tv_mobile);
            final FamilyPersonModel model = (FamilyPersonModel) marker.getExtraInfo().getSerializable("bundle");
            final LatLng ll = marker.getPosition();

            Glide.with(getActivity())
                    .load(AppConstants.HOST + model.photo)
                    .asBitmap()
                    .transform(new CropCircleTransformation(getActivity()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            iv_header.setImageBitmap(resource);
                            tv_name.setText(model.name);
                            tv_mobile.setText(model.phone);
                            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                                public void onInfoWindowClick() {
                                    getPersonDetail(model.id);
                                }
                            };
                            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(pop), ll, -110, listener);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                            dismissDialog();
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            iv_header.setImageResource(R.mipmap.d54_tx);
                            tv_name.setText(model.name);
                            tv_mobile.setText(model.phone);
                            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                                public void onInfoWindowClick() {
                                    getPersonDetail(model.id);
                                }
                            };
                            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(pop), ll, -110, listener);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                            dismissDialog();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            showLoading();
                        }

                    });

        } else if (currentIndex == 3) {
            final View pop = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_map_device, null);
            final ImageView iv_header = pop.findViewById(R.id.iv_header);
            final TextView tv_name = pop.findViewById(R.id.tv_name);
            final TextView tv_mobile = pop.findViewById(R.id.tv_mobile);
            final TextView tv_date = pop.findViewById(R.id.tv_date);
            final DeviceModel model = (DeviceModel) marker.getExtraInfo().getSerializable("bundle");
            final LatLng ll = marker.getPosition();
            Glide.with(getActivity())
                    .load(AppConstants.HOST + model.dutyPhoto)
                    .asBitmap()
                    .transform(new CropCircleTransformation(getActivity()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            iv_header.setImageBitmap(resource);
                            tv_name.setText(model.dutyPeople);
                            tv_mobile.setText(model.dutyPhone);
                            tv_date.setText(getString(R.string.txt_map_shop_install_date) + model.installDate);
                            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                                public void onInfoWindowClick() {
                                    getDeviceDetail(model.id);
                                }
                            };
                            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(pop), ll, -110, listener);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                        }
                    });
        } else {
            CommunityFamilyActivity.startActivity(getActivity(), marker.getExtraInfo());
        }
        return false;
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        double lat = latLng.latitude;//点击位置的维度
        double lon = latLng.longitude;//点击位置的经度
        for (int i = 0; i < latLngBoundsList.size(); i++) {
            //西南坐标
            LatLng southwestLatLng = new LatLng(latLngBoundsList.get(i).southwest.latitude, latLngBoundsList.get(i).southwest.longitude);
            //东北坐标
            LatLng northeastLatLng = new LatLng(latLngBoundsList.get(i).northeast.latitude, latLngBoundsList.get(i).northeast.longitude);
            //点击位置在某区域范围两维度之间
            if (lat >= southwestLatLng.latitude && lat <= northeastLatLng.latitude) {
                //点击位置在某区域范围两经度之间
                if (lon >= southwestLatLng.longitude && lon <= northeastLatLng.longitude) {
                    getCommunityCensusInfo(communityModels.get(i), latLngBoundsList.get(i).getCenter());
                }
            } else {
                mBaiduMap.hideInfoWindow();
            }
        }
    }

    private void clearOverlayMarker() {
        if (latLngBoundsList.size() == 0)
            return;
        LogUtils.e("latLngBoundsList:" + latLngBoundsList.size());
        for (int i = 0; i < latLngBoundsList.size(); i++) {
            List<Marker> markers = mBaiduMap.getMarkersInBounds(latLngBoundsList.get(i));
            try {
                if (markers != null) {
                    if (markers.size() > 0) {
                        for (int j = 0; j < markers.size(); j++) {
                            markers.get(j).remove();
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void initOverlay(List<CommunityModel> models) {
//        clearOverlay();
        mBaiduMap.clear();
        latLngBoundsList = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            String southwestStr = models.get(i).swCoordinate;
            String[] southwestArr;
            LatLng ooGrondSouthwest;

            String northeastStr = models.get(i).neCoordinate;
            String[] northeastArr;
            LatLng ooGroudNortheast;

            if (!TextUtils.isEmpty(southwestStr)) {//西南坐标
                southwestArr = southwestStr.split(",");
                if (southwestArr.length == 0)
                    return;
                ooGrondSouthwest = new LatLng(Double.parseDouble(southwestArr[1]), Double.parseDouble(southwestArr[0]));
                if (!TextUtils.isEmpty(northeastStr)) {//东北坐标
                    northeastArr = northeastStr.split(",");
                    if(northeastArr.length==0)
                        return;
                    ooGroudNortheast = new LatLng(Double.parseDouble(northeastArr[1]), Double.parseDouble(northeastArr[0]));
                    bounds = new LatLngBounds.Builder().include(ooGroudNortheast)
                            .include(ooGrondSouthwest).build();

                    GradientDrawable drawable = (GradientDrawable) getActivity().getResources().getDrawable(R.drawable.bg_dash_border);
//                    drawable.setColor(getResources().getColor(R.color.color_map_community_5c5085c1));
                    try {
                        if (!TextUtils.isEmpty(models.get(i).color) && models.get(i).color.length() == 6)
                            drawable.setColor(Color.parseColor("#5c" + models.get(i).color));
                        else
                            drawable.setColor(getResources().getColor(R.color.color_map_community_5c5085c1));
                    } catch (Exception e) {
                        e.printStackTrace();
                        drawable.setColor(getResources().getColor(R.color.color_map_community_5c5085c1));
                    }
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_bd_map_ground, null);
                    TextView tv_community_no = view.findViewById(R.id.tv_community_no);
                    tv_community_no.setText(models.get(i).orgName);
                    bdGround = BitmapDescriptorFactory
                            .fromView(view);
                    OverlayOptions ooGround = new GroundOverlayOptions()
                            .positionFromBounds(bounds).image(bdGround);
                    mBaiduMap.addOverlay(ooGround);
                    latLngBoundsList.add(bounds);
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(bounds.getCenter())
                            .zoom(17)
                            .build();
                    MapStatusUpdate u = MapStatusUpdateFactory
                            .newMapStatus(mMapStatus);
                    mBaiduMap.setMapStatus(u);
                }

            }
        }
    }


    /**
     * 点击人员marker弹窗信息窗
     */
    private void showPerson(FamilyPersonModel model) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_community_person, null);
        ImageView iv_header = inflate.findViewById(R.id.iv_header);
        TextView tv_name = inflate.findViewById(R.id.tv_name);
        TextView tv_tag1 = inflate.findViewById(R.id.tv_tag1);
        TextView tv_tag2 = inflate.findViewById(R.id.tv_tag2);
        TextView tv_tag3 = inflate.findViewById(R.id.tv_tag3);
        TextView tv_relative = inflate.findViewById(R.id.tv_relative);
        TextView tv_position = inflate.findViewById(R.id.tv_position);
        TextView tv_gender = inflate.findViewById(R.id.tv_gender);
        TextView tv_idcard = inflate.findViewById(R.id.tv_idcard);
        TextView tv_age = inflate.findViewById(R.id.tv_age);
        TextView tv_birthday = inflate.findViewById(R.id.tv_birthday);
        TextView tv_nation = inflate.findViewById(R.id.tv_nation);
        TextView tv_religion = inflate.findViewById(R.id.tv_religion);
        TextView tv_party = inflate.findViewById(R.id.tv_party);
        TextView tv_mobile = inflate.findViewById(R.id.tv_mobile);
        TextView tv_address = inflate.findViewById(R.id.tv_address);
        ImageView iv_tag = inflate.findViewById(R.id.iv_tag);
        glide.onDisplayImage(getActivity(), iv_header, AppConstants.HOST + model.photo);
        Glide.with(getActivity())
                .load(AppConstants.HOST + model.photo)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .placeholder(R.mipmap.d54_tx)
                .into(iv_header);
        tv_name.setText(model.name);
        if (!TextUtils.isEmpty(model.memberTag)) {
            String[] tagArr = model.memberTag.split(",");
            if (tagArr.length == 0) {
                tv_tag1.setVisibility(View.GONE);
                tv_tag2.setVisibility(View.GONE);
                tv_tag3.setVisibility(View.GONE);
            } else if (tagArr.length == 1) {
                tv_tag1.setVisibility(View.VISIBLE);
                tv_tag1.setText(tagArr[0]);
                tv_tag2.setVisibility(View.GONE);
                tv_tag3.setVisibility(View.GONE);
            } else if (tagArr.length == 2) {
                tv_tag1.setVisibility(View.VISIBLE);
                tv_tag1.setText(tagArr[0]);
                tv_tag2.setVisibility(View.VISIBLE);
                tv_tag2.setText(tagArr[1]);
                tv_tag3.setVisibility(View.GONE);
            } else if (tagArr.length == 3) {
                tv_tag1.setVisibility(View.VISIBLE);
                tv_tag1.setText(tagArr[0]);
                tv_tag2.setVisibility(View.VISIBLE);
                tv_tag2.setText(tagArr[1]);
                tv_tag3.setVisibility(View.VISIBLE);
                tv_tag3.setText(tagArr[2]);
            }
        } else {
            tv_tag1.setVisibility(View.GONE);
            tv_tag2.setVisibility(View.GONE);
            tv_tag3.setVisibility(View.GONE);
        }
        if ("业主".equals(model.isOwner)) {
            iv_tag.setVisibility(View.VISIBLE);
        } else {
            iv_tag.setVisibility(View.GONE);
        }


        tv_relative.setText(model.headRelation);
        tv_position.setText(model.occupation);
        if ("1".equals(model.sex))
            tv_gender.setText(getString(R.string.txt_gender_male));
        else if ("2".equals(model.sex))
            tv_gender.setText(getString(R.string.txt_gender_female));
        if (!TextUtils.isEmpty(model.idCard)) {
            tv_idcard.setText(model.idCard);
            tv_age.setText(ValidateUtil.getUserAgeByCardId(model.idCard));
            tv_birthday.setText(ValidateUtil.getUserBrithdayByCardId(model.idCard));
        }
        tv_nation.setText(model.nation);
        tv_religion.setText(model.religion);
        tv_party.setText(model.party);
        tv_mobile.setText(model.phone);
        tv_address.setText(model.roomAddress);
        if (mPopupWindow == null)
            mPopupWindow = new PopupWindow(getActivity());
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(mMapView, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }

    /**
     * 点击商铺marker弹出窗
     */
    private void showShop(final ShopModel model) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_community_shop, null);
        ImageView iv_header = inflate.findViewById(R.id.iv_header);
        TextView tv_shop_name = inflate.findViewById(R.id.tv_shop_name);
        TextView tv_tag1 = inflate.findViewById(R.id.tv_tag1);
        TextView tv_tag2 = inflate.findViewById(R.id.tv_tag2);
        TextView tv_person = inflate.findViewById(R.id.tv_person);
        TextView tv_shop_mobile = inflate.findViewById(R.id.tv_shop_mobile);
        TextView tv_address = inflate.findViewById(R.id.tv_address);
        TextView tv_sort = inflate.findViewById(R.id.tv_sort);
        TextView tv_business = inflate.findViewById(R.id.tv_business);
        TextView tv_company = inflate.findViewById(R.id.tv_company);
        TextView tv_license = inflate.findViewById(R.id.tv_license);
        TextView tv_legal_name = inflate.findViewById(R.id.tv_legal_name);
        ImageView iv_licensePositive = inflate.findViewById(R.id.iv_licensePositive);
        ImageView iv_licenseReverse = inflate.findViewById(R.id.iv_licenseReverse);
        ImageView iv_legalCardPositive = inflate.findViewById(R.id.iv_legalCardPositive);
        ImageView iv_legalCardReverse = inflate.findViewById(R.id.iv_legalCardReverse);
        tv_shop_name.setText(model.shopsName);
        if ("2".equals(model.auditStatus)) {
            tv_tag1.setText(R.string.txt_map_shop_check_pass);
            tv_tag2.setVisibility(View.GONE);
            tv_tag1.setVisibility(View.VISIBLE);
        } else if ("1".equals(model.auditStatus)) {
            tv_tag2.setText(R.string.txt_map_shop_check_ing);
            tv_tag1.setVisibility(View.GONE);
            tv_tag2.setVisibility(View.VISIBLE);
        } else if ("3".equals(model.auditStatus)) {
            tv_tag2.setText(R.string.txt_map_shop_check_nopass);
            tv_tag1.setVisibility(View.GONE);
            tv_tag2.setVisibility(View.VISIBLE);
        }
        tv_person.setText(model.contactName);
        tv_shop_mobile.setText(model.contactPhone);
        tv_address.setText(model.businessAddress);
        tv_sort.setText(model.shopsCategory);
        tv_business.setText(model.mainBusiness);
        tv_company.setText(model.entName);
        tv_license.setText(model.licenseNo);
        tv_legal_name.setText(model.legalPerson);
        glide.onDisplayImageWithDefault(getActivity(), iv_header, AppConstants.HOST + model.shopPhoto, R.mipmap.c1_image2);
        glide.onDisplayImage(getActivity(), iv_licensePositive, AppConstants.HOST + model.licensePositive);
        glide.onDisplayImage(getActivity(), iv_licenseReverse, AppConstants.HOST + model.licenseReverse);
        glide.onDisplayImage(getActivity(), iv_legalCardPositive, AppConstants.HOST + model.legalCardPositive);
        glide.onDisplayImage(getActivity(), iv_legalCardReverse, AppConstants.HOST + model.legalCardReverse);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList imgs = new ArrayList();
                switch (view.getId()) {
                    case R.id.iv_licensePositive:
                        imgs = new ArrayList();
                        imgs.add(AppConstants.HOST + model.licensePositive);
                        ImageBrowseActivity.startActivity(getActivity(), imgs);
                        break;
                    case R.id.iv_licenseReverse:
                        imgs = new ArrayList();
                        imgs.add(AppConstants.HOST + model.licenseReverse);
                        ImageBrowseActivity.startActivity(getActivity(), imgs);
                        break;
                    case R.id.iv_legalCardPositive:
                        imgs = new ArrayList();
                        imgs.add(AppConstants.HOST + model.legalCardPositive);
                        ImageBrowseActivity.startActivity(getActivity(), imgs);
                        break;
                    case R.id.iv_legalCardReverse:
                        imgs = new ArrayList();
                        imgs.add(AppConstants.HOST + model.legalCardReverse);
                        ImageBrowseActivity.startActivity(getActivity(), imgs);
                        break;
                }
            }
        };
        iv_legalCardPositive.setOnClickListener(onClickListener);
        iv_legalCardReverse.setOnClickListener(onClickListener);
        iv_licensePositive.setOnClickListener(onClickListener);
        iv_licenseReverse.setOnClickListener(onClickListener);
        if (mPopupWindow == null)
            mPopupWindow = new PopupWindow(getActivity());
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
//        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight((int)(ScreenUtils.getScreenHeight(getActivity())*0.8));
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
//        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(mMapView, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });

    }

    private String devicePhone = "";

    /**
     * 点击设施marker弹窗
     */
    private void showDevice(final DeviceModel model) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_community_device, null);
        TextView tv_name = inflate.findViewById(R.id.tv_name);
        TextView tv_tag1 = inflate.findViewById(R.id.tv_tag1);
        TextView tv_tag2 = inflate.findViewById(R.id.tv_tag2);
        TextView tv_device_no = inflate.findViewById(R.id.tv_device_no);
        ImageView iv_header = inflate.findViewById(R.id.iv_header);
        TextView tv_person_name = inflate.findViewById(R.id.tv_person_name);
        TextView tv_phone = inflate.findViewById(R.id.tv_phone);
        ImageView iv_cell = inflate.findViewById(R.id.iv_cell);
        TextView tv_time = inflate.findViewById(R.id.tv_time);
        TextView tv_company = inflate.findViewById(R.id.tv_company);
        TextView tv_remark = inflate.findViewById(R.id.tv_remark);
        tv_name.setText(model.facilitiesType);
        if ("1".equals(model.equipmentStatus)) {
            tv_tag1.setText(getString(R.string.txt_map_device_status_normal));
            tv_tag1.setVisibility(View.VISIBLE);
            tv_tag2.setVisibility(View.GONE);
        } else {
            tv_tag2.setText(getString(R.string.txt_map_device_status_unusual));
            tv_tag2.setVisibility(View.VISIBLE);
            tv_tag1.setVisibility(View.GONE);
        }
        tv_device_no.setText(getString(R.string.txt_map_device_no_prefix) + model.facilitiesNo);
        devicePhone = model.dutyPhone;
        Glide.with(getActivity())
                .load(AppConstants.HOST + model.dutyPhoto)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(iv_header);
        tv_person_name.setText(model.dutyPeople);
        tv_phone.setText(model.dutyPhone);
        tv_time.setText(model.scrapDate);
        tv_company.setText(model.ownerCompany);
        tv_remark.setText(model.remarks);

        iv_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.dutyPhone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), PERMISSION_CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 没有权限，申请权限。
                        ActivityCompat.requestPermissions(getActivity(), requestPermissions, REQUEST_PERMISSION_CODE);
                    } else {
                        // 有权限了，去放肆吧。
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        });
        if (mPopupWindow == null)
            mPopupWindow = new PopupWindow(getActivity());
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(mDrawerLayout, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        dismissDialog();
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        super.onResume();
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
    }

    @Override
    protected void onVisible() {
        super.onVisible();

        if (willPlayAnim(getActivity())) {
            final Dialog dialog = new Dialog(getActivity(), R.style.custom_dialog2);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View viewDialog = inflater.inflate(R.layout.layout_community_start_page, null);
            final ImageView imageView = viewDialog.findViewById(R.id.iv_image);
            int width = metric.widthPixels;
            int height = metric.heightPixels;
            //设置dialog的宽高为屏幕的宽高
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
            dialog.setContentView(viewDialog, layoutParams);
            Glide.with(getActivity())
                    .load(AppConstants.HOST + getCommunityStartPage(getActivity()))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                            Animation anim = new AnimationUtils().loadAnimation(getActivity(), R.anim.image_enlarge_anim);
                            anim.setFillAfter(true);//动画执行完毕后停留在最后一帧
                            imageView.startAnimation(anim);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    imageView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (imageView.getAnimation() != null && !imageView.getAnimation().getFillAfter()) {
                                                imageView.clearAnimation();
                                            }
                                            imageView.setAnimation(null);
                                            imageView.setVisibility(View.GONE);
                                            saveWillPlayAnim(getActivity(), false);
                                            dialog.dismiss();
                                            getCommunityData();
                                        }
                                    }, 2000);

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            dismissDialog();
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            imageView.setImageResource(R.mipmap.d42_guohui);
                            Animation anim = new AnimationUtils().loadAnimation(getActivity(), R.anim.image_enlarge_anim);
                            anim.setFillAfter(true);//动画执行完毕后停留在最后一帧
                            imageView.startAnimation(anim);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    imageView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (imageView.getAnimation() != null && !imageView.getAnimation().getFillAfter()) {
                                                imageView.clearAnimation();
                                            }
                                            imageView.setAnimation(null);
                                            imageView.setVisibility(View.GONE);
                                            saveWillPlayAnim(getActivity(), false);
                                            dialog.dismiss();
                                        }
                                    }, 2000);

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            dismissDialog();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            showLoading();
                        }

                    });
        }


    }


    /**
     * 获取小区列表
     */
    private void getCommunityData() {
        serverDao.getCommunityList(getUser(getActivity()).id, getUser(getActivity()).orgCode, new DialogCallback<BaseResponse<List<CommunityModel>>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<List<CommunityModel>> baseResponse, Call call, Response response) {
                communityModels = new ArrayList<>();
                communityModels = baseResponse.retData;
                if (communityModels.size() > 0) {
                    initOverlay(communityModels);
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
     * 小区统计信息
     *
     * @param communityModel
     * @param center
     */
    private void getCommunityCensusInfo(final CommunityModel communityModel, final LatLng center) {
        serverDao.getCommunityCensusInfo(getUser(getActivity()).id, communityModel.orgCode, new DialogCallback<BaseResponse<List<CommunityCensusModel>>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<List<CommunityCensusModel>> baseResponse, Call call, Response response) {
                if (baseResponse.retData.size() > 0) {
                    clearOverlayMarker();
                    for (int i = 0; i < baseResponse.retData.size(); i++) {
                        String coordStr = baseResponse.retData.get(i).coordinate;
                        if (!TextUtils.isEmpty(coordStr)) {
                            String[] coordArr = coordStr.split(",");
                            double eLat = Double.parseDouble(coordArr[1]);
                            double eLong = Double.parseDouble(coordArr[0]);
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_map_community, null);
                            TextView tv_building_no = view.findViewById(R.id.tv_building_no);
                            TextView tv_family_num = view.findViewById(R.id.tv_family_num);
                            TextView tv_person_num = view.findViewById(R.id.tv_person_num);
                            tv_building_no.setText(baseResponse.retData.get(i).floor);
                            tv_family_num.setText(baseResponse.retData.get(i).roomCount);
                            tv_person_num.setText(baseResponse.retData.get(i).memberCount);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                    .fromView(view);
                            Bundle bundle = new Bundle();
                            bundle.putString("floorName", baseResponse.retData.get(i).floor);
                            bundle.putString("floorId", baseResponse.retData.get(i).id);
                            bundle.putString("disName", communityModel.orgName);

                            MarkerOptions ooE = new MarkerOptions()
                                    .position(new LatLng(eLat, eLong))
                                    .icon(bitmapDescriptor)
                                    .zIndex(0)
                                    .extraInfo(bundle)
                                    .period(10);
                            mBaiduMap.addOverlay(ooE);
                            //定位到该区域中心
                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(center)
                                    .zoom(18.5f)
                                    .build();
                            MapStatusUpdate u = MapStatusUpdateFactory
                                    .newMapStatus(mMapStatus);
                            mBaiduMap.setMapStatus(u);
                            mIvBack.setVisibility(View.VISIBLE);
                            mIvBack.setImageResource(R.mipmap.d43_fanhui);
                        }
                    }
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
     * 获取商铺列表
     */
    private void getShopList() {
        String coordinate = mCurrentLon + "," + mCurrentLat;
//        coordinate = "116.491081,39.948961";
        serverDao.getCommunityShopList(getUser(getActivity()).id, coordinate, scopeId, statusId, new DialogCallback<BaseResponse<List<ShopModel>>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<List<ShopModel>> baseResponse, Call call, Response response) {
                mBaiduMap.clear();
                shopModels = new ArrayList<>();
                shopModels = baseResponse.retData;
                addShopMarker(shopModels);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }


    /**
     * 描绘商铺marker
     *
     * @param data
     */
    private void addShopMarker(List<ShopModel> data) {
        BitmapDescriptor bitmapDescriptor;
        mBaiduMap.hideInfoWindow();
        for (int i = 0; i < data.size(); i++) {
            ShopModel model = data.get(i);
            if (!TextUtils.isEmpty(model.coordinate)) {
                String[] coordinateArr = model.coordinate.split(",");
                if (coordinateArr.length == 2) {
                    LatLng latLng = new LatLng(Double.parseDouble(coordinateArr[1]), Double.parseDouble(coordinateArr[0]));
                    MarkerOptions markerOptions = new MarkerOptions();
                    if ("1".equals(model.auditStatus)) {
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.mipmap.d45_icon1);
                        markerOptions.icon(bitmapDescriptor)
                                .position(latLng);
                        bitmaps.add(bitmapDescriptor);
                    } else if ("2".equals(model.auditStatus) || "3".equals(model.auditStatus)) {
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.mipmap.d45_icon2);
                        markerOptions.icon(bitmapDescriptor)
                                .position(latLng);
                        bitmaps.add(bitmapDescriptor);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bundle", model);
                    markerOptions.extraInfo(bundle);
                    mBaiduMap.addOverlay(markerOptions);
                }
            }
        }

    }

    /**
     * 获取商铺筛选条件
     */
    private void getShopFilter() {
        serverDao.getCommunityShopFilter(new JsonCallback<BaseResponse<CommunityShopFilterModel>>() {
            @Override
            public void onSuccess(BaseResponse<CommunityShopFilterModel> baseResponse, Call call, Response response) {
                shopFilterModel = baseResponse.retData;
                scopeList = shopFilterModel.range;
                initDrawerData(1);
            }
        });
    }

    /**
     * 获取商铺详情
     *
     * @param shopId
     */
    private void getShopDetail(String shopId) {
        serverDao.getCommunityShopDetail(getUser(getActivity()).id, shopId, new DialogCallback<BaseResponse<ShopModel>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<ShopModel> baseResponse, Call call, Response response) {
                showShop(baseResponse.retData);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }


    /**
     * 获取人员列表
     */
    private void getPersonList() {
        String coordinate = mCurrentLon + "," + mCurrentLat;
//        coordinate = "116.526366,39.961461";
        serverDao.getCommunityPersonList(getUser(getActivity()).id, coordinate, scopeId, "", new JsonCallback<BaseResponse<List<FamilyPersonModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<FamilyPersonModel>> baseResponse, Call call, Response response) {
                mBaiduMap.clear();
                personModels = new ArrayList<>();
                personModels = baseResponse.retData;
                addPersonMarker(personModels);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }


    /**
     * 描绘居民marker
     */
    private void addPersonMarker(List<FamilyPersonModel> data) {
        BitmapDescriptor bitmapDescriptor;
        mBaiduMap.hideInfoWindow();
        for (int i = 0; i < data.size(); i++) {
            FamilyPersonModel model = data.get(i);
            if (!TextUtils.isEmpty(model.coordinate)) {
                String[] coordinateArr = model.coordinate.split(",");
                if (coordinateArr.length == 2) {
                    LatLng latLng = new LatLng(Double.parseDouble(coordinateArr[1]), Double.parseDouble(coordinateArr[0]));
                    MarkerOptions markerOptions = new MarkerOptions();
                    bitmapDescriptor = BitmapDescriptorFactory
                            .fromResource(R.mipmap.d48_icon1);
                    bitmaps.add(bitmapDescriptor);
                    markerOptions.icon(bitmapDescriptor)
                            .position(latLng);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bundle", model);
                    markerOptions.extraInfo(bundle);
                    mBaiduMap.addOverlay(markerOptions);
                }
            }
        }

    }

    /**
     * 人员详情
     *
     * @param pid
     */
    private void getPersonDetail(String pid) {
        serverDao.getCommunityPersonDetail(getUser(getActivity()).id, pid, new DialogCallback<BaseResponse<FamilyPersonModel>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<FamilyPersonModel> baseResponse, Call call, Response response) {
                showPerson(baseResponse.retData);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 获取人员筛选条件
     */
    private void getPersonFilter() {
        serverDao.getCommunityPersonFilter(new JsonCallback<BaseResponse<CommunityPersonFilterModel>>() {
            @Override
            public void onSuccess(BaseResponse<CommunityPersonFilterModel> baseResponse, Call call, Response response) {
                personFilterModel = baseResponse.retData;
                personFilterModel.tag.add(0, new DictionaryModel("全部"));
                scopeList = personFilterModel.range;
                initDrawerData(2);
            }
        });
    }

    /**
     * 获取设备列表
     */
    private void getDeviceList() {
        String coordinate = mCurrentLon + "," + mCurrentLat;
//        coordinate = "116.405293,39.912583";
        serverDao.getCommunityDeviceList(getUser(getActivity()).id, coordinate, getUser(getActivity()).orgCode, scopeId, deviceId, new JsonCallback<BaseResponse<List<DeviceModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<DeviceModel>> baseResponse, Call call, Response response) {
                mBaiduMap.clear();
                deviceModels = new ArrayList<>();
                deviceModels = baseResponse.retData;
                addDeviceMarker(deviceModels);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 描绘设备marker
     */
    private void addDeviceMarker(List<DeviceModel> data) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.d50_icon1);
        mBaiduMap.hideInfoWindow();
        for (int i = 0; i < data.size(); i++) {
            DeviceModel model = data.get(i);
            if (!TextUtils.isEmpty(model.coordinate)) {
                String[] coordinateArr = model.coordinate.split(",");
                if (coordinateArr.length == 2) {
                    LatLng latLng = new LatLng(Double.parseDouble(coordinateArr[1]), Double.parseDouble(coordinateArr[0]));
                    MarkerOptions markerOptions = new MarkerOptions();
                    if ("1".equals(model.facilitiesType)) {//消防栓
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.mipmap.d50_icon2);
                        bitmaps.add(bitmapDescriptor);
                    } else if ("2".equals(model.facilitiesType)) {//摄像头
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.mipmap.d50_icon1);
                        bitmaps.add(bitmapDescriptor);
                    } else if ("3".equals(model.facilitiesType)) {//电子门禁
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.mipmap.d50_icon3);
                        bitmaps.add(bitmapDescriptor);
                    } else {
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.mipmap.d50_icon1);
                        bitmaps.add(bitmapDescriptor);
                    }

                    markerOptions.icon(bitmapDescriptor)
                            .position(latLng);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bundle", model);
                    markerOptions.extraInfo(bundle);
                    mBaiduMap.addOverlay(markerOptions);
                }
            }
        }
    }

    /**
     * 获取设备筛选条件
     */
    private void getDeviceFilter() {
        serverDao.getCommunityDeviceFilter(new JsonCallback<BaseResponse<CommunityDeviceFilterModel>>() {
            @Override
            public void onSuccess(BaseResponse<CommunityDeviceFilterModel> baseResponse, Call call, Response response) {
                deviceFilterModel = baseResponse.retData;
                initDrawerData(3);
            }
        });
    }


    /**
     * 物业设施详情
     *
     * @param deviceId
     */
    private void getDeviceDetail(String deviceId) {
        serverDao.getCommunityDeviceDetail(getUser(getActivity()).id, deviceId, new DialogCallback<BaseResponse<DeviceModel>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<DeviceModel> baseResponse, Call call, Response response) {
                showDevice(baseResponse.retData);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
            // 退出时销毁定位
            if (mLocClient != null)
                mLocClient.stop();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mMapView.onDestroy();
            mMapView = null;
            // 回收 bitmap 资源
            for (int i = 0; i < bitmaps.size(); i++) {
                bitmaps.get(i).recycle();
            }
            bdGround.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            LogUtils.e("location:" + location.getLatitude() + "," + location.getLongitude());
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .accuracy(0)
                    .direction(mCurrentDirection)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
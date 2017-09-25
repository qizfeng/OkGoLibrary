package com.project.community.ui.life.minsheng;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.DeviceModel;
import com.project.community.ui.community.CommunityFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TransportationBaiduMapActivity extends BaseActivity implements BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.bmapView)
    TextureMapView mMapView;
    @Bind(R.id.head_name)
    TextView headName;
    @Bind(R.id.head_banci)
    TextView headBanci;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    /**
     * MapView 是地图主控件
     */
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;

    // 定位相关
    private MyLocationData locData;
    LocationClient mLocClient;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    public MyLocationListenner myListener = new MyLocationListenner();
    private float mCurrentAccracy;

    private List<LatLngBounds> latLngBoundsList = new ArrayList<>();
    private List<BitmapDescriptor> bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_baidu_map);
        ButterKnife.bind(this);
        initData();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, "1路", R.mipmap.iv_back);
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
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000 * 15);
        mLocClient.setLocOption(option);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
                .fromResource(R.mipmap.d32_icon1),
                accuracyCircleFillColor, accuracyCircleStrokeColor));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            requestPermissions(requestPermissions, REQUEST_PERMISSION_CODE);
        } else {
            // 有权限了，去放肆吧。
            mLocClient.start();
        }
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        List<DeviceModel> mDeviceModels =new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            DeviceModel deviceModel =new DeviceModel();
            mDeviceModels.add(deviceModel);
        }
        addDeviceMarker(mDeviceModels);
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
                    ToastUtils.showShortToast(this, getString(R.string.permission_tips));
                }
                break;

        }
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
//                    getCommunityCensusInfo(communityModels.get(i), latLngBoundsList.get(i).getCenter());
                }
            }
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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
            // LogUtils.e("location:"+location.getLatitude()+","+location.getLongitude());
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

    @Override
    protected void onDestroy() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    } else if ("3".equals(model.facilitiesType)) {
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
}

package com.project.community.ui.life.minsheng;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.DeviceModel;
import com.project.community.ui.community.CommunityFragment;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.ui.life.wuye.AddHouseNoActivity;
import com.project.community.ui.me.shop_management.ShopDataActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cj on 17/9/28.
 * 地址页面
 */

public class AdrressActivity extends BaseActivity implements BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {

    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.bmapView)
    TextureMapView mMapView;
    @Bind(R.id.iv_current_poi)
    ImageView ivCurrentPoi;
    @Bind(R.id.imgs_datouzheng)
    ImageView imgs_datouzheng;

    private String mCurrentAddress="";
    /**
     * MapView 是地图主控件
     */
    private BaiduMap mBaiduMap;
    // 定位相关
    private MyLocationData locData;
    LocationClient mLocClient;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    public MyLocationListenner myListener = new MyLocationListenner();
    private double mLongitude; // 经度
    private double mLatitude; //纬度

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AdrressActivity.class);
        context.startActivity(intent);
    }
    public static void startActivity(Context context,String longitude,String latitude) {
        Intent intent = new Intent(context, AdrressActivity.class);
        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adrress);
        ButterKnife.bind(this);
        inintData();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void inintData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.address), R.mipmap.iv_back);
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
        mMapView.showZoomControls(false);
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
        option.setScanSpan(1000 * 30);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        if (getIntent().getExtras()!=null){
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
                    .fromResource(R.mipmap.d32_icon1),
                    accuracyCircleFillColor, accuracyCircleStrokeColor));
        }else {
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
                    .fromResource(R.mipmap.k),
                    accuracyCircleFillColor, accuracyCircleStrokeColor));
        }

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

        if (getIntent().getExtras()!=null){
            mLongitude= Double.parseDouble(getIntent().getStringExtra("longitude"));
            mLatitude= Double.parseDouble(getIntent().getStringExtra("latitude"));
            imgs_datouzheng.setVisibility(View.GONE);
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(new LatLng(mLatitude, mLongitude))
                    .zoom(18)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(u);

            addDeviceMarker(mLatitude,mLongitude);

        }else {
            mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                @Override
                public void onMapStatusChangeStart(MapStatus mapStatus) {

                }

                @Override
                public void onMapStatusChange(MapStatus mapStatus) {

                }

                @Override
                public void onMapStatusChangeFinish(MapStatus mapStatus) {
                    //target地图操作的中心点。
                    LatLng target = mBaiduMap.getMapStatus().target;
                    Log.e("onMapStatusChangeFinish", target.toString());
                    mLongitude= target.longitude;
                    mLatitude= target.latitude;
                    latlngToAddress(new LatLng(target.latitude,target.longitude));
//                mBaiduMap.hideInfoWindow();
                }
            });
        }

    }

    public static final int REQUEST_PERMISSION_CODE = 123;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final String[] requestPermissions = {
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION
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



    @OnClick(R.id.iv_current_poi)
    public void onViewClicked() {
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(new LatLng(mCurrentLat, mCurrentLon))
                .zoom(18)
                .build();
        MapStatusUpdate u = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(u);
    }
    private List<LatLngBounds> latLngBoundsList = new ArrayList<>();
    @Override
    public void onMapClick(LatLng latLng) {
        Log.e( "onMapClick__latLng", latLng.toString());

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        Log.e( "onMapClick__mapPoi", mapPoi.getName());

        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e( "onMapClick__marker", marker.getTitle());
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
            LogUtils.e("location____:" + location.getLatitude() + "," + location.getLongitude()+","+location.getAddrStr());
            mLongitude= location.getLatitude();
            mLatitude= location.getLatitude();
            mCurrentAddress=location.getAddrStr();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 通过经纬度获取地址
     *
     * @param point
     * @return
     */
    GeoCoder geoCoder = GeoCoder.newInstance();
    private void latlngToAddress(LatLng latlng) {
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
        //设置地址或经纬度反编译后的监听,这里有两个回调方法,
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            //经纬度转换成地址
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null ||  result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(AdrressActivity.this, "找不到该地址!",Toast.LENGTH_SHORT).show();
                }
//                tv_address.setText("地址:" + result.getAddress());
                Log.e("onGetReverseGeoCodeRe", "地址:"+ result.getAddress());
                mCurrentAddress=result.getAddress();

            }

        //把地址转换成经纬度
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                // 详细地址转换在经纬度
                String address=result.getAddress();
            }
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confire, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confire:
                if (getIntent().getExtras()==null){
                    Intent intent = new Intent();
                    intent.putExtra("result",mCurrentAddress);
                    intent.putExtra("latitude",mLatitude);
                    intent.putExtra("longitude",mLongitude);
                    setResult(100,intent);
                    finish();
                }else {
                    finish();
                }

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * 描绘设备marker
     */
    private void addDeviceMarker(double lat,double lon) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.d32_icon1);
        mBaiduMap.hideInfoWindow();
        LatLng latLng = new LatLng(lat,lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(bitmapDescriptor)
                .position(latLng);
        mBaiduMap.addOverlay(markerOptions);
    }

}

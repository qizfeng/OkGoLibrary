package com.project.community.ui.community;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.ui.MainActivity;
import com.project.community.ui.SplashActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/7/11.
 */

public class CommunityFragment extends BaseFragment implements View.OnClickListener {
    private ImageView iv_current_poi;//点击回到当前位置
    /**
     * MapView 是地图主控件
     */
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private Marker mMarkerE;
    private InfoWindow mInfoWindow;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_marka);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_markb);
    BitmapDescriptor bdC = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_markc);
    BitmapDescriptor bdD = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_markd);
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_gcoding);
    BitmapDescriptor bdGround = BitmapDescriptorFactory
            .fromResource(R.mipmap.ground_overlay);
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


    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_community, container, false);
        mMapView =(TextureMapView) view.findViewById(R.id.bmapView);
        iv_current_poi = (ImageView) view.findViewById(R.id.iv_current_poi);
        return view;
    }

    @Override
    protected void initData() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBaiduMap.setMapStatus(msu);
        initOverlay();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getActivity());
                button.setBackgroundResource(R.mipmap.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA || marker == mMarkerD) {
                    button.setText("更改位置");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            LatLng ll = marker.getPosition();
                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                                    ll.longitude + 0.005);
                            marker.setPosition(llNew);
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerB) {
                    button.setText("更改图标");
                    button.setTextColor(Color.BLACK);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.setIcon(bd);
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerC) {
                    button.setText("删除");
                    button.setTextColor(Color.BLACK);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

        iv_current_poi.setOnClickListener(this);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            requestPermissions(requestPermissions, REQUEST_PERMISSION_CODE);
        } else {
            // 有权限了，去放肆吧。
            mLocClient.start();
        }
    }


    public static final int REQUEST_PERMISSION_CODE = 123;

    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final String[] requestPermissions = {
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
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
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_current_poi:
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(mCurrentLat, mCurrentLon))
                        .zoom(18)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(u);
                break;
        }
    }

    public void initOverlay() {
        // add marker overlay
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        // 掉下动画
        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(5);
        // 掉下动画
        ooB.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
        // 生长动画
        ooC.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        MarkerOptions ooD = new MarkerOptions().position(llD).icons(giflist)
                .zIndex(0).period(10);
        // 生长动画
        ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

        // add ground overlay
        final LatLng ooGrond1Southwest = new LatLng(39.92235, 116.380338);
        final LatLng ooGroud1Northeast = new LatLng(39.927246, 116.384977);
        LatLngBounds bounds = new LatLngBounds.Builder().include(ooGroud1Northeast)
                .include(ooGrond1Southwest).build();
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.5f);
        mBaiduMap.addOverlay(ooGround);

        //add ground overlay2
        LatLng ooGround2Southwest = new LatLng(39.935029, 116.383877);
        LatLng ooGround2Northeast = new LatLng(39.939577, 116.389331);
        LatLngBounds bounds2 = new LatLngBounds.Builder().include(ooGround2Northeast).include(ooGround2Southwest).build();
        OverlayOptions ooGround2 = new GroundOverlayOptions()
                .positionFromBounds(bounds2).image(bdGround2).transparency(0.5f);
        mBaiduMap.addOverlay(ooGround2);

        final List<LatLngBounds> latLngBoundsList = new ArrayList<>();
        latLngBoundsList.add(bounds);
        latLngBoundsList.add(bounds2);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
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
                            //测试MarkerE
                            double eLat = (northeastLatLng.latitude - southwestLatLng.latitude) / 2.0 + southwestLatLng.latitude;
                            double eLong = (northeastLatLng.longitude - southwestLatLng.longitude) / 2.0 + southwestLatLng.longitude;
                            MarkerOptions ooE = new MarkerOptions().position(new LatLng(eLat, eLong)).icon(bdA).zIndex(0).period(10);
                            mMarkerE = (Marker) (mBaiduMap.addOverlay(ooE));

                            //测试MarkerF
                            double fLat = (northeastLatLng.latitude - southwestLatLng.latitude) / 3.0 + southwestLatLng.latitude;
                            double fLong = (northeastLatLng.longitude - southwestLatLng.longitude) / 2.0 + southwestLatLng.longitude;
                            MarkerOptions ooF = new MarkerOptions().position(new LatLng(fLat, fLong)).icon(bdB).zIndex(0).period(10);
                            mMarkerE = (Marker) (mBaiduMap.addOverlay(ooF));

                            //测试MarkerG
                            double gLat = (northeastLatLng.latitude - southwestLatLng.latitude) / 2.0 + southwestLatLng.latitude;
                            double gLong = (northeastLatLng.longitude - southwestLatLng.longitude) / 3.0 + southwestLatLng.longitude;
                            MarkerOptions ooG = new MarkerOptions().position(new LatLng(gLat, gLong)).icon(bdC).zIndex(0).period(10);
                            mMarkerE = (Marker) (mBaiduMap.addOverlay(ooG));

                            //定位到该区域中心
                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(latLngBoundsList.get(i).getCenter())
                                    .zoom(18)
                                    .build();
                            MapStatusUpdate u = MapStatusUpdateFactory
                                    .newMapStatus(mMapStatus);
                            mBaiduMap.setMapStatus(u);
                        }
                    }
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(marker.getPosition())
                        .zoom(20)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(u);
                return false;
            }
        });


        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                ToastUtils.showShortToast(getActivity(), "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                        + marker.getPosition().longitude);
            }

            public void onMarkerDragStart(Marker marker) {
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
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
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
            bdA.recycle();
            bdB.recycle();
            bdC.recycle();
            bdD.recycle();
            bd.recycle();
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
           // LogUtils.e("onReceiveLocation lat:" + location.getLatitude() + ",long:" + location.getLongitude());
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(16.0f);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}



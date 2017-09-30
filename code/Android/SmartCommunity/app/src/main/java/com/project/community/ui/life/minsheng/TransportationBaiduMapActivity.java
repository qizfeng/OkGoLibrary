package com.project.community.ui.life.minsheng;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.life.minsheng.overlayutil.BusLineOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.project.community.R.id.linearLayout;

public class TransportationBaiduMapActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener,
        BaiduMap.OnMapClickListener {

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

    private int nodeIndex = -2;// 节点索引,供浏览节点时使用
    /**
     * 公共交通信息查询结果
     * */
    private BusLineResult route = null;// 保存驾车/步行路线数据的变量，供浏览节点时使用

    private List<String> busLineIDList = null;
    private int busLineIndex = 0;

    /**
     * MapView 是地图主控件
     */
    private BaiduMap mBaiduMap;
    private BusLineSearch mBusLineSearch = null;
    /**
     * 用于显示一条公交详情结果的Overlay
     * */
    BusLineOverlay overlay;//公交路线绘制对象


    PoiSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_baidu_map);
        ButterKnife.bind(this);
        initData();

    }

    private void initData2() {
        /**
         * public final void setOnMapClickListener(BaiduMap.OnMapClickListener listener)
         * 设置地图单击事件监听者
         * @param listener - 地图单击事件监听者
         * 需要实现BaiduMap.OnMapClickListener接口的
         * onMapClick(LatLng point)和onMapPoiClick(MapPoi poi)方法
         * */
        mBaiduMap.setOnMapClickListener(this);

        /**
         * public static PoiSearch newInstance()
         * 创建PoiSearch实例
         * @return PoiSearch实例
         * */
        mSearch = PoiSearch.newInstance();
        /**
         * public void setOnGetPoiSearchResultListener(OnGetPoiSearchResultListener listener)
         * 设置poi检索监听者
         * @param listener - 监听者
         * 需要实现onGetPoiDetailResult(PoiDetailResult result)和 onGetPoiResult(PoiResult result)方法
         * */
        mSearch.setOnGetPoiSearchResultListener(this);

        /**
         * public static BusLineSearch newInstance()
         * 获取一个新的检索实例
         * @param 检索实例
         * */
        mBusLineSearch = BusLineSearch.newInstance();

        /**
         * public void setOnGetBusLineSearchResultListener(OnGetBusLineSearchResultListener listener)
         * 设置公交详情检索结果监听者
         * @param listener - 公交详情检索结果监听者
         *
         * 需要实现OnGetBusLineSearchResultListener接口的 onGetBusLineResult方法
         * */
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);

        busLineIDList = new ArrayList<String>();

        /**
         * public BusLineOverlay(BaiduMap baiduMap)
         * 构造函数
         * @param baiduMap - 该BusLineOverlay所引用的 BaiduMap 对象
         * */
        overlay = new BusLineOverlay(mBaiduMap);

        /**
         * 设置地图 Marker 覆盖物点击事件监听者
         * */
        mBaiduMap.setOnMarkerClickListener(overlay);

        startSearch("成都","118");



    }

    private void startSearch(String City,String line){
        busLineIDList.clear();
        busLineIndex = 0;
        //发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
        /**
         * public boolean searchInCity(PoiCitySearchOption option)
         * 城市内检索
         * @param option - 请求参数
         * @return 成功发起检索返回true , 失败返回false
         *
         * public PoiCitySearchOption city(java.lang.String city)
         * 检索城市
         * @param city - 检索城市
         * @return 该poi城市内检索参数对象
         *
         * public PoiCitySearchOption keyword(java.lang.String key)
         * 搜索关键字
         * @param key - 搜索关键字
         * @return 该poi城市内检索参数对象
         * */
        mSearch.searchInCity((new PoiCitySearchOption())
                .city(City)
                .keyword(line));
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, "1路", R.mipmap.iv_back);
        mMapView.showZoomControls(false);
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
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        initData2();
//        mBaiduMap.setMyLocationEnabled(true);
//        mBaiduMap.getUiSettings().setCompassEnabled(true);
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
//        mBaiduMap.setMapStatus(msu);
//        //initOverlay();
//
//
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000 * 15);
//        mLocClient.setLocOption(option);
//        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory
//                .fromResource(R.mipmap.d32_icon1),
//                accuracyCircleFillColor, accuracyCircleStrokeColor));
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 没有权限，申请权限。
//            requestPermissions(requestPermissions, REQUEST_PERMISSION_CODE);
//        } else {
//            // 有权限了，去放肆吧。
//            mLocClient.start();
//        }
//        mBaiduMap.setOnMapClickListener(this);
//        mBaiduMap.setOnMarkerClickListener(this);
//        List<DeviceModel> mDeviceModels =new ArrayList<>();
//        for (int i = 0; i < 5 ; i++) {
//            DeviceModel deviceModel =new DeviceModel();
//            mDeviceModels.add(deviceModel);
//        }
//        addDeviceMarker(mDeviceModels);
    }


    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }


    @Override
    protected void onDestroy() {
        /**
         * 释放检索对象
         * */
        mSearch.destroy();
        /**
         * 释放检索对象资源
         * */
        mBusLineSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(TransportationBaiduMapActivity.this, "未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            // 遍历所有poi，找到类型为公交线路的poi
            busLineIDList.clear();

            for (PoiInfo poi : result.getAllPoi()) {
                Log.e("onGetPoiResult: ", poi.name);
                /**
                 * public PoiInfo.POITYPE type
                 * poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路,
                 * */
                Log.e("onGetPoiResult: ", "----"+poi.type);
                if (poi.type == PoiInfo.POITYPE.BUS_LINE
                        || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {

                    /**
                     * poi id 如果为isPano为true，可用此参数调用街景组件PanoramaService类的
                     * requestPanoramaWithPoiUId方法检索街景数据
                     * */
                    Log.e("onGetPoiResult: ","----"+ poi.uid);
                    busLineIDList.add(poi.uid);
                }
            }
            searchBusline();
        }

        route = null;
    }

    private void searchBusline() {

        //公交的站点大于，该路线的长度。
        if (busLineIndex >= busLineIDList.size()) {
            busLineIndex = 0;
        }
        //公交站点 小于这个路线的长度
        if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
                && busLineIDList.size() > 0) {
            //定义并设置公交信息结果监听者（与POI类似），并发起公交详情检索；
            //如下代码为发起检索代码，定义监听者和设置监听器的方法与POI中的类似
            //参数city和这个keyword路线的UID
            // 公交检索入口
            Log.e("searchBusline: ", "---"+busLineIDList.get(busLineIndex));
            boolean flag = mBusLineSearch
                    .searchBusLine((new BusLineSearchOption().city("成都")
                            .uid(busLineIDList.get(busLineIndex))));
            //是否成功检索
            if (flag) {
                //把布局隐藏
//                linearLayout.setVisibility(View.GONE);
                Toast.makeText(TransportationBaiduMapActivity.this, "检索成功~", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(TransportationBaiduMapActivity.this, "检索失败~", Toast.LENGTH_LONG)
                        .show();
            }
            busLineIndex++;
        }
    }

    /**
     * 下一条按钮点击事件
     * */
    public void SearchNextBusline(View v) {
        if (busLineIndex >= busLineIDList.size()) {
            busLineIndex = 0;
        }
        if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
                && busLineIDList.size() > 0) {
            /**
             * public boolean searchBusLine(BusLineSearchOption option)
             * 公交检索入口
             * @param option - 请求参数
             * @return 成功发起检索返回true , 失败返回false*
             * */

            /**
             * BusLineSearchOption:城市公交信息查询参数
             *
             * public BusLineSearchOption city(java.lang.String city)
             * 设置查询城市
             * @param city - 查询城市
             * @return 该公交信息查询参数对象
             *
             * public BusLineSearchOption uid(java.lang.String uid)
             * 设置公交路线uid.
             * @param uid - uid 可以由poi检索时传入公交路线关键字得到
             * @return 该公交信息查询参数对象
             * */
            mBusLineSearch.searchBusLine((new BusLineSearchOption()
                    .city("北京")
                    .uid(busLineIDList.get(busLineIndex))));
            busLineIndex++;
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    @Override
    public void onGetBusLineResult(BusLineResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TransportationBaiduMapActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();
        route = result;
        nodeIndex = -1;

        /**
         * OverlayManager:
         * removeFromMap():将所有Overlay从 地图上消除
         * */
        overlay.removeFromMap();

        /**
         * public void setData(BusLineResult result)
         * 设置公交线数据
         * @param result - 公交线路结果数据
         * */
        overlay.setData(result);

        /**
         * public final void addToMap()
         * 将所有Overlay 添加到地图上
         * */
        overlay.addToMap();

        /**
         * public void zoomToSpan()
         * 缩放地图，使所有Overlay都在合适的视野内
         * 注： 该方法只对Marker类型的overlay有效
         * */
        overlay.zoomToSpan();

        Toast.makeText(TransportationBaiduMapActivity.this, result.getBusLineName(),
                Toast.LENGTH_SHORT).show();
    }
}

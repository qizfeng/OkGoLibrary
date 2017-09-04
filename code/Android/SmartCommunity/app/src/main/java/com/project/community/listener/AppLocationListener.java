package com.project.community.listener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.library.okgo.utils.LogUtils;
import com.project.community.callback.ServerDao;
import com.project.community.callback.ServerDaoImpl;
import com.project.community.constants.AppConstants;

/**
 * Created by qizfeng on 17/7/26.
 */

public class AppLocationListener implements BDLocationListener {

    /**
     * 经纬度
     */
    double longitude;

    double latitude;

    Handler mHandler;

    public AppLocationListener(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        LogUtils.e("onReceiveLocation lat:" + location.getLatitude() + ",long:" + location.getLongitude());
        /*
         * location.getLocType()的返回值含义： 61 ： GPS定位结果 62 ： 扫描整合定位依据失败。此时定位结果无效。
		 * 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。 65 ： 定位缓存的结果。 66 ：
		 * 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果 67 ：
		 * 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果 68 ：
		 * 网络连接失败时，查找本地离线定位时对应的返回结果 161： 表示网络定位结果 162~167： 服务端定位失败。
		 */
        int locType = location.getLocType();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        // TODO 调试使用
        StringBuffer sb = new StringBuffer(256);
        sb.append("当前时间:");
        sb.append(location.getTime());
        sb.append("\n错误码:");
        sb.append(location.getLocType());
        sb.append("\n纬度");
        sb.append(location.getLatitude());
        sb.append("\n经度");
        sb.append(location.getLongitude());
        sb.append("\n半径:");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\n速度 : ");
            sb.append(location.getSpeed());
            sb.append("\n卫星数 : ");
            sb.append(location.getSatelliteNumber());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\n地址 : ");
            sb.append(location.getAddrStr());
        }
        // LogUtil.i("BDLocationListene " + sb.toString());
        if (locType == AppConstants.LOCATION_GPS
                || locType == AppConstants.LOCATION_NETWORK) {
            // GPS定位结果、网络定位结果
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    String userId = "dlc";
                    // 发送给服务器
                    int result = send(userId, longitude, latitude);
                    Message msg = new Message();
                    msg.what = result;
                    mHandler.sendMessage(msg);
                }
            });
            // 发送给本地Service
            sendMsg(sb.toString());
        } else if (locType == AppConstants.LOCATION_NETWORK_EXCEPTION
                || locType == AppConstants.LOCATION_NETWORK_CONNECT_FAIL) {
            // 网络异常，没有成功向服务器发起请求。
            Message msg = new Message();
            msg.what = locType;
            mHandler.sendMessage(msg);
        }
    }

    private void sendMsg(String sb) {
        Message msg = mHandler.obtainMessage();
        // Bundle是message中的数据
        Bundle b = new Bundle();
        msg.what = AppConstants.SEND_LOCATION_TO_SERVICE;// what表示正常发送数据
        // 设置数据
        b.putString("LocationData", sb);
        msg.setData(b);
        // 传消息给主线程
        mHandler.sendMessage(msg);
    }

    /**
     * 向服务器端当前位置的经纬度
     *
     * @param userId    用户ID
     * @param longitude 经度值
     * @param latitude  纬度值
     */
    private int send(String userId, double longitude, double latitude) {
        // 发送给服务器信息的代码。。。
        LogUtils.e("lat:"+latitude+",long:"+longitude);
        return AppConstants.UPLOAD_LOACTION_SUCCESS;
    }

}


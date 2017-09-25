package com.project.community.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.callback.ServerDao;
import com.project.community.callback.ServerDaoImpl;
import com.project.community.constants.AppConstants;
import com.project.community.constants.SharedPreferenceUtils;
import com.project.community.listener.AppLocationListener;
import com.project.community.model.UserModel;
import com.project.community.util.GPSUtil;
import com.project.community.util.NetworkUtils;
import com.project.community.util.NotificationUtil;
import com.project.community.util.ServiceUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qizfeng on 17/7/26.
 * 定位服务类
 */

public class AppLocationService extends Service {

    /**
     * Service action
     */
    public static final String ACTION_MY_LOCATION_SERVICE = "com.project.community.service.AppLocationService";

    /**
     * 间隔时间为60秒
     */
    private static final int DELAY_TIME = 15* 1000;
    /**
     * 开机一秒后开始检测网络
     */
    private static final int CHECK_NETWORK_DELAY_TIME = 1 * 1000;
    /**
     * Context
     */
    private Context mContext;
    /**
     * 定位SDK核心类
     */
    private LocationClient mLocationClient;
    /**
     * 定位结果处理器
     */
    private AppLocationListener mLocationListener;
    /**
     * 通知工具类
     */
    private NotificationUtil mNotificationUtil;
    /**
     * 服务的启动方式，开机自启动/手动启动
     */
    private int startingMode = AppConstants.UNKNOWN_START_SERVICE;
    /**
     * 当前网络是否可用的标识
     */
    private boolean isOpenNetwork = false;
    /**
     * 已经检测网络的次数
     */
    private int checkNetworkNumber = 0;
    /**
     * 最大检测次数
     */
    private int checkNetWorkMaxNum = 3;
    /**
     * 定时器
     */
    private Timer mTimer;

    /**
     * 保存所有跟服务器连接的客户端---每一个客户端绑定就多了一个
     */
    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    /**
     * 保留最后一次跟服务器连接的客户端的标识
     */
    private int mLastClient = 0;
    /**
     * Activity手动启动Service时就会得到这个引用---Messenger用来信息传递
     */
    Handler mHandler = new InComingHandler();
    private final Messenger mMessenger = new Messenger(mHandler);

    /**
     * Handler---子线程与UI线程交互---这里是与LocationListener交互 也可以作为与Activity之间的交互媒介
     */
    @SuppressLint("HandlerLeak")
    private class InComingHandler extends Handler {
        public void handleMessage(Message msg) {
            int result = msg.what;

            switch (result) {
                // Service与BDListener的交互
                case AppConstants.CHECK_NETWORK_CONNECT_FLAG: // 检查网络状态
                    doCheckNetWork();// 定时检查网络状态
                    break;
                case AppConstants.SEND_LOCATION_TO_SERVICE: // 传给Service
                   // LogUtils.e("更新LBS信息成功！");
                    Update(msg);
                    break;
                case AppConstants.UPLOAD_LOACTION_SUCCESS: // 上传地点到服务器成功
                    LogUtils.i("您当前的位置上传服务器成功！");
                    //show("您当前的位置上传服务器成功");
                    break;
                case AppConstants.UPLOAD_LOACTION_FAIL: // 上传地点到服务器失败
                    LogUtils.e("您当前的位置上传服务器失败！");
                    break;
                case AppConstants.LOCATION_NETWORK_EXCEPTION: // 定位网络错误
                    LogUtils.e("网络异常！请检查您的网络连接。");
                    // 通知用户网络不可用
                    mNotificationUtil.sendNetworkNotification();
                    break;
                case AppConstants.LOCATION_NETWORK_CONNECT_FAIL: // 定位网络连接失败
                    LogUtils.e("网络连接失败，请将网络关闭再重新打开试试！");
                    // 通知用户网络不可用
                    mNotificationUtil.sendNetworkNotification();
                    break;
                // 接下来是Service与Activity的交互
                case AppConstants.MSG_REGISTER_CLIENT:// 绑定客户端
                    mClients.add(msg.replyTo);
                    LogUtils.i("客户端注册绑定");
                    LogUtils.i("有" + mClients.size() + "客户端");
                    break;
                case AppConstants.MSG_UNREGISTER_CLIENT:// 解绑客户端
                    mClients.remove(msg.replyTo);
                    LogUtils.i("客户端解除绑定");
                    LogUtils.i("有" + mClients.size() + "客户端");
                    break;
                case AppConstants.MSG_SET_VALUE:// 客户端的通信数据
                    mLastClient = msg.arg1;
                    LogUtils.i("客户端传递消息");
                    for (int i = mClients.size() - 1; i >= 0; i--) {
                        try {
                            // 将消息发送给客户端
                            mClients.get(i).send(
                                    Message.obtain(null, AppConstants.MSG_SET_VALUE,
                                            mLastClient, 0));
                        } catch (RemoteException e) {
                            // 远程客户端出错，从list中移除
                            // 遍历列表以保证内部循环安全运行
                            mClients.remove(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 更新数据---发送给客户端
     */
    private void Update(Message msg) {
        // 改变消息类型
        msg.what = AppConstants.MSG_SET_VALUE;
        // 将MSG发送给已经绑定的客户端
        for (int i = mClients.size() - 1; i >= 0; i--) {
            try {
                LogUtils.e("将定位信息发送给客户端:" + i + "！");
                 LogUtils.e("msg.what:"+msg.what);
                // 将消息发送给客户端
                mClients.get(i).send(msg);
            } catch (RemoteException e) {
                // 远程客户端出错，从list中移除
                // 遍历列表以保证内部循环安全运行
                mClients.remove(i);
            }
        }
//         LogUtils.i("定位信息:"+myLocation);
        // txt_LocationView.setText(myLocation);
        ServerDao serverDao =new ServerDaoImpl(getApplicationContext());
        if( SharedPreferenceUtils.getBoolean(getApplicationContext(), SharedPreferenceUtils.SP_LOGIN, false)){
//            serverDao.doUploadLocation(getUser(getApplicationContext()).id,myl);

        }


    }
    /**
     * 获取登录用户信息
     *
     * @param context
     * @return
     */
    public UserModel getUser(Context context) {
        UserModel userModel = new UserModel();
        Gson gson = new Gson();
        try {
            userModel = gson.fromJson(SharedPreferenceUtils.getString(context, SharedPreferenceUtils.SP_USER), UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userModel;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 返回值可用来进行Activity与Service之间的交互
        LogUtils.i("BindService---onBind");
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        LogUtils.e("--------------MyLocatorService onCreate()----------------");

        mNotificationUtil = new NotificationUtil(this);
        mContext = AppLocationService.this;
        // 设置为前台进程，尽量避免被系统干掉。
        // MobileLocatorService.this.setForeground(true);
        // 初始化定位服务，配置相应参数
        initLocationService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("--------------MyLocationService onStartCommand()----------------");
        if (intent != null) {
            startingMode = intent.getIntExtra("startingMode",
                    AppConstants.UNKNOWN_START_SERVICE);
            LogUtils.i("startingMode = " + startingMode);
            if (startingMode == AppConstants.HANDLER_START_SERVICE) {
                LogUtils.e("-------------手动启动---------------");

                // 判断服务是否已开启
                boolean isRun = ServiceUtil.isServiceRun(
                        getApplicationContext(), "com.baidu.location.f");
                LogUtils.i("isRun = " + isRun);
                if (!isRun) {
                    LogUtils.e("MobileLocatorService start Location Service");

                    // 没启动，开启定位服务
                    mLocationClient.start();
                }
            }// 手动结束
            else {
                // 关闭手机，再次开启手机。这种情况下，startingMode的值获取不到。
                // 关机重启，这种情况下，startingMode的值可以拿到。
                // if (startingMode == AppConstants.BOOT_START_SERVICE) {
                LogUtils.e("-------------开机自启动---------------");
                checkNetworkNumber++;

                // 第一次，1分钟后检测网络---这个线程只会执行一次
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        LogUtils.e("--------------第一次检测网络---------------");
                        checkNetwork();

                        Message msg = new Message();
                        msg.arg1 = AppConstants.CHECK_NETWORK_CONNECT_FLAG;
                        mHandler.sendMessage(msg);

                    }
                }, CHECK_NETWORK_DELAY_TIME);
            }
        }

        return Service.START_REDELIVER_INTENT;
    }

    /**
     * 检查网络
     */
    private void doCheckNetWork() {
        // 第一检测网络，直接过了。（已打开） ---检查定位服务
        boolean isRun = ServiceUtil.isServiceRun(getApplicationContext(),
                "com.baidu.location.f");
        if (isOpenNetwork && isRun) {
            LogUtils.i("--------------第一次检测网络，直接过了。（已打开）----------------");
            return;
        }
        // 否则打开计时器---每个一段时间提醒一次
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                checkNetworkNumber++;
                LogUtils.i("Timer checkNetworkNumber = " + checkNetworkNumber);
                checkNetwork();

                boolean isRun = ServiceUtil.isServiceRun(
                        getApplicationContext(), "com.baidu.location.f");
                if (isOpenNetwork && isRun) {
                    mNotificationUtil
                            .cancelNotification(AppConstants.NOTIFICATIO_NETWORK_NOT_OPEN);
                    mTimer.cancel();
                    return;
                } else {
                    if (checkNetworkNumber == checkNetWorkMaxNum) {

                        LogUtils.e("--------------第三次检测网络，还未开启，直接退出应用---------");

                        // 检查网络，提醒了用户三次依然未开，退出应用。
                        mNotificationUtil
                                .cancelNotification(AppConstants.NOTIFICATIO_NETWORK_NOT_OPEN);
                        mNotificationUtil
                                .cancelNotification(AppConstants.NOTIFICATIO_GPS_NOT_OPEN);
                        mTimer.cancel();
                        // System.gc();
                        System.exit(0);
                    }
                }
            }

        }, 0, DELAY_TIME);
    }

    /**
     * 功能-检测网络是否可用
     */
    private void checkNetwork() {
        // 如果网络不可用，开启GPS就没有意义
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            isOpenNetwork = true;
            if (GPSUtil.isOPen(mContext) == false) {
                // 通知用户GPS未开启
                mNotificationUtil.sendGPSNotification();
            }
            LogUtils.e("MobileLocatorService start Location Service");

            // 开启定位服务
            mLocationClient.start();
        } else {
            // 通知用户网络不可用
            mNotificationUtil.sendNetworkNotification();
        }
    }

    /**
     * 初始化定位服务，配置相应参数
     */
    private void initLocationService() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mLocationListener = new AppLocationListener(mHandler);
        mLocationClient.registerLocationListener(mLocationListener);
        setLocationOption();

    }

    /**
     * 设置定位的相关参数
     */
    private void setLocationOption() {
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setOpenGps(true); // 是否打开GPS
        locationOption.setCoorType("bd09ll");
        locationOption.setIsNeedAddress(true);// 是否需要地理位置
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式--设置为高精度
        locationOption.setScanSpan(DELAY_TIME); // 设置发起定位请求的间隔时间
        // 设置产品线名称，强烈建议使用自定义的产品线名称，方便提供更高效准确的定位服务
        locationOption.setProdName(this.getString(R.string.loaction_prod_name));
        mLocationClient.setLocOption(locationOption);
    }


    @Override
    public void onDestroy() {
        LogUtils.e("---------------MyLocatorService onDestroy()----------------");
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            if (mLocationListener != null) {
                mLocationClient.unRegisterLocationListener(mLocationListener);
            }
        }
        SharedPreferences sp = mContext.getSharedPreferences(
                "MyMobileLocation", Context.MODE_PRIVATE);
        String result = sp.getString("instruct", null);
        LogUtils.i("MobileLocatorService onDestroy() result = " + result);
        if ("exit".equals(result)) {
            sp.edit().putString("instruct", "true").commit();
            LogUtils.e("---------------MobileLocatorService onDestroy()-----------1-----");
            System.exit(0);
            return;
        }

        LogUtils.e("---------------MobileLocatorService onDestroy()---------2-------");

        // 销毁时重新启动Service
        // Intent intent = new Intent(ACTION_MY_LOCATION_SERVICE);
        // intent.putExtra("startingMode", startingMode);
        // this.startService(intent);
    }
}


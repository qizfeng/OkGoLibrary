package com.project.community.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.constants.SharedPreferenceUtils;
import com.project.community.service.AppLocationService;
import com.project.community.ui.guide.WelcomeGuideActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ServiceUtil;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/4.
 * 启动动画页
 */

public class SplashActivity extends BaseActivity {
    /**
     * Messenger Service引用
     */
    private Messenger mMessengerService;
    /**
     * 客户端与Service的交互媒介---Service回复的对象
     */
    private final Messenger mMessenger = new Messenger(new InComingHandler());

    private boolean mIsBind = false;
    private LocationServiceConnection myServiceConnection = new LocationServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharedPreferenceUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, requestPermissions, REQUEST_PERMISSION_CODE);
            } else {
                Intent intent = new Intent(this, WelcomeGuideActivity.class);
                startActivity(intent);
                finish();
            }
            return;
        }
        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
    }

    private void enterHomeActivity() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, requestPermissions, REQUEST_PERMISSION_CODE);
        } else {
            doBindService();
            doStartService();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(myServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限，申请权限。
                } else {
                    // 有权限了，去放肆吧。
                    doBindService();
                    doStartService();
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // 权限被用户同意，可以去放肆了。
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 权限被用户拒绝了，洗洗睡吧。
                        ToastUtils.showShortToast(SplashActivity.this, getString(R.string.permission_tips));
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                return;
        }

    }

    /**
     * 客户端也有一个InComingHandler---用来交互信息 功能描述:
     *
     * @author dlc
     * @creatTime 2015年1月23日 上午10:12:35
     */
    @SuppressLint("HandlerLeak")
    private class InComingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // LogUtils.e("接收到消息:");
            int result = msg.what;
            switch (result) {
                case AppConstants.MSG_SET_VALUE: // 交互数据类型
                    //LogUtils.e("Received from service: " + msg.arg1);
                    // 这里用于更新网络信息---更新Location
                    Bundle b = msg.getData();
                    // 记得设置bundle时，将Location 键的值设为对应的location
                    //  String myLocation = b.getString("LocationData");
                    String latitute = b.getString("latitute");
                    String longitute = b.getString("longitute");
                    try {
                        if (longitute.startsWith("-"))
                            longitute = Math.abs(Double.parseDouble(longitute)) + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LogUtils.e("location:" + longitute + "," + latitute);

                    /**
                     * 上传位置信息
                     * 上传位置信息
                     * 上传位置信息
                     */
                    if (!TextUtils.isEmpty(latitute) && !TextUtils.isEmpty(longitute)) {
                        saveLocation(SplashActivity.this, latitute, longitute);
                        if (!isLogin(SplashActivity.this))
                            return;
                        if (!NetworkUtils.isNetworkAvailable(SplashActivity.this))
                            return;
                        serverDao.doUploadLocation(getUser(SplashActivity.this).id, longitute + "," + latitute, new JsonCallback<BaseResponse<List>>() {
                            @Override
                            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                                LogUtils.e(baseResponse.message);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                LogUtils.e(e.getMessage());
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * My ServiceConnection---Service连接连接监听类---绑定后会调用 得到引用后就可以通信了
     */
    private class LocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // service连接建立时将调用该方法
            // 返回IBinder接口以便我们可以跟service关联。
            // 我们可通过IPC接口来交流
            LogUtils.e("链接service");
            mMessengerService = new Messenger(service); // Service的Messenger
            mIsBind = true;
            try {
                // 注册
                Message msg = Message
                        .obtain(null, AppConstants.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;// 用来接收Service数据的Messenger
                mMessengerService.send(msg);
                LogUtils.e("注册");
                // 例子
                msg = Message.obtain(null, AppConstants.MSG_SET_VALUE, 11111, 0);
                mMessengerService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 当进程崩溃时将被调用，因为运行在同一程序，如果是崩溃将所以永远不会发生
            // 只有异常销毁时才会被调用
            LogUtils.e("和Service失去联系");
            mMessengerService = null;
            mIsBind = false;
        }

    }


    /**
     * startService
     */
    private void doStartService() {
        // 如果服务没有开启---现开启Service然后绑定
        boolean isRun = ServiceUtil.isServiceRun(getApplicationContext(),
                AppLocationService.ACTION_MY_LOCATION_SERVICE);
        if (!isRun) {
            Intent mIntent = new Intent(SplashActivity.this, AppLocationService.class);
            mIntent.setAction(AppLocationService.ACTION_MY_LOCATION_SERVICE);//你定义的service的action
            mIntent.setPackage(getPackageName());//这里你需要设置你应用的包名
//            startService(mIntent);
            mIntent.putExtra("startingMode", AppConstants.HANDLER_START_SERVICE);
            getApplicationContext().startService(mIntent);
            // LogUtils.e("开启Service---onStart:"+ startService(mIntent));
        }
    }

    /**
     * stop service
     */
    private void doStopService() {

        boolean isRun = ServiceUtil.isServiceRun(getApplicationContext(),
                "com.project.community.service.AppLocationService");
        if (isRun) {
            LogUtils.e("关闭Service---onStop");
            Intent mIntent = new Intent(
                    AppLocationService.ACTION_MY_LOCATION_SERVICE);
            stopService(mIntent);
        }

    }

    /**
     * bind service
     */
    private void doBindService() {
        if (mIsBind) {
            return;
        }
        LogUtils.e("bindService---onStart");
        // 绑定Service
        Intent mIntent = new Intent();
        mIntent.setPackage(getPackageName());
        mIntent.setAction(AppLocationService.ACTION_MY_LOCATION_SERVICE);
        boolean isRun = ServiceUtil.isServiceRun(getApplicationContext(),
                AppLocationService.ACTION_MY_LOCATION_SERVICE);
        if (!isRun) {
            LogUtils.e("bindService---onStart:" + bindService(mIntent, myServiceConnection, Context.BIND_AUTO_CREATE));
            mIsBind = true;
        }
    }

    /**
     * unbind
     */
    private void doUnBindService() {
        // 解绑Service
        if (mIsBind) {
            if (mMessengerService != null) {
                try {
                    // 发送取消注册消息
                    Message msg = Message.obtain(null,
                            AppConstants.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mMessengerService.send(msg);
                } catch (Exception e) {

                }
            }
            unbindService(myServiceConnection);
            mIsBind = false;
        }

    }


    public static final int REQUEST_PERMISSION_CODE = 123;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_LOGS = Manifest.permission.READ_LOGS;
    public static final String PERMISSION_SET_DEBUG_APP = Manifest.permission.SET_DEBUG_APP;
    public static final String PERMISSION_SYSTEM_ALERT_WINDOW = Manifest.permission.SYSTEM_ALERT_WINDOW;
    public static final String PERMISSION_WRITE_APN_SETTINGS = Manifest.permission.WRITE_APN_SETTINGS;

    private static final String[] requestPermissions = {
            // PERMISSION_RECORD_AUDIO,
            // PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
//            PERMISSION_READ_LOGS,
//            PERMISSION_SET_DEBUG_APP,
//            PERMISSION_SYSTEM_ALERT_WINDOW,
//            PERMISSION_WRITE_APN_SETTINGS
    };


}

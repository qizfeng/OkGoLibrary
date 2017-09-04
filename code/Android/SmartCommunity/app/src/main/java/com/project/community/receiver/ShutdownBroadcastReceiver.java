package com.project.community.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.library.okgo.utils.LogUtils;
import com.project.community.service.AppLocationService;


/**
 * 
 * 功能描述:关机时系统发出的广播接收器
 * @author  	dlc
 * @creatTime 	2015年1月22日 下午4:47:57 
 *
 */
public class ShutdownBroadcastReceiver extends BroadcastReceiver{

	 private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";

	@Override
	public void onReceive(Context context, Intent intent) {
        LogUtils.e("Shut down this system, ShutdownBroadcastReceiver onReceive()");
        
        if (intent.getAction().equals(ACTION_SHUTDOWN)) {  
            LogUtils.i("ShutdownBroadcastReceiver onReceive(), MobileLocatorService Stop");
              
            SharedPreferences sp = context.getSharedPreferences("MyMobileLocation", Context.MODE_PRIVATE);  
            sp.edit().putString("instruct", "exit").commit();  
              
            context.stopService(new Intent(AppLocationService.ACTION_MY_LOCATION_SERVICE));
        }  
		
	}  
	 
	 
}

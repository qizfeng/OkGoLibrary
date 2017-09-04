package com.project.community.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.library.okgo.utils.LogUtils;
import com.project.community.constants.AppConstants;
import com.project.community.service.AppLocationService;

/**
 * 
 * 功能描述:	开机启动时系统发出的广播接收器
 * @author  	dlc
 * @creatTime 	2015年1月22日 下午4:40:24 
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver{

	private static final String ACTION_BOOT="android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		 LogUtils.i("Boot this system , BootBroadcastReceiver onReceive()");
		 
		 if (intent.getAction().equals(ACTION_BOOT)) {
			 LogUtils.i("BootBroadcastReceiver onReceive(), MobileLocatorService Start");
	            //启动后台服务
	            Intent mIntent = new Intent(AppLocationService.ACTION_MY_LOCATION_SERVICE);
	            mIntent.putExtra("startingMode", AppConstants.BOOT_START_SERVICE);
	            context.startService(mIntent);  
	        }  
	}

}

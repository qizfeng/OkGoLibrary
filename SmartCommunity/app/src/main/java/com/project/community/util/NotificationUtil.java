package com.project.community.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.project.community.R;
import com.project.community.constants.AppConstants;


/**
 * 
 * 功能描述:以通知的形式提醒用户的处理工具类
 * 
 * @author dlc
 * @creatTime 2015年1月22日 上午11:36:26
 * 
 */
public class NotificationUtil {

	Context mContext;
	private Notification mNotification;

	private NotificationManager mNotificationManager;

	public NotificationUtil(Context context) {
		mContext = context;
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * 发送网络不可用的通知
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void sendNetworkNotification() {
		
//		mNotification = new Notification(R.mipmap.ic_launcher_round,
//				mContext.getString(R.string.notification_network_title),
//				System.currentTimeMillis())
//		;
		mNotification = new Notification.Builder(mContext)
				.setSmallIcon(R.mipmap.ic_launcher_round)
				.setContentTitle(mContext.getString(R.string.notification_network_name))
				.setContentText(mContext.getString(R.string.notification_network_content))
				.setWhen(System.currentTimeMillis())
				.build();
		// 该标志表示当用户点击 Clear 之后，能够清除该通知。
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// 向QQ一样常驻通知栏
		// mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		// 将使用默认的声音来提醒用户
		mNotification.defaults = Notification.DEFAULT_SOUND;

		Intent intent = new Intent();
		// 判断手机系统的版本 即API大于10 就是3.0或以上版本
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}

		// 这里需要设置Intent.FLAG_ACTIVITY_NEW_TASK属性
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mContentIntent = PendingIntent.getActivity(mContext, 0,
				intent, 0);
		// 这里必需要用setLatestEventInfo(上下文,标题,内容,PendingIntent)不然会报错.
//		mNotification.setLatestEventInfo(mContext,
//				mContext.getString(R.string.notification_network_name),
//				mContext.getString(R.string.notification_network_content),
//				mContentIntent);
		// 这里发送通知(消息ID,通知对象)
		mNotificationManager.notify(AppConstants.NOTIFICATIO_NETWORK_NOT_OPEN,
				mNotification);
	}

	/**
	 * 发送GPS未开启的通知
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void sendGPSNotification() {
//		mNotification = new Notification(R.mipmap.ic_launcher_round,
//				mContext.getString(R.string.notification_gps_title),
//				System.currentTimeMillis());
		mNotification = new Notification.Builder(mContext)
				.setSmallIcon(R.mipmap.ic_launcher_round)
				.setContentTitle(mContext.getString(R.string.notification_gps_name))
				.setContentText(mContext.getString(R.string.notification_gps_content))
				.setWhen(System.currentTimeMillis())
				.build();
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.defaults = Notification.DEFAULT_SOUND;

		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mContentIntent = PendingIntent.getActivity(mContext, 0,
				intent, 0);
//		mNotification.setLatestEventInfo(mContext,
//				mContext.getString(R.string.notification_gps_name),
//				mContext.getString(R.string.notification_gps_content),
//				mContentIntent);
		mNotificationManager.notify(AppConstants.NOTIFICATIO_GPS_NOT_OPEN,
				mNotification);
	}

	/**
	 * 取消通知
	 * 
	 * @param notificationId
	 */
	public void cancelNotification(int notificationId) {
		mNotificationManager.cancel(notificationId);
	}
}

package com.duxl.mobileframe.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.duxl.mobileframe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 版本下载并安装（通知栏更新）
 */
public class UpdateVersionNotification {

	public static boolean mIsUpdating; // 是否正确更新
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private Context mContext;
	private final int mNotifyId = 9999;
	private int mNotifyIcon = 0;

	public UpdateVersionNotification(Context context, int notifyIcon) {
		this.mContext = context;
		mNotifyIcon = notifyIcon;
		this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.mNotification = new Notification(notifyIcon, "版本更新", System.currentTimeMillis());
		this.mNotification.flags = Notification.FLAG_NO_CLEAR;
	}

	/**
	 * 更新通知栏进度
	 * 
	 * @param progress
	 */
	private void updateNotification(int progress) {
		RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.notification_update_version);
		remoteView.setImageViewResource(R.id.ivIcon_notification_update_version, mNotifyIcon);
		remoteView.setTextViewText(R.id.tvProgress_notification_update_version, "正在更新 " + progress + "%");
		remoteView.setProgressBar(R.id.pb_notification_update_version, 100, progress, false);
		mNotification.contentView = remoteView;

		// PendingIntent contentIntent = PendingIntent.getActivity (mContext,
		// 0,new Intent("android.settings.SETTINGS"), 0);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(), 0);
		mNotification.contentIntent = contentIntent;

		// 发出通知
		this.mNotificationManager.notify(mNotifyId, mNotification);
	}
	
	/**
	 * 执行更新
	 * @param updateURL
	 */
	public void doUpdate(final String updateURL) {
		new Thread() {
			public void run() {
				downloadApk(updateURL);
			};
		}.start();
	}

	/**
	 * 下载apk
	 * 
	 * @param updateURL
	 */
	private void downloadApk(String updateURL) {
		mIsUpdating = true;
		File apkFile = null;
		File updateDir = null;
		try {
			URL url = new URL(updateURL);
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			InputStream in = hc.getInputStream();
			hc.setConnectTimeout(15000);
			int totalSize = hc.getContentLength();
			int curSize = 0;
			int progress = 0;

			String pkgName = mContext.getPackageName();
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				updateDir = new File(Environment.getExternalStorageDirectory().getPath() + "/"+pkgName+"/apk/");
			} else {
				updateDir = new File("/data/data/"+pkgName+"/apk");
			}

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			apkFile = new File(updateDir.getPath(), "job.apk");
			if (apkFile.exists()) {
				apkFile.delete();
			}
			apkFile.createNewFile();

			// 修改文件夹及安装包的权限,供第三方应用访问
			try {
				Runtime.getRuntime().exec("chmod 705 " + updateDir.getPath());
				Runtime.getRuntime().exec("chmod 604 " + apkFile.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}

			FileOutputStream out = new FileOutputStream(apkFile);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				curSize += c;
				progress = curSize * 100 / totalSize;
				Message msg = new Message();
				msg.what = 200;
				msg.obj = apkFile.getPath();
				msg.arg1 = progress;
				mHandler.sendMessage(msg);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			mHandler.sendEmptyMessage(0);
			return;
		}
	}

	private long beginTime;
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if (msg.what == 0) {
				// 取消通知
				mIsUpdating = false;
				mNotificationManager.cancel(mNotifyId);
				Toast.makeText(mContext, "更新" + mContext.getString(R.string.app_name) + "失败", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 200) {
				if(msg.arg1 >= 100) {
					mIsUpdating = false;
					mNotificationManager.cancel(mNotifyId);
					String apkUpdatePath = msg.obj.toString();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.parse("file://" + apkUpdatePath), "application/vnd.android.package-archive");
					mContext.startActivity(intent);
				} else {
					if(System.currentTimeMillis() - beginTime > 1000) { // 1秒钟更新一次，降低通知栏更新频率
						updateNotification(msg.arg1);
						beginTime = System.currentTimeMillis();
					}
				}
			}
		}
	};
}

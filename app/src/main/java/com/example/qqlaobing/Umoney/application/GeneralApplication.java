package com.example.qqlaobing.Umoney.application;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;

import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.example.qqlaobing.Umoney.handler.AppSystemHandler;
import com.example.qqlaobing.Umoney.handler.AppSystemListenerContainer;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestController;
import com.umeng.commonsdk.UMConfigure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


/*
* create by zlz
*
* */
public class GeneralApplication extends Application {
	/**
	 * 屏幕宽度
	 */
	public static int screenWidth;
	/**
	 * 屏幕高度
	 */
	public static int screenHeight;
	/**
	 * 屏幕密度
	 */
	public static float screenDensity;
	// application实例
	private static GeneralApplication mInstance = null;
	// 网络模块的实例
	private NetRequestController mNetRequestController = null;

	// app 系统通知
	private AppSystemHandler mAppSystemHandler = null;
	private AppSystemListenerContainer mSystemListenerContainer = null;

	public GeneralApplication() {
		mInstance = this;
	}
	public AppSystemHandler getAppSystemHandler() {
		return mAppSystemHandler;
	}

	public AppSystemListenerContainer getSystemListenerContainer() {
		return mSystemListenerContainer;
	}

	public static GeneralApplication getInstance() {
		if (mInstance == null) {
			mInstance = new GeneralApplication();
		}
		return mInstance;
	}
	/**
	 * 初始化当前设备屏幕宽高
	 */
	private void initScreenSize() {
		DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
		screenWidth = curMetrics.widthPixels;
		screenHeight = curMetrics.heightPixels;
		screenDensity = curMetrics.density;
	}
	/**
	 * 方 法 名: getNetRequestController 功能描述: 获取网络模块的实例 参数说明: N/A 返 回 值:
	 * NetRequestController
	 */
	public NetRequestController getNetRequestController() {
		if (mNetRequestController == null) {
			mNetRequestController = NetRequestController.getInstance(this
					.getApplicationContext());
		}
		return mNetRequestController;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Application#onCreate() 方法描述： application的回调函数 参数说明： N/A
	 * 返 回 值：void
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initApplicationModel();
		MultiDex.install(this);
	}

	public static boolean checkPermission(Context context, String permission) {
		boolean result = false;
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				Class<?> clazz = Class.forName("android.content.Context");
				Method method = clazz.getMethod("checkSelfPermission",
						String.class);
				int rest = (Integer) method.invoke(context, permission);
				if (rest == PackageManager.PERMISSION_GRANTED) {
					result = true;
				} else {
					result = false;
				}
			} catch (Exception e) {
				result = false;
			}
		} else {
			PackageManager pm = context.getPackageManager();
			if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
				result = true;
			}
		}
		return result;
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = null;
			if (checkPermission(context, permission.READ_PHONE_STATE)) {
				device_id = tm.getDeviceId();
			}
			String mac = null;
			FileReader fstream = null;
			try {
				fstream = new FileReader("/sys/class/net/wlan0/address");
			} catch (FileNotFoundException e) {
				fstream = new FileReader("/sys/class/net/eth0/address");
			}
			BufferedReader in = null;
			if (fstream != null) {
				try {
					in = new BufferedReader(fstream, 1024);
					mac = in.readLine();
				} catch (IOException e) {
				} finally {
					if (fstream != null) {
						try {
							fstream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 方 法 名: initApplicationModel 功能描述: 初始化app中的模块实例 参数说明: N/A 返 回 值: void
	 */
	private void initApplicationModel() {
		// 应用程序系统的handler
		mAppSystemHandler = AppSystemHandler.getInstance();
		mSystemListenerContainer = AppSystemListenerContainer.getInstance();
		mInstance=this;
		initScreenSize();
		// 注册网络模块
		mNetRequestController = NetRequestController.getInstance(this
				.getApplicationContext());
		/**
		 * 设置组件化的Log开关
		 * 参数: boolean 默认为false，如需查看LOG设置为true
		 */
		UMConfigure.setLogEnabled(true);
//		UMConfigure.init(getInstance(), "5b7275c68f4a9d1d6d000044", "U_money", UMConfigure.DEVICE_TYPE_PHONE,null);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Application#onTerminate() 方法描述：释放资源 参数说明：N/A 返 回 值：void
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		mNetRequestController = null;
		ActivityManager activityMgr = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(getPackageName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Application#onLowMemory() 方法描述：释放资源 参数说明：N/A 返 回 值：void
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mNetRequestController = null;
		ActivityManager activityMgr = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(getPackageName());
	}
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	public void exitApplication() {
		Message sysMsg = mAppSystemHandler
				.obtainMessage(NetRequestBusinessDefine.K_EXIT_APPLICATION);
		mAppSystemHandler.sendMessage(sysMsg);
		GeneralApplication.getInstance().onTerminate();
	}

}

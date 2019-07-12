package com.example.qqlaobing.Umoney.handler;

import android.os.Handler;
import android.os.Message;

import com.example.qqlaobing.Umoney.application.GeneralApplication;

import java.util.Iterator;
import java.util.List;
/*
*  * 创  建  者: zhonglz
 * 创建日期: 2018年6月22日
* */

public class AppSystemHandler extends Handler {
	private static AppSystemHandler mInstance = null;

	public static AppSystemHandler getInstance() {
		if (mInstance == null) {
			mInstance = new AppSystemHandler();
		}
		return mInstance;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		List<AppSystemEventListener> list = GeneralApplication
				.getInstance().getSystemListenerContainer().getListenerList();
		for (Iterator<AppSystemEventListener> it = list.iterator(); it
				.hasNext();) {
			it.next().HandleSystemEvent(msg);
		}
	}

}

package com.example.qqlaobing.Umoney.handler;

import java.util.ArrayList;
import java.util.List;

/*
*  * 创  建  者: zhonglz
 * 创建日期: 2018年6月22日
* */

public class AppSystemListenerContainer {

	private static AppSystemListenerContainer mInstance = null;
	private List<AppSystemEventListener> mListenerList = new ArrayList<AppSystemEventListener>();

	public static AppSystemListenerContainer getInstance() {
		if (mInstance == null) {
			mInstance = new AppSystemListenerContainer();
		}
		return mInstance;
	}

	public List<AppSystemEventListener> getListenerList() {
		return mListenerList;
	}

	public void addSystemListener(AppSystemEventListener listener) {
		if (mListenerList != null) {
			if (!mListenerList.contains(listener)) {
				mListenerList.add(listener);
			}
		}
	}

	public void removeSystemListener(AppSystemEventListener listener) {
		if (mListenerList != null) {
			if (mListenerList.contains(listener)) {
				mListenerList.remove(listener);
			}
		}
	}

	public boolean hasSystemListener(AppSystemEventListener listener) {
		if (mListenerList != null) {
			return mListenerList.contains(listener);
		} else {
			return false;
		}
	}
}

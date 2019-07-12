package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;

import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IGetDataPresenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.been.UserInfoBeen;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class HomePresenterImpl implements IGetDataPresenter {
    private Handler mHandler;
    private UserInfoBeen userInfoBeen;
    public HomePresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_HOME);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_HOME,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void getData() {
        netLogain();
    }
}

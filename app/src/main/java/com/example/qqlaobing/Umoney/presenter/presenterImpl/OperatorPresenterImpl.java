package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class OperatorPresenterImpl implements IPostDataPrensenter {
    private String telephone,password;
    private Handler mHandler;
    public OperatorPresenterImpl(Handler mHandler, String telephone, String password){
        this.password=password;
        this.telephone=telephone;
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("password",password);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_LOGIN);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_LOGIN,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }
}

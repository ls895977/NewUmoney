package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class ApplyConfirmPresenterImpl implements IPostDataPrensenter {
    private String orderno;
    private Handler mHandler;
    public ApplyConfirmPresenterImpl(Handler mHandler, String orderno){
        this.orderno=orderno;
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("orderno",orderno);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_POST_APPLY);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_POST_APPLY,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }

}

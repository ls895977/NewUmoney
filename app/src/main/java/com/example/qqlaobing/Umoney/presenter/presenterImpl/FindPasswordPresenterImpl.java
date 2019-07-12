package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IGetDataPresenter;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class FindPasswordPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private Handler mHandler;
    private String telephone,password,repassword,varf,type;
    public FindPasswordPresenterImpl(Handler mHandler, String telephone, String password, String varf){
        this.mHandler=mHandler;
        this.telephone=telephone;
        this.password=password;
        this.varf=varf;
    }
    public FindPasswordPresenterImpl(Handler mHandler, String telephone, String type){
        this.mHandler=mHandler;
        this.telephone=telephone;
        this.type=type;

    }
    private void netRegister(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("password",password);
        params.put("varf",varf);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_FINDPASSWORD);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_FINDPASSWORD,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    private void netGetS(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("type",3);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GETS);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GETS,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netRegister();
    }

    @Override
    public void getData() {
        netGetS();
    }
}

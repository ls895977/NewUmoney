package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IGetDataPresenter;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class LoginPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private String telephone,password,address,apppath;
    private Handler mHandler;
    public LoginPresenterImpl(Handler mHandler,String telephone, String password,String address,String apppath){
        this.password=password;
        this.telephone=telephone;
        this.mHandler=mHandler;
        this.address=address;
        this.apppath=apppath;
    }
    public LoginPresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("from","android");
        params.put("password",password);
        params.put("address",address);
        params.put("apppath",apppath);
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
    public void codeLogain(String telephone, String code,String address){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("from","android");
        params.put("code",code);
        params.put("address",address);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_LOGIN_CODE);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_LOGIN_CODE,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    private void netupdata(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("type","android");
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_UPDATA);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_UPDATA,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }

    @Override
    public void getData() {
        netupdata();
    }
}

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

public class RegisterPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private Handler mHandler;
    private String telephone,password,repassword,varf,type,apppath;
    public RegisterPresenterImpl(Handler mHandler,String telephone, String password, String repassword, String varf, String apppath){
        this.mHandler=mHandler;
        this.telephone=telephone;
        this.password=password;
        this.repassword=repassword;
        this.varf=varf;
        this.apppath=apppath;
    }
    public RegisterPresenterImpl(Handler mHandler,String telephone,String type){
        this.mHandler=mHandler;
        this.telephone=telephone;
        this.type=type;

    }
    private void netRegister(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("password",password);
        params.put("repassword",repassword);
        params.put("from","android");
        params.put("varf",varf);
        params.put("apppath",apppath);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_REGISTE);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_REGISTE,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    private void netGetS(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("type",type);
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

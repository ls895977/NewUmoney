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
 * Created by zhanglizhi on 2018/7/19.
 */

public class UserInfoPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private Handler mHandler;
    private String username,idcard,telephone,idcardimg1,idcardimg2,idcardimg3;
    public UserInfoPresenterImpl(Handler mHandler,String username, String idcard,
                                 String telephone, String idcardimg1, String idcardimg2,String idcardimg3){
        this.mHandler=mHandler;
        this.telephone=telephone;
        this.idcardimg3=idcardimg3;
        this.idcardimg2=idcardimg2;
        this.idcardimg1=idcardimg1;
        this.idcard=idcard;
        this.username=username;
    }
    public UserInfoPresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netPostUserInfo(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",telephone);
        params.put("username",username);
        params.put("idcard",idcard);
        params.put("idcardimg1",idcardimg1);
        params.put("idcardimg2",idcardimg2);
        params.put("idcardimg3",idcardimg3);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_POSTUSER_INFO);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_POSTUSER_INFO,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);
    }
    private void netGetUserInfo(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GET_USERINFO);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GET_USERINFO,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }

    @Override
    public void postData() {
        netPostUserInfo();
    }

    @Override
    public void getData() {
        netGetUserInfo();
    }
}

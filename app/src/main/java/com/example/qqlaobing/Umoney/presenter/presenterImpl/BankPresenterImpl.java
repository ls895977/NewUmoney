package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.been.BankBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IGetDataPresenter;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class BankPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private Handler mHandler;
    private BankBeen bankBeen;
    public BankPresenterImpl(Handler mHandler, BankBeen bankBeen){
        this.bankBeen=bankBeen;
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",bankBeen.getTelephone());
        params.put("username",bankBeen.getUsername());
        params.put("idcard",bankBeen.getIdcard());
        params.put("bankno",bankBeen.getBankcard());
        params.put("name",bankBeen.getBankname());
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_BANK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_BANK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }
    public BankPresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netGetLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GET_BANK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GET_BANK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    public void getBackLists(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GETBACKLIST);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GETBACKLIST,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    public void getBack(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GETBACK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GETBACK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    public void deleteBack(String bankid){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("bankid",bankid);
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_DELLETBACK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_DELLETBACK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    public void addBack(BankBeen bankBeen){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("telephone",bankBeen.getTelephone());
        params.put("username",bankBeen.getUsername());
        params.put("idcard",bankBeen.getIdcard());
        params.put("bankno",bankBeen.getBankcard());
//        params.put("id",bankBeen.getId());
        params.put("bankname",bankBeen.getBankname());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_ADDBACK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_ADDBACK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void getData() {
        netGetLogain();
    }
}

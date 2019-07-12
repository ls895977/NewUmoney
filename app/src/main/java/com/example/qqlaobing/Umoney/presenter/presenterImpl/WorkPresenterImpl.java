package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IGetDataPresenter;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.been.WorkBeen;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class WorkPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private WorkBeen workBeen;
    private Handler mHandler;
    public WorkPresenterImpl(Handler mHandler, WorkBeen workBeen){
        this.workBeen=workBeen;
        this.mHandler=mHandler;
    }

    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("trade",workBeen.getTrade());
        params.put("work",workBeen.getWork());
        params.put("name",workBeen.getName());
        params.put("telephone",workBeen.getTelephone());
        params.put("marriage",workBeen.getMarriage());
        params.put("address",workBeen.getAddress());
        params.put("use",workBeen.getUse());
        params.put("workimg",workBeen.getWorkimg());
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_WORK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_WORK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    public WorkPresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netGetLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GET_WORK);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GET_WORK,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void getData() {
        netGetLogain();
    }
    @Override
    public void postData() {
        netLogain();
    }


}

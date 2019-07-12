package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;

import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class OderListPresenterImpl implements IPostDataPrensenter {
    private String page,limit;
    private Handler mHandler;
    public OderListPresenterImpl(Handler mHandler, String page, String limit){
        this.page=page;
        this.limit=limit;
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("limit",limit);
        params.put("page",page);
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GET_LISTS);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GET_LISTS,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }
}

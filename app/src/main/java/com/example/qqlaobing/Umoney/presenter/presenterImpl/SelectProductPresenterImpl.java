package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;

import java.util.HashMap;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class SelectProductPresenterImpl implements IPostDataPrensenter {
    private Handler mHandler;
    public SelectProductPresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_selectProducts);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_selectProducts,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }

}

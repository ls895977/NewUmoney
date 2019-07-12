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

public class GetMoneyPresenterImpl implements IPostDataPrensenter {
    private Handler mHandler;
    private String day,amount,product_id;
    public GetMoneyPresenterImpl(Handler mHandler,String day,String amount,String product_id){
        this.mHandler=mHandler;
        this.day=day;
        this.amount=amount;
        this.product_id=product_id;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("days",day);
        params.put("amount",amount);
        params.put("product_id",product_id);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GET_MONEY);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GET_MONEY,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }

}

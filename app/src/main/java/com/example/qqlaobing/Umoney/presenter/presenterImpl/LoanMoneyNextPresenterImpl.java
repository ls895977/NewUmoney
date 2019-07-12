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

public class LoanMoneyNextPresenterImpl implements IPostDataPrensenter {
    private Handler mHandler;
    private String loan_id,bank_id,product_id;
    public LoanMoneyNextPresenterImpl(Handler mHandler,String loan_id,String bank_id,String product_id){
        this.mHandler=mHandler;
        this.product_id=product_id;
        this.bank_id=bank_id;
        this.loan_id=loan_id;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("loan_id",loan_id);
        params.put("bank_id","1");
        params.put("product_id",product_id);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_comfirmProduct);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_comfirmProduct,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }

}

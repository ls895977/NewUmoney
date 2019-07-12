package com.example.qqlaobing.Umoney.presenter.presenterImpl;

import android.os.Handler;
import android.util.Log;

import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IGetDataPresenter;
import com.example.qqlaobing.Umoney.presenter.ipresenter.IPostDataPrensenter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.been.ContactBeen;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/18.
 */

public class ContactPresenterImpl implements IPostDataPrensenter,IGetDataPresenter {
    private Handler mHandler;
    private List<ContactBeen> list;
    private JSONArray alllist;
    public ContactPresenterImpl(Handler mHandler, List<ContactBeen> list,JSONArray alllist){
        this.list=list;
        this.mHandler=mHandler;
        this.alllist=alllist;
    }
    private void netLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username1",list.get(0).getName());
        params.put("telephone1",list.get(0).getTelPhone());
        params.put("relationship1",list.get(0).getRelation());

        params.put("username2",list.get(1).getName());
        params.put("telephone2",list.get(1).getTelPhone());
        params.put("relationship2",list.get(1).getRelation());
        params.put("othercontacts",alllist);
        Log.e("ssssssssssaaaa",params.toString());
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_CONTACT);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_CONTACT,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void postData() {
        netLogain();
    }
    public ContactPresenterImpl(Handler mHandler){
        this.mHandler=mHandler;
    }
    private void netGetLogain(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        String url = NetRequestRULBuilder
                .buildRequestUrl(NetRequestBusinessDefine.K_GET_CONTACT);
        GeneralApplication
                .getInstance()
                .getNetRequestController()
                .requestJsonObject(
                        mHandler.getClass().getName(),
                        params,
                        NetRequestBusinessDefine.K_GET_CONTACT,
                        url);
        GeneralApplication.getInstance().getNetRequestController()
                .registHandler(mHandler);

    }
    @Override
    public void getData() {
        netGetLogain();
    }
}

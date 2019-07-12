package com.example.qqlaobing.Umoney.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.LoanAdapter;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.OderListPresenterImpl;

import com.example.qqlaobing.Umoney.adapter.MyLoanAdapter;
import com.example.qqlaobing.Umoney.been.MyLoanBeen;
import com.example.qqlaobing.Umoney.view.myview.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class MyLoanFragment extends Fragment{
    private View rootView;
    private RefreshListView lvMyLoan;
    private List<MyLoanBeen> data;
    private int  page=1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_GET_LISTS:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONArray list=results.getJSONArray("loan_list");
                            Log.e("list",list.length()+"");
                            if (list.length()==0){
                                if (NetRequestBusinessDefine.login) {
                                    Log.e("list", "123");
                                    Toast.makeText(getActivity(), "没有更多贷款数据", Toast.LENGTH_LONG).show();
                                    lvMyLoan.loadComplete();
                                }else {
                                    break;
                                }
                            }else {
                                data = new ArrayList<>();
                                for (int i = 0; i < list.length(); i++) {
                                    Log.e("list", "11111111111");
                                    MyLoanBeen myLoanBeen = new MyLoanBeen();
                                    myLoanBeen.setDamount(((JSONObject) list.get(i)).getString("damount"));
                                    myLoanBeen.setStatus(((JSONObject) list.get(i)).getString("status"));
                                    myLoanBeen.setInterest(((JSONObject) list.get(i)).getString("interest"));
                                    myLoanBeen.setDeadline(((JSONObject) list.get(i)).getString("deadline"));
                                    myLoanBeen.setOrderno(((JSONObject) list.get(i)).getString("orderno"));
                                    myLoanBeen.setOverduefee(((JSONObject) list.get(i)).getString("overduefee"));
                                    myLoanBeen.setRefundtime(((JSONObject) list.get(i)).getString("overdue_days"));
                                    myLoanBeen.setTime(((JSONObject) list.get(i)).getString("addtime"));
                                    JSONArray service_fee=((JSONObject) list.get(i)).getJSONArray("service_fee");
                                    List<MyLoanBeen.FeiYong> feiYongList=new ArrayList<>();
                                    for (int j=0;j<service_fee.length();j++){
                                        JSONObject x_fee=service_fee.getJSONObject(j);
                                        MyLoanBeen.FeiYong feiYong=new MyLoanBeen.FeiYong();
                                        feiYong.setDesc(x_fee.getString("desc"));
                                        feiYong.setVal(x_fee.getString("val"));
                                        feiYongList.add(feiYong);
                                    }
                                    myLoanBeen.setFeiYongList(feiYongList);
                                    data.add(myLoanBeen);
                                    Log.e("listaaa", data.get(i).getOrderno());
                                }
//                                Log.e("listaaa", data.toString());
                                adapter = new LoanAdapter(getActivity(), data);
                                lvMyLoan.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                lvMyLoan.loadComplete();
//                                lvMyLoan.loadComplete();
                            }
                        }else {
                            lvMyLoan.loadComplete();
                            Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        lvMyLoan.loadComplete();
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    };
    private LoanAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_myloan, container, false);
        initView();
        initData();
        Log.e("eeeeeeee","start");
        return rootView;
    }

    private void initData() {
        lvMyLoan.setInterface(new RefreshListView.LoadListener() {
            @Override
            public void onLoad() {
                page++;
                OderListPresenterImpl oderListPresenter = new OderListPresenterImpl(mHandler, String.valueOf(page), "10");
                oderListPresenter.postData();
            }
            @Override
            public void pullLoad() {
                OderListPresenterImpl oderListPresenter = new OderListPresenterImpl(mHandler, "1", "10");
                oderListPresenter.postData();
            }
        });

    }

    private void initView() {
        lvMyLoan=rootView.findViewById(R.id.lv_myloan);
    }
    public void upData(){
        OderListPresenterImpl oderListPresenter = new OderListPresenterImpl(mHandler, "1", "10");
        oderListPresenter.postData();
    }

}

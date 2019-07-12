package com.example.qqlaobing.Umoney.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.view.activity.CertificationCenterActivity;
import com.example.qqlaobing.Umoney.view.activity.ChangePasswordActivity;
import com.example.qqlaobing.Umoney.view.activity.KeFuActivity;
import com.example.qqlaobing.Umoney.view.activity.QuanActivity;

import com.example.qqlaobing.Umoney.view.activity.AboutUsActivity;
import com.example.qqlaobing.Umoney.view.activity.LoginActivity;
import com.example.qqlaobing.Umoney.view.activity.MainActivity;
import com.example.qqlaobing.Umoney.view.activity.MyBankCardActivity;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class MyFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private LinearLayout ll_myoder,ll_aboutus,ll_youhuiquan,ll_changepassword,ll_renzheng,ll_kefu;
    private LinearLayout ll_bankcard;
    private TextView tv_username;
    private Button bt_exit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        initData();
        return rootView;
    }

    private void initData() {
        ll_myoder.setOnClickListener(this);
        ll_renzheng.setOnClickListener(this);
        ll_aboutus.setOnClickListener(this);
        ll_kefu.setOnClickListener(this);
//        ll_youhuiquan.setOnClickListener(this);
        ll_bankcard.setOnClickListener(this);
        ll_changepassword.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
        SharedPreferences share=getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (share!=null){
            tv_username.setText(share.getString("username",""));
        }
    }

    private void initView() {
        ll_aboutus=(LinearLayout)rootView.findViewById(R.id.ll_aboutus);
        ll_kefu=(LinearLayout)rootView.findViewById(R.id.ll_kefu);
        ll_renzheng=(LinearLayout)rootView.findViewById(R.id.ll_renzheng);
        ll_changepassword=(LinearLayout)rootView.findViewById(R.id.ll_changepassword);
        ll_myoder=(LinearLayout)rootView.findViewById(R.id.ll_myoder);
//        ll_youhuiquan=(LinearLayout)rootView.findViewById(R.id.ll_youhuiquan);
        ll_bankcard =(LinearLayout)rootView.findViewById(R.id.ll_bankcard);
        tv_username =(TextView)rootView.findViewById(R.id.tv_username);
        bt_exit =(Button)rootView.findViewById(R.id.bt_exit);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.ll_youhuiquan:
//                    startActivity(new Intent(getActivity(), QuanActivity.class));
//                break;
            case R.id.ll_bankcard:
                startActivity(new Intent(getActivity(), MyBankCardActivity.class));
                break;
            case R.id.ll_renzheng:
                startActivity(new Intent(getActivity(), CertificationCenterActivity.class));
                break;
            case R.id.ll_aboutus:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.ll_myoder:
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.gotoDownloadFragment();
                break;
            case R.id.ll_changepassword:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.ll_kefu:
                startActivity(new Intent(getActivity(), KeFuActivity.class));
                break;
            case R.id.bt_exit:
                SharedPreferences share=getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
                share.edit().clear().commit();
                NetRequestBusinessDefine.login=false;
                NetRequestBusinessDefine.K_TOKEN="";
                MainActivity mainActivity1=(MainActivity)getActivity();
                startActivity(new Intent(mainActivity1, LoginActivity.class));
                mainActivity1.finish();
                break;
            default:break;
        }
    }
}

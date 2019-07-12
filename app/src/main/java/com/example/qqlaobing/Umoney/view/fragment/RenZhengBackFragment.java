package com.example.qqlaobing.Umoney.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.BankBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.BankPresenterImpl;
import com.example.qqlaobing.Umoney.view.activity.BankCardActivity;
import com.example.qqlaobing.Umoney.view.activity.CertificationCenterActivity;
import com.example.qqlaobing.Umoney.view.activity.MainActivity;
import com.example.qqlaobing.Umoney.view.activity.RenZhengActivity;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class RenZhengBackFragment extends Fragment implements View.OnClickListener{
    private TextView tv_sure;
    private ImageView tv_exit;
    private EditText et_username,et_telephone,et_idcard,et_bankid,et_bank;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_BANK:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            dialog.close();
                            Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                            RenZhengActivity renZhengActivity=(RenZhengActivity)getActivity();
                            renZhengActivity.ChangeFragment(3);
                        }else {
                            Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_LONG).show();
                            dialog.close();
                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;

                default:break;
            }
        }
    };
    private LoadingDialog dialog;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bank_card, container, false);
        initView();
        initData();
        return rootView;
    }
    private void initData() {
        BankPresenterImpl bankPresenter=new BankPresenterImpl(mHandler);
        bankPresenter.getData();

        tv_sure.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    private void initView() {
        tv_sure=(TextView)rootView.findViewById(R.id.tv_sure);
        tv_exit=(ImageView) rootView.findViewById(R.id.tv_exit);
        et_username=(EditText) rootView.findViewById(R.id.et_username);
        et_telephone=(EditText) rootView.findViewById(R.id.et_telephone);
        et_idcard=(EditText) rootView.findViewById(R.id.et_idcard);
        et_bankid=(EditText) rootView.findViewById(R.id.et_bankid);
        et_bank=(EditText) rootView.findViewById(R.id.et_bank);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                if (et_bank.getText().toString().equals("")||et_username.getText().toString().equals("")||
                        et_telephone.getText().toString().equals("")||et_idcard.getText().toString().equals("")||
                        et_bankid.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"资料填写不完整",Toast.LENGTH_LONG).show();
                }else {
                    dialog = new LoadingDialog(getActivity(), "上传中...");
                    dialog.show();
                    BankBeen bankBeen=new BankBeen();
                    bankBeen.setTelephone(et_telephone.getText().toString());
                    bankBeen.setBankcard(et_bankid.getText().toString());
                    bankBeen.setBankname(et_bank.getText().toString());
                    bankBeen.setIdcard(et_idcard.getText().toString());
                    bankBeen.setUsername(et_username.getText().toString());
                    BankPresenterImpl bankPresenter=new BankPresenterImpl(mHandler,bankBeen);
                    bankPresenter.postData();
                }
                break;
            case R.id.tv_exit:
                break;
            default:break;
        }

    }
}

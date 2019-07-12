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
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.ZhiMaPresenterImpl;
import com.example.qqlaobing.Umoney.view.activity.MainActivity;
import com.example.qqlaobing.Umoney.view.activity.RenZhengActivity;
import com.example.qqlaobing.Umoney.view.activity.ZhiMaActivity;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class RenZhengZhiMaFragment extends Fragment implements View.OnClickListener{
    private EditText et_zhima;
    private TextView tv_sure;
    private ImageView iv_exit;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_ZHIMA:
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
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            Log.e("111","main");
                            NetRequestBusinessDefine.login = true;
                            startActivity(intent);
                            getActivity().finish();

                        }else {
                            Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_LONG).show();
                            dialog.close();
                        }
                    } catch (JSONException e) {
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
        rootView = inflater.inflate(R.layout.fragment_zi_ma, container, false);
        initView();
        initData();
        return rootView;
    }
    private void initData() {
        ZhiMaPresenterImpl zhiMaPresenter=new ZhiMaPresenterImpl(mHandler);
        zhiMaPresenter.getData();

        iv_exit.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
    }

    private void initView() {
        iv_exit=(ImageView)rootView.findViewById(R.id.iv_exit);
        tv_sure=(TextView) rootView.findViewById(R.id.tv_sure);
        et_zhima=(EditText) rootView.findViewById(R.id.et_zhima);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                if (et_zhima.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请填写芝麻分",Toast.LENGTH_LONG).show();
                }else {
                    dialog = new LoadingDialog(getActivity(), "上传中...");
                    dialog.show();
                    ZhiMaPresenterImpl zhiMaPresenter=new ZhiMaPresenterImpl(mHandler,et_zhima.getText().toString());
                    zhiMaPresenter.postData();
                }
                break;
            case R.id.iv_exit:
                break;
            default:break;
        }

    }
}

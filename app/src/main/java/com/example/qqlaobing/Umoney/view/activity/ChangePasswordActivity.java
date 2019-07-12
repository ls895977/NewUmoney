package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.net.NetRequestRULBuilder;
import com.githang.statusbar.StatusBarCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText et_sure_password,et_password;
    private Button btn_change;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_CHANGEPASSWORD:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            ChangePasswordActivity.this.finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
            };
    private ImageView tv_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_password);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        btn_change=(Button)findViewById(R.id.btn_change);
        et_password=(EditText)findViewById(R.id.et_password);
        tv_exit =(ImageView)findViewById(R.id.tv_exit);
        et_sure_password=(EditText)findViewById(R.id.et_sure_password);
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordActivity.this.finish();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_password.equals("")||et_sure_password.equals("")){
                    Toast.makeText(ChangePasswordActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else {
                    if (et_sure_password.getText().toString().equals(et_password.getText().toString())){
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("password",et_password.getText().toString());
                        Log.e("ssssssssssaaaa",params.toString());
                        String url = NetRequestRULBuilder
                                .buildRequestUrl(NetRequestBusinessDefine.K_CHANGEPASSWORD);
                        GeneralApplication
                                .getInstance()
                                .getNetRequestController()
                                .requestJsonObject(
                                        mHandler.getClass().getName(),
                                        params,
                                        NetRequestBusinessDefine.K_CHANGEPASSWORD,
                                        url);
                        GeneralApplication.getInstance().getNetRequestController()
                                .registHandler(mHandler);
                    }else {
                        Toast.makeText(ChangePasswordActivity.this,"两次密码不同",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

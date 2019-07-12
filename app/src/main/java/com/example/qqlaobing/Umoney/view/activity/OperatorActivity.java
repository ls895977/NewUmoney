package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;

import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 运营商界面
 * @author zlz
 */
public class OperatorActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout ll_getpassword,ll_error;
    private EditText et_phone_password,et_phone;
    private ImageView tv_exit;
    private TextView tv_getcode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_OPERATOR:
                    try {
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            Intent intent = new Intent();
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                            setResult(5, intent);
                            OperatorActivity.this.finish();
                        }else {
                            Toast.makeText(OperatorActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
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
    private TextView tv_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_operator);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        initView();
        initData();
    }

    private void initData() {
        ll_getpassword.setOnClickListener(this);
        ll_error.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_getcode.setOnClickListener(this);

    }

    private void initView() {
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_phone_password=(EditText)findViewById(R.id.et_phone_password);
        ll_getpassword=(LinearLayout) findViewById(R.id.ll_getpassword);
        ll_error=(LinearLayout)findViewById(R.id.ll_error);
        tv_exit=(ImageView) findViewById(R.id.tv_exit);
        tv_finish =(TextView) findViewById(R.id.tv_finish);
        tv_getcode =(TextView) findViewById(R.id.tv_getcode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_exit:
                dialog = new LoadingDialog(OperatorActivity.this, "上传中...");
                dialog.show();
                this.finish();
                break;
            case R.id.tv_finish:
                Intent intent = new Intent();
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                setResult(5, intent);
                OperatorActivity.this.finish();
                break;
            default:break;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

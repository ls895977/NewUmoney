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
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.ZhiMaPresenterImpl;

import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ZhiMaActivity extends AppCompatActivity implements View.OnClickListener{
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
                            Toast.makeText(ZhiMaActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                            setResult(6, intent);
                            ZhiMaActivity.this.finish();
                        }else {
                            Toast.makeText(ZhiMaActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                            dialog.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GET_ZHIMA:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONObject zmfinfo = data.getJSONObject("zmfinfo");
                            if (zmfinfo.getString("zmfinfo").equals("")||zmfinfo.getString("zmfinfo")==null){
                                et_zhima.setText("");
                            }else{
                                et_zhima.setText(zmfinfo.getString("zmfinfo"));
                            }
                        }else {
                            Toast.makeText(ZhiMaActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_zi_ma);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        initView();
        initData();
    }

    private void initData() {
        ZhiMaPresenterImpl zhiMaPresenter=new ZhiMaPresenterImpl(mHandler);
        zhiMaPresenter.getData();

        iv_exit.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
    }

    private void initView() {
        iv_exit=(ImageView)findViewById(R.id.iv_exit);
        tv_sure=(TextView) findViewById(R.id.tv_sure);
        et_zhima=(EditText) findViewById(R.id.et_zhima);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                if (et_zhima.getText().toString().equals("")){
                    Toast.makeText(this,"请填写芝麻分",Toast.LENGTH_LONG).show();
                }else {
                    dialog = new LoadingDialog(ZhiMaActivity.this, "上传中...");
                    dialog.show();
                    ZhiMaPresenterImpl zhiMaPresenter=new ZhiMaPresenterImpl(mHandler,et_zhima.getText().toString());
                    zhiMaPresenter.postData();
                }
                break;
            case R.id.iv_exit:
                this.finish();
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

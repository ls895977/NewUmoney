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
import com.example.qqlaobing.Umoney.been.BankBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.BankPresenterImpl;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 银行卡认证
 * @author zlz
 */
public class BankCardActivity extends AppCompatActivity implements View.OnClickListener{
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
                            Toast.makeText(BankCardActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                                setResult(3, intent);
                                BankCardActivity.this.finish();
                        }else {
                            Toast.makeText(BankCardActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                            dialog.close();
                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GET_BANK:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONObject bankinfo = data.getJSONObject("bankinfo");
                            et_bank.setText(bankinfo.getString("name"));
                            et_bankid.setText(bankinfo.getString("bankno"));
                            et_idcard.setText(bankinfo.getString("idcard"));
                            et_telephone.setText(bankinfo.getString("telephone"));
                            et_username.setText(bankinfo.getString("username"));
                        }else {
                            Toast.makeText(BankCardActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_bank_card);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        initView();
        initData();
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

    private void initData() {
        BankPresenterImpl bankPresenter=new BankPresenterImpl(mHandler);
        bankPresenter.getData();

        tv_sure.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    private void initView() {
        tv_sure=(TextView)findViewById(R.id.tv_sure);
        tv_exit=(ImageView) findViewById(R.id.tv_exit);
        et_username=(EditText) findViewById(R.id.et_username);
        et_telephone=(EditText) findViewById(R.id.et_telephone);
        et_idcard=(EditText) findViewById(R.id.et_idcard);
        et_bankid=(EditText) findViewById(R.id.et_bankid);
        et_bank=(EditText) findViewById(R.id.et_bank);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                if (et_bank.getText().toString().equals("")||et_username.getText().toString().equals("")||
                        et_telephone.getText().toString().equals("")||et_idcard.getText().toString().equals("")||
                        et_bankid.getText().toString().equals("")){
                    Toast.makeText(this,"资料填写不完整",Toast.LENGTH_LONG).show();
                }else {
                    dialog = new LoadingDialog(BankCardActivity.this, "上传中...");
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
                    this.finish();
                break;
            default:break;
        }

    }
}

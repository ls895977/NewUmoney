package com.example.qqlaobing.Umoney.view.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;

import com.example.qqlaobing.Umoney.presenter.presenterImpl.RegisterPresenterImpl;
import com.example.qqlaobing.Umoney.view.myview.TimeButton;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisteActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etPhoneNumber,etValidateCode,etPassword,etConfirmPassword;
    private Button btnRegister;
    private TimeButton btnGetCode;
    private TextView tvGotoLogin,tvError,tv_exit;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_REGISTE:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("net",msg.obj.toString());
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            if (!response.isNull("data")) {
                                JSONObject results = response.getJSONObject("data");
                                RegisteActivity.this.finish();
                            }
                        }else {
                            Toast.makeText(RegisteActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GETS:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("net",msg.obj.toString());
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            if (!response.isNull("data")) {
                           Toast.makeText(RegisteActivity.this,"验证码发送成功",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(RegisteActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registe);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        initView();
        initData();
    }

    private void initData() {
        btnGetCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvGotoLogin.setOnClickListener(this);
        tvError.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    private void initView() {
        etPhoneNumber=(EditText) findViewById(R.id.et_phonenumber);
        etValidateCode=(EditText)findViewById(R.id.et_validatecode);
        etPassword=(EditText)findViewById(R.id.et_password);
        etConfirmPassword=(EditText)findViewById(R.id.et_confirmpassword);
        btnGetCode=(TimeButton) findViewById(R.id.btn_getcode);
        btnRegister=(Button)findViewById(R.id.btn_register);
        tvGotoLogin=(TextView) findViewById(R.id.tv_gotologin);
        tvError=(TextView) findViewById(R.id.tv_error);
        tv_exit=(TextView) findViewById(R.id.tv_exit);
        btnGetCode.setEnabled(false);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMobile(etPhoneNumber.getText().toString())){
                    btnGetCode.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isMobile(etPhoneNumber.getText().toString())){
                    btnGetCode.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getcode:
                if (isMobile(etPhoneNumber.getText().toString())){
                    //验证码接口
                    btnGetCode.setLenght(60000);
                    btnGetCode.starTime();
                    RegisterPresenterImpl presenter=new RegisterPresenterImpl(mHandler,etPhoneNumber.getText().toString(),"1");
                    presenter.getData();
                }else {
                    Toast.makeText(RegisteActivity.this,"请输入正确手机号",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_register:
                if (etValidateCode.getText().toString().equals("")){
                    Toast.makeText(RegisteActivity.this,"请输入验证码",Toast.LENGTH_LONG).show();
                }
                if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                    //注册接口
                    RegisterPresenterImpl registerPresenter=new RegisterPresenterImpl(mHandler,etPhoneNumber.getText().toString(),
                            etPassword.getText().toString(),etConfirmPassword.getText().toString(),etValidateCode.getText().toString(),getPackageName());
                    registerPresenter.postData();
                }else {
                    Toast.makeText(RegisteActivity.this,"两次密码不同",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_gotologin:
                this.finish();
                break;
            case R.id.tv_exit:
                this.finish();
                break;
            default:break;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
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

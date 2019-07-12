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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.BankPresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.GetMoneyPresenterImpl;

import com.example.qqlaobing.Umoney.presenter.presenterImpl.LoanMoneyNextPresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.LoanMoneyPresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.SelectProductPresenterImpl;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.JsonArray;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoanActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnLoan;
    private CheckBox checkBox;
    private TextView tvGetMoney,tvSureLoanMoney;
    private Spinner tvLoanTime;
    private ImageView ivCancel;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_GET_MONEY:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        Log.e("11111",resultCode);
                        if (resultCode.equals("401")){
                            NetRequestBusinessDefine.login=false;
                        }
                        if (resultCode.equals("1")) {
                            Intent intent = new Intent(LoanActivity.this,MainActivity.class);
                            startActivity(intent);
                            LoanActivity.this.finish();
                        } else {
                            Log.e("11111",response.getString("msg"));
                            Log.e("11111","1");
                            Toast.makeText(LoanActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_comfirmProduct:
                    dialog.close();
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        Log.e("11111",resultCode);
                        if (resultCode.equals("401")){
                            NetRequestBusinessDefine.login=false;
                        }
                        if (resultCode.equals("1")) {
                            Intent intent = new Intent(LoanActivity.this,LoanNextActivity.class);
                            startActivity(intent);
                            LoanActivity.this.finish();
                        } else {
                            Toast.makeText(LoanActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_selectProducts:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        Log.e("11111",resultCode);
                        if (resultCode.equals("401")){
                            NetRequestBusinessDefine.login=false;
                        }
                        if (resultCode.equals("1")) {
                            JSONObject data=response.getJSONObject("data");
                            JSONObject datas=data.getJSONObject("data");
                            JSONArray jsonArray=datas.getJSONArray("products");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                Log.e("zlz",jsonObject.getString("days").length()+"");
                                if (jsonObject.getString("days").length()<3){
//                                    tv_fenqi.setText(jsonObject.getString("amount"));
                                }else {
//                                    tv_danqi.setText(jsonObject.getString("amount"));
//                                    money=jsonObject.getString("amount");
                                    String days=jsonObject.getString("days");
                                    String str2=days.replace(" ", "");//去掉所用空格
                                    //数据
                                    product_id=jsonObject.getString("id");
                                    data_list = Arrays.asList(str2.split(","));
                                    day=data_list.get(0);
                                    //适配器
                                    arr_adapter = new ArrayAdapter<String>(LoanActivity.this, android.R.layout.simple_spinner_item, data_list);
                                    //设置样式
                                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //加载适配器
                                    tvLoanTime.setAdapter(arr_adapter);
                                    tvLoanTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            day=data_list.get(i);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                }
                            }
                        } else {
                            Log.e("11111",response.getString("msg"));
                            Log.e("11111","1");
                            Toast.makeText(LoanActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GET_BANK:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONObject bankinfo = data.getJSONObject("bankinfo");
                            tv_bank.setText(bankinfo.getString("name")+"("+"尾号"+bankinfo.getString("bankno").substring(bankinfo.getString("bankno").length()-4,bankinfo.getString("bankno").length())+")");
                        }else {
                            Toast.makeText(LoanActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    };
    private String day;
    private String money;
    private TextView tv_bank,tv_fenqi;
    private EditText tv_danqi;
    private TextView tv_agree;
    private String product_id,backid;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loan);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
//        Intent intent=getIntent();
//        day = intent.getStringExtra("day");
//        money = intent.getStringExtra("money");
        initView();
        initData();
    }

    private void initData() {
        SelectProductPresenterImpl selectProductPresenter=new SelectProductPresenterImpl(mHandler);
        selectProductPresenter.postData();
//        Log.e("eeeeee",day+"---"+money);
//        tvGetMoney.setText(money);
//        tvSureLoanMoney.setText(money);
//        tvLoanTime.setText(day);
        BankPresenterImpl bankPresenter=new BankPresenterImpl(mHandler);
        bankPresenter.getData();
//
        btnLoan.setOnClickListener(this);
//        ivCancel.setOnClickListener(this);
//        tv_agree.setOnClickListener(this);
//        btnLoan.setEnabled(false);
//        tvGetMoney.setText(money);
//        tvSureLoanMoney.setText(money);
//        tvLoanTime.setText(day);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b){
//                    btnLoan.setBackgroundResource(R.drawable.btn_allshap);
//                    btnLoan.setEnabled(true);
//                }
//                else {
//                    btnLoan.setBackgroundResource(R.drawable.btn_noselect);
//                    btnLoan.setEnabled(false);
//                }
//            }
//        });
    }

    private void initView() {
        tvGetMoney=(TextView)findViewById(R.id.tv_getmoney);
        tvLoanTime=(Spinner) findViewById(R.id.tv_loantime);
        tv_bank =(TextView)findViewById(R.id.tv_bank);
        tv_danqi =(EditText) findViewById(R.id.tv_danqi);
        tv_fenqi =(TextView)findViewById(R.id.tv_fenqi);
        tv_agree =(TextView)findViewById(R.id.tv_agree);
        checkBox=(CheckBox)findViewById(R.id.cb_checkbox);
        btnLoan=(Button)findViewById(R.id.btn_loan);
        ivCancel=(ImageView)findViewById(R.id.iv_cancel);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_loan:
                if (tv_danqi.getText().toString().equals("")||tv_bank.getText().toString().equals("")){
                    Toast.makeText(LoanActivity.this,"请完善信息",Toast.LENGTH_SHORT).show();
                }else {
                    dialog = new LoadingDialog(LoanActivity.this, "提交中...");
                    dialog.show();
                    LoanMoneyNextPresenterImpl loanMoneyNextPresenter=new LoanMoneyNextPresenterImpl(mHandler,NetRequestBusinessDefine.oderid,"",product_id);
                    loanMoneyNextPresenter.postData();
                }
//                GetMoneyPresenterImpl getMoneyPresenter=new GetMoneyPresenterImpl(mHandler,day,money,product_id);
//                getMoneyPresenter.postData();
                break;
            case R.id.iv_cancel:
                Intent intent = new Intent(LoanActivity.this,MainActivity.class);
                startActivity(intent);
                LoanActivity.this.finish();
                break;
            case R.id.tv_agree:
                Intent intent1 = new Intent(LoanActivity.this,AgreeWebActivity.class);
                startActivity(intent1);
                break;
            default:break;
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
            Intent intent = new Intent(LoanActivity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
            Log.e("aaaaaaaaaaaaaaa", "-------2");
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

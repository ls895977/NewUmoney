package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.BankBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.BankPresenterImpl;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.qiniu.android.utils.Json;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 添加银行卡
 * @author zlz
 */
public class AddBankCardActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_sure;
    private String[] spinnerString = null;
    private String card="";
    private List<String> backs;
    private Spinner spinner;
    private ImageView tv_exit;
    private EditText et_username,et_telephone,et_idcard,et_bankid;
    private ArrayAdapter<String> mArrayAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_ADDBACK:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        Log.e("eee","1111111");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
//                                JSONObject results = response.getJSONObject("data");
                            dialog.close();
                            Toast.makeText(AddBankCardActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                                setResult(3, intent);
                                AddBankCardActivity.this.finish();
                        }else {
                            Toast.makeText(AddBankCardActivity.this,response.getString("info"),Toast.LENGTH_LONG).show();
                            dialog.close();
                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GETBACK:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray bankinfo=data.getJSONArray("banks");
                            spinnerString=new String[bankinfo.length()];
                            for (int i=0;i<bankinfo.length();i++){
                                spinnerString[i]=bankinfo.getString(i);
                            }
                            Log.e("eee",spinnerString.toString());
                            mArrayAdapter=new ArrayAdapter<String>(AddBankCardActivity.this,R.layout.bg_spinner,spinnerString){
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    if (convertView == null){
//                    设置spinner展开的Item布局
                                        convertView = getLayoutInflater().inflate(R.layout.item_spinner, parent, false);
                                    }
                                    TextView spinnerText=(TextView)convertView.findViewById(R.id.spinner_textView);
                                    spinnerText.setText(getItem(position));
                                    return convertView;
                                }
                            };
                            spinner.setAdapter(mArrayAdapter);
//        spinner设置监听
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    card=spinnerString[position];
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }else {
                            Toast.makeText(AddBankCardActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_add_bank_card);
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
        bankPresenter.getBack();

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
//        et_bank=(EditText) findViewById(R.id.et_bank);
        spinner=(Spinner) findViewById(R.id.et_bank);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                if (card.equals("")||et_username.getText().toString().equals("")||
                        et_telephone.getText().toString().equals("")||et_idcard.getText().toString().equals("")||
                        et_bankid.getText().toString().equals("")){
                    Toast.makeText(this,"资料填写不完整",Toast.LENGTH_LONG).show();
                }else {
                    dialog = new LoadingDialog(AddBankCardActivity.this, "上传中...");
                    dialog.show();
                    BankBeen bankBeen=new BankBeen();
                    bankBeen.setTelephone(et_telephone.getText().toString());
                    bankBeen.setBankcard(et_bankid.getText().toString());
                    bankBeen.setBankname(card);
                    bankBeen.setIdcard(et_idcard.getText().toString());
                    bankBeen.setUsername(et_username.getText().toString());
                    bankBeen.setId(et_bankid.getText().toString());
                    BankPresenterImpl bankPresenter=new BankPresenterImpl(mHandler);
                    bankPresenter.addBack(bankBeen);
                }
                break;
            case R.id.tv_exit:
                    this.finish();
                break;
            default:break;
        }

    }
}

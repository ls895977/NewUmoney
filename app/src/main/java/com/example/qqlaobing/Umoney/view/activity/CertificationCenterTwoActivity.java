package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.CertificationAdapter;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.CenterPresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.HomePresenterImpl;
import com.example.qqlaobing.Umoney.view.fragment.ReListView;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证中心
 * @author zlz
 */
public class CertificationCenterTwoActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnGetAmount;
    private String authKey = "f932ff6d-6c73-4c31-9acf-b97575fc31ca";
    private String urlNotify = "http://211.149.180.198";
    private ReListView lvCertification;
    private int[] inco={R.mipmap.userinfo,R.mipmap.work,R.mipmap.card,R.mipmap.contact,
    R.mipmap.operator,R.mipmap.sesame,R.mipmap.taobao,R.mipmap.gongjijing,R.mipmap.shebao};
    private List<String> data1;
    private String[] list=new String[inco.length];
    private TextView tv_exit;
    private CertificationAdapter certificationAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_HOME:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("111","main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            Log.e("111",data.getString("authstatus"));
                            if (data.getString("authstatus").equals("1")) {
                                Log.e("111","main2");
                                Intent intent = new Intent(CertificationCenterTwoActivity.this, MainActivity.class);
                                Log.e("111","main");
                                NetRequestBusinessDefine.login = true;
                                startActivity(intent);
                                CertificationCenterTwoActivity.this.finish();
                            }else {
                                Log.e("111", "main");
                                Toast.makeText(CertificationCenterTwoActivity.this, "资料填写不完善", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GET_AUTHINFO:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            if (data.getString("cominfo").equals("1")) {
                                list[0]="已认证";
                            }else {
                                list[0]="未认证";
                            }
                            if (data.getString("gz").equals("1")) {
                                list[1]="已认证";
                            }else {
                                list[1]="未认证";
                            }
                            if (data.getString("bank").equals("1")) {
                                list[2]="已认证";
                            }else {
                                list[2]="未认证";
                            }
                            if (data.getString("contacts").equals("1")) {
                                list[3]="已认证";
                            }else {
                                list[3]="未认证";
                            }
                            if (data.getString("tmobile").equals("1")) {
                                list[4]="已认证";
                            }else {
                                list[4]="未认证";
                            }
                            if (data.getString("zmf").equals("1")) {
                                list[5]="已认证";
                            }else {
                                list[5]="未认证";
                            }
                            if (data.getString("taobao").equals("1")) {
                                list[6]="已认证";
                            }else {
                                list[6]="未认证";
                            }
                            if (data.getString("social").equals("1")) {
                                list[7]="已认证";
                            }else {
                                list[7]="未认证";
                            }
                            if (data.getString("fund").equals("1")) {
                                list[8]="已认证";
                            }else {
                                list[8]="未认证";
                            }
                            certificationAdapter = new CertificationAdapter(CertificationCenterTwoActivity.this,data1,inco,list);
                            lvCertification.setAdapter(certificationAdapter);
                            lvCertification.loadComplete();

                        } else {
                            lvCertification.loadComplete();
                            Toast.makeText(CertificationCenterTwoActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
            };
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_certification);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"));
        initview();
        initData();
    }

    private void initData() {
        btnGetAmount.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        data1=new ArrayList<>();
        data1.add("个人信息");
        data1.add("工作认证");
        data1.add("银行卡");
        data1.add("紧急联系人");
        data1.add("运营商信息");
        data1.add("芝麻信用");
        data1.add("淘宝认证");
        data1.add("公积金");
        data1.add("社保");
//        CenterPresenterImpl centerPresenter=new CenterPresenterImpl(mHandler);
//        centerPresenter.postData();
        /*data.add("支付宝认证");
        data.add("淘宝认证(可选)");*/
        lvCertification.setInterface(new ReListView.LoadListener() {
            @Override
            public void onLoad() {
                lvCertification.loadComplete();
            }

            @Override
            public void pullLoad() {
                Log.e("re","1111");
                CenterPresenterImpl centerPresenter=new CenterPresenterImpl(mHandler);
                centerPresenter.postData();
            }
        });
        lvCertification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i-1){
                    case 0:
                        String id = "demo_"+System.currentTimeMillis();
                        AuthBuilder mAuthBuilder = new AuthBuilder(id, authKey, urlNotify, new OnResultListener() {
                            @Override
                            public void onResult(String s) {
                                try {
                                    JSONObject jsonObject=new JSONObject(s);
                                    int code=jsonObject.getInt("ret_code");
                                    if (code==000000){
                                        Intent intent=new Intent(CertificationCenterTwoActivity.this,UserInfoActivity.class);
                                        intent.putExtra("sanfang",s);
                                        startActivityForResult(intent,1);
//                                        list[0]="已认证";
//                                        certificationAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("error",s);
                            }

                        });
                        mAuthBuilder.faceAuth(CertificationCenterTwoActivity.this);
                        break;
                    case 1:
                        startActivityForResult(new Intent(CertificationCenterTwoActivity.this,WorkCheckActivity.class),2);
                        break;
                    case 2:
                        startActivityForResult(new Intent(CertificationCenterTwoActivity.this,BankCardActivity.class),3);
                        break;
                    case 3:
                        startActivityForResult(new Intent(CertificationCenterTwoActivity.this,ContactActivity.class),4);
                        break;
                    case 4:
                        startActivityForResult(new Intent(CertificationCenterTwoActivity.this,WebOpeActivity.class),5);
                        break;
                    case 5:
                        startActivityForResult(new Intent(CertificationCenterTwoActivity.this,ZhiMaActivity.class),6);
                        break;
                    case 6:
                        Intent intent=new Intent(CertificationCenterTwoActivity.this,WebActivity.class);
                        url =NetRequestBusinessDefine.K_HOST_NAME+"/user/taobao?orderno="+"&token="+NetRequestBusinessDefine.K_TOKEN;
                        intent.putExtra("url", url);
                        startActivity(intent);
//                        startActivity(new Intent(CertificationCenterActivity.this,ZhiFuBaoActivity.class));
                        break;
                    case 7:
                        Intent intent1=new Intent(CertificationCenterTwoActivity.this,WebActivity.class);
                        url =NetRequestBusinessDefine.K_HOST_NAME+"/user/fund?orderno="+"&token="+NetRequestBusinessDefine.K_TOKEN;
                        intent1.putExtra("url", url);
                        startActivity(intent1);
//                        startActivity(new Intent(CertificationCenterActivity.this,BankCardActivity.class));
                        break;
                    case 8:
                        Intent intent2=new Intent(CertificationCenterTwoActivity.this,WebActivity.class);
                        url =NetRequestBusinessDefine.K_HOST_NAME+"/user/social?orderno="+"&token="+NetRequestBusinessDefine.K_TOKEN;
                        intent2.putExtra("url", url);
                        startActivity(intent2);
//                        startActivity(new Intent(CertificationCenterActivity.this,BankCardActivity.class));
                        break;
                    default:break;
                }
            }
        });
    }

    private void initview() {
        btnGetAmount=(Button)findViewById(R.id.btn_getamount);
        lvCertification=(ReListView) findViewById(R.id.lv_certification);
        tv_exit=(TextView) findViewById(R.id.tv_exit);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getamount:
                HomePresenterImpl homePresenter=new HomePresenterImpl(mHandler);
                homePresenter.getData();
                break;
            case R.id.tv_exit:
                Intent intent = new Intent(CertificationCenterTwoActivity.this, MainActivity.class);
                NetRequestBusinessDefine.login = true;
                startActivity(intent);
                CertificationCenterTwoActivity.this.finish();
//                GeneralApplication.getInstance().exitApplication();
                this.finish();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        switch (resultCode){
            case 1:
                    Log.e("xxxxxx",requestCode+"");
                        list[0]="已认证";
                        certificationAdapter.notifyDataSetChanged();
                break;
            case 2:
                    Log.e("xxxxxx",requestCode+"");
                    list[1]="已认证";
                    certificationAdapter.notifyDataSetChanged();
                break;
            case 3:
                    Log.e("xxxxxx",requestCode+"");
                    list[2]="已认证";
                    certificationAdapter.notifyDataSetChanged();
                break;
            case 4:
                    Log.e("xxxxxx",requestCode+"");
                    list[3]="已认证";
                    certificationAdapter.notifyDataSetChanged();
                break;
            case 5:
                CenterPresenterImpl centerPresenter=new CenterPresenterImpl(mHandler);
                centerPresenter.postData();
                break;
            case 6:
                    Log.e("xxxxxx",requestCode+"");
                    list[5]="已认证";
                certificationAdapter.notifyDataSetChanged();
                break;
            default:break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        CenterPresenterImpl centerPresenter=new CenterPresenterImpl(mHandler);
        centerPresenter.postData();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CertificationCenterTwoActivity.this, MainActivity.class);
        NetRequestBusinessDefine.login = true;
        startActivity(intent);
        CertificationCenterTwoActivity.this.finish();
    }
}

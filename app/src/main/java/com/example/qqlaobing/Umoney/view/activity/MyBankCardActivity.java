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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.MybackAdapter;
import com.example.qqlaobing.Umoney.been.BankBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.BankPresenterImpl;

import com.example.qqlaobing.Umoney.presenter.presenterImpl.CenterPresenterImpl;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBankCardActivity extends AppCompatActivity {
    private ListView lv_back;
    private List<BankBeen> bankBeens=new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_GETBACKLIST:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        bankBeens.clear();
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray bankinfo = data.getJSONArray("bankLists");
                            for (int i=0;i<bankinfo.length();i++){
                                JSONObject back=(JSONObject) bankinfo.get(i);
                                BankBeen bankBeen=new BankBeen();
                                bankBeen.setBankname(back.getString("bankname"));
                                bankBeen.setBankcard(back.getString("bankno"));
                                bankBeen.setImageurl(back.getString("image"));
                                bankBeen.setId(back.getString("id"));
                                bankBeens.add(bankBeen);
                            }
                            BankBeen bankBeen=new BankBeen();
                            bankBeens.add(bankBeen);
                            mybackAdapter = new MybackAdapter(MyBankCardActivity.this,bankBeens);
                            lv_back.setAdapter(mybackAdapter);
                            mybackAdapter.setOnArtClick(new MybackAdapter.setOnDeleteLisener() {
                                @Override
                                public void Click(String cardid, int i) {
                                    if (i==1){
                                        startActivityForResult(new Intent(MyBankCardActivity.this,AddBankCardActivity.class),1);
                                    }else {
                                        bankPresenter.deleteBack(cardid);
                                        bankPresenter.getBackLists();
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(MyBankCardActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_DELLETBACK:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject data = response.getJSONObject("data");
//                            JSONObject bankinfo = data.getJSONObject("bankinfo");
//                            String name=bankinfo.getString()
                            bankPresenter.getBackLists();
//                            mybackAdapter.notifyDataSetChanged();

                        }else {
                            Toast.makeText(MyBankCardActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:break;
            }
        }
    };
    private ImageView tv_exit;
    private BankPresenterImpl bankPresenter;
    private MybackAdapter mybackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_bank_card);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        lv_back= (ListView) findViewById(R.id.lv_back);
        tv_exit=(ImageView)findViewById(R.id.tv_exit);
        bankPresenter = new BankPresenterImpl(mHandler);
        bankPresenter.getBackLists();
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBankCardActivity.this.finish();
            }
        });
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        switch (resultCode){
            case 3:
                bankPresenter = new BankPresenterImpl(mHandler);
                bankPresenter.getBackLists();
                break;

            default:break;
        }
    }

}

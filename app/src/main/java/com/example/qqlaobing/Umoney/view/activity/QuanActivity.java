package com.example.qqlaobing.Umoney.view.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;


import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.QuanAdapter;
import com.example.qqlaobing.Umoney.been.QuanBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.QuanPresenterImpl;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuanActivity extends AppCompatActivity {
    private TextView tv_exit;
    private ListView lv_quan;
    private List<QuanBeen> list;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_GET_QUAN:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        JSONObject results = response.getJSONObject("data");
                        if (resultCode.equals("1")) {
                            JSONArray data=results.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                QuanBeen quanBeen=new QuanBeen();
                                quanBeen.setAmount(((JSONObject)data.get(i)).getString("amount"));
                                quanBeen.setStatus(((JSONObject)data.get(i)).getInt("status"));
                                quanBeen.setTimeto(((JSONObject)data.get(i)).getString("addtime"));
                                quanBeen.setTitle(((JSONObject)data.get(i)).getString("title"));
                                list.add(quanBeen);
                            }
                            QuanAdapter quanAdapter=new QuanAdapter(QuanActivity.this,list);
                            lv_quan.setAdapter(quanAdapter);
                        }
                        else {

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
        setContentView(R.layout.activity_quan);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        lv_quan=(ListView)findViewById(R.id.lv_quan);
        tv_exit=(TextView)findViewById(R.id.tv_exit);
        list=new ArrayList<>();

        QuanPresenterImpl quanPresenter=new QuanPresenterImpl(mHandler);
        quanPresenter.postData();
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuanActivity.this.finish();
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
}

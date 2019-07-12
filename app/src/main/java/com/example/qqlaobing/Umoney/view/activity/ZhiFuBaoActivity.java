package com.example.qqlaobing.Umoney.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.example.qqlaobing.Umoney.R;
import com.umeng.analytics.MobclickAgent;

public class ZhiFuBaoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_fu_bao);
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
    }

    @Override
    public void onClick(View view) {

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

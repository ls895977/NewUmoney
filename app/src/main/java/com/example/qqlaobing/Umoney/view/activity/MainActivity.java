package com.example.qqlaobing.Umoney.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.FragmentAdapter;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.HomePresenterImpl;
import com.example.qqlaobing.Umoney.view.fragment.FindFragment;
import com.example.qqlaobing.Umoney.view.fragment.MyFragment;
import com.example.qqlaobing.Umoney.view.fragment.MyLoanFragment;
import com.example.qqlaobing.Umoney.view.myview.MyViewPager;

import com.example.qqlaobing.Umoney.view.fragment.HomeFragment;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private long mExitTime=0;
    private MyViewPager mvpMain;
    private LinearLayout llHome,llMyLoan,llMy,llfind;
    private ImageView ivHome,ivMyLoan,ivMy,ivfind;
    private TextView tvHome,tvMyLoan,tvMy,tvfind;
    private List<Fragment> list;
    private FragmentAdapter fragmentAdapter;
    private HomeFragment homeFragment;
    private MyLoanFragment myLoanFragment;
    private LoaningActivity loaningActivity;
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
                        Log.e("111", "main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("401")){
                            NetRequestBusinessDefine.login=false;
                            break;
                        }
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            if (!data.getString("loan").equals("null")) {
                                NetRequestBusinessDefine.loan = true;
                                if (list.size()!=0){
                                    list.remove(0);
                                }
                                list.add(0,loaningActivity);
                            } else {
                                if (list.size()!=0){
                                    list.remove(0);
                                }
                                list.add(0,homeFragment);
                                Log.e("111", "main2");
                                NetRequestBusinessDefine.loan = false;
                                NetRequestBusinessDefine.login = true;
                            }
                        } else {
                            Toast.makeText(MainActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    };
    private FindFragment findFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        SharedPreferences share=getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (!share.getString("token","").equals("")){
            Log.e("eeee","12343");
            NetRequestBusinessDefine.login=true;
            NetRequestBusinessDefine.K_TOKEN=share.getString("token","");
            Log.e("eeee",share.getString("token",""));
            HomePresenterImpl homePresenter=new HomePresenterImpl(mHandler);
            homePresenter.getData();
        }
        initView();
        initData();
    }

    private void initData() {
        Log.e("eee","oncreate");
        list = new ArrayList<Fragment>();
        homeFragment = new HomeFragment();
        myLoanFragment = new MyLoanFragment();
        loaningActivity = new LoaningActivity();
        findFragment = new FindFragment();
        if (NetRequestBusinessDefine.loan){
            if (list.size()!=0){
                list.remove(0);
            }
            list.add(0,loaningActivity);
        }else {
            if (list.size()!=0){
                list.remove(0);
            }
            list.add(0,homeFragment);
        }
        list.add(1,myLoanFragment);
        list.add(2,new MyFragment());
        list.add(3,findFragment);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), list);
        mvpMain.setAdapter(fragmentAdapter);
        mvpMain.setCurrentItem(0);
        mvpMain.setNoScroll(true);
        ivHome.setImageResource(R.mipmap.homeselect);
        tvHome.setTextColor(Color.parseColor("#333333"));

        llHome.setOnClickListener(this);
        llMyLoan.setOnClickListener(this);
        llMy.setOnClickListener(this);
        llfind.setOnClickListener(this);

    }

    private void initView() {
        mvpMain=(MyViewPager)findViewById(R.id.mvp_main);
        llHome=(LinearLayout)findViewById(R.id.ll_home);
        llMy=(LinearLayout)findViewById(R.id.ll_my);
        llMyLoan=(LinearLayout)findViewById(R.id.ll_myloan);
        llfind=(LinearLayout)findViewById(R.id.ll_find);

        ivHome=(ImageView)findViewById(R.id.iv_home);
        ivMy=(ImageView)findViewById(R.id.iv_my);
        ivMyLoan=(ImageView)findViewById(R.id.iv_myloan);
        ivfind=(ImageView)findViewById(R.id.iv_find);

        tvHome=(TextView)findViewById(R.id.tv_home);
        tvMy=(TextView)findViewById(R.id.tv_my);
        tvMyLoan=(TextView)findViewById(R.id.tv_myloan);
        tvfind=(TextView)findViewById(R.id.tv_find);

    }

    public void gotoDownloadFragment() {
        fragmentAdapter.notifyDataSetChanged();//去贷款页面
        mvpMain.setCurrentItem(1);
        ivHome.setImageResource(R.mipmap.home);
        tvHome.setTextColor(Color.parseColor("#9b9b9b"));
        ivMyLoan.setImageResource(R.mipmap.myloanselect);
        tvMyLoan.setTextColor(Color.parseColor("#333333"));
        ivMy.setImageResource(R.mipmap.my);
        tvMy.setTextColor(Color.parseColor("#9b9b9b"));
        ivfind.setImageResource(R.mipmap.find_noselect);
        tvfind.setTextColor(Color.parseColor("#9b9b9b"));
    }
    public void gotoHomeFragment() {
        Log.e("eeee","false1111111");
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();//关闭自己//去贷款页面
        /*if (list.size()!=0){
            list.remove(0);
        }
        list.add(0,homeFragment);
        fragmentAdapter.notifyDataSetChanged();
        mvpMain.setCurrentItem(0);
        ivHome.setImageResource(R.mipmap.homeselect);
        tvHome.setTextColor(Color.parseColor("#333333"));
        ivMyLoan.setImageResource(R.mipmap.myloan);
        tvMyLoan.setTextColor(Color.parseColor("#9b9b9b"));
        ivMy.setImageResource(R.mipmap.my);
        tvMy.setTextColor(Color.parseColor("#9b9b9b"));*/
    }
    public void goTOLoan(){
        Intent intent=new Intent(this, LoanActivity.class);
//        intent.putExtra("day",day);
//        intent.putExtra("money",money+"");
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        Log.e("code",requestCode+"---"+resultCode);
        switch (resultCode) {
            case 12:
                if (list.size()!=0){
                    list.remove(0);
                }
                list.add(0,loaningActivity);
                fragmentAdapter.notifyDataSetChanged();
                mvpMain.setCurrentItem(0);
                ivHome.setImageResource(R.mipmap.homeselect);
                tvHome.setTextColor(Color.parseColor("#333333"));
                ivfind.setImageResource(R.mipmap.find_noselect);
                tvfind.setTextColor(Color.parseColor("#9b9b9b"));
                ivMyLoan.setImageResource(R.mipmap.myloan);
                tvMyLoan.setTextColor(Color.parseColor("#9b9b9b"));
                ivMy.setImageResource(R.mipmap.my);
                tvMy.setTextColor(Color.parseColor("#9b9b9b"));
                break;
            default:break;
        }
    }
    @Override
    public void onClick(View view) {
        Log.e("eeee","false11111");
        switch (view.getId()){
            case R.id.ll_home:
                if (!NetRequestBusinessDefine.login){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    this.finish();
                    break;
                }
                if (NetRequestBusinessDefine.loan){
                    if (list.size()!=0){
                        list.remove(0);
                    }
                    list.add(0,loaningActivity);
                    fragmentAdapter.notifyDataSetChanged();
                }else {
                    if (list.size()!=0){
                        list.remove(0);
                    }
                    Log.e("eeee","false");
                    list.add(0,homeFragment);
                    fragmentAdapter.notifyDataSetChanged();
                }
                mvpMain.setCurrentItem(0);
                ivHome.setImageResource(R.mipmap.homeselect);
                tvHome.setTextColor(Color.parseColor("#333333"));
                ivMyLoan.setImageResource(R.mipmap.myloan);
                tvMyLoan.setTextColor(Color.parseColor("#9b9b9b"));
                ivMy.setImageResource(R.mipmap.my);
                tvMy.setTextColor(Color.parseColor("#9b9b9b"));
                ivfind.setImageResource(R.mipmap.find_noselect);
                tvfind.setTextColor(Color.parseColor("#9b9b9b"));
                break;
            case R.id.ll_myloan:
                if (!NetRequestBusinessDefine.login){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    this.finish();
                    break;
                }
//                fragmentAdapter.notifyDataSetChanged();
                mvpMain.setCurrentItem(1);
                myLoanFragment.upData();
                ivHome.setImageResource(R.mipmap.home);
                tvHome.setTextColor(Color.parseColor("#9b9b9b"));
                ivMyLoan.setImageResource(R.mipmap.myloanselect);
                tvMyLoan.setTextColor(Color.parseColor("#333333"));
                ivMy.setImageResource(R.mipmap.my);
                tvMy.setTextColor(Color.parseColor("#9b9b9b"));
                ivfind.setImageResource(R.mipmap.find_noselect);
                tvfind.setTextColor(Color.parseColor("#9b9b9b"));
                break;
            case R.id.ll_my:
                if (!NetRequestBusinessDefine.login){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    this.finish();
                    break;
                }
//                fragmentAdapter.notifyDataSetChanged();
                mvpMain.setCurrentItem(2);
                ivHome.setImageResource(R.mipmap.home);
                tvHome.setTextColor(Color.parseColor("#9b9b9b"));
                ivMyLoan.setImageResource(R.mipmap.myloan);
                tvMyLoan.setTextColor(Color.parseColor("#9b9b9b"));
                ivMy.setImageResource(R.mipmap.myselect);
                tvMy.setTextColor(Color.parseColor("#333333"));
                ivfind.setImageResource(R.mipmap.find_noselect);
                tvfind.setTextColor(Color.parseColor("#9b9b9b"));
                break;
            case R.id.ll_find:
                if (!NetRequestBusinessDefine.login){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    this.finish();
                    break;
                }
//                fragmentAdapter.notifyDataSetChanged();
                mvpMain.setCurrentItem(3);
                ivHome.setImageResource(R.mipmap.home);
                tvHome.setTextColor(Color.parseColor("#9b9b9b"));
                ivMyLoan.setImageResource(R.mipmap.myloan);
                tvMyLoan.setTextColor(Color.parseColor("#9b9b9b"));
                ivMy.setImageResource(R.mipmap.my);
                tvMy.setTextColor(Color.parseColor("#9b9b9b"));
                ivfind.setImageResource(R.mipmap.find_select);
                tvfind.setTextColor(Color.parseColor("#333333"));
                break;
            default:break;
        }
    }
 /*   @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Log.e("sssssssssssssssss", (System.currentTimeMillis() - mExitTime) + "----------" + System.currentTimeMillis() + "------------" + mExitTime);
        if ((System.currentTimeMillis() - mExitTime) < 3000) {
//            super.onBackPressed();
            this.finish();
            System.exit(0);
        } else {
            Log.e("aaaaaaaaaaaaaaa", "-------1");
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            *//*Log.e("aaaaaaaaaaaaaaa", "-------2");
            this.finish();
            System.exit(0);*//*
        }
    }*/
 private long exitTime = 0;
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
                 if (keyCode == KeyEvent.KEYCODE_BACK
                         && event.getAction() == KeyEvent.ACTION_DOWN) {
                         if ((System.currentTimeMillis() - exitTime) > 2000) {
                             Log.e("aaaaaaaaaaaaaaa", "-------1");
                                 //弹出提示，可以有多种方式
                                 Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                                 exitTime = System.currentTimeMillis();
                             } else {
                             Log.e("aaaaaaaaaaaaaaa", "-------2");
                                 MainActivity.this.finish();
                            }
                        return true;
                     }
                 return super.onKeyDown(keyCode, event);
             }


    @Override
    protected void onDestroy() {
        Log.e("eee","deestroy");
        getSupportFragmentManager().beginTransaction().remove(homeFragment);
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("eee","re");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("eee","pa");
        MobclickAgent.onPause(this);
    }
}

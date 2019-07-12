package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.view.fragment.RenZhengBackFragment;
import com.example.qqlaobing.Umoney.view.fragment.RenZhengContectFragment;
import com.example.qqlaobing.Umoney.view.fragment.RenZhengUserinfoFragment;
import com.example.qqlaobing.Umoney.view.fragment.RenZhengWebOpeFragment;
import com.example.qqlaobing.Umoney.view.fragment.RenZhengWorkFragment;
import com.example.qqlaobing.Umoney.view.fragment.RenZhengZhiMaFragment;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenZhengActivity extends AppCompatActivity {

    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_one)
    ImageView ivOne;
    @BindView(R.id.iv_two)
    ImageView ivTwo;
    @BindView(R.id.iv_three)
    ImageView ivThree;
    @BindView(R.id.iv_four)
    ImageView ivFour;
    @BindView(R.id.iv_five)
    ImageView ivFive;
    @BindView(R.id.iv_six)
    ImageView ivSix;
    @BindView(R.id.fl_fragment)
    FrameLayout flFragment;
    private FragmentManager supportFragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RenZhengUserinfoFragment renZhengUserinfoFragment;
    private RenZhengContectFragment renZhengContectFragment;
    private RenZhengWorkFragment renZhengWorkFragment;
    private RenZhengWebOpeFragment renZhengWebOpeFragment;
    private RenZhengZhiMaFragment renZhengZhiMaFragment;
    private RenZhengBackFragment renZhengBackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        setContentView(R.layout.activity_ren_zheng);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //获取管理者
        supportFragmentManager = getSupportFragmentManager();
        //开启事务
        fragmentTransaction = supportFragmentManager.beginTransaction();
        ChangeFragment(NetRequestBusinessDefine.STEP);
    }

    @OnClick(R.id.iv_cancel)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        NetRequestBusinessDefine.login = true;
        startActivity(intent);
        this.finish();
    }

    public void ChangeFragment(int page){
        Log.e("zlz","==="+page);
        hideFrag();
        //开启事务
        fragmentTransaction = supportFragmentManager.beginTransaction();
        switch (page){
            case 1 :
                tvTitle.setText("个人信息");
                ivOne.setBackgroundResource(R.mipmap.one_select);
            if (renZhengUserinfoFragment == null) {
                //实例化fragment2
                renZhengUserinfoFragment = new RenZhengUserinfoFragment();
                fragmentTransaction.add(R.id.fl_fragment, renZhengUserinfoFragment).commit();
            } else {
                //有的话就显示
                fragmentTransaction.show(renZhengUserinfoFragment).commit();
            }
            break;
            case 2:
                tvTitle.setText("收款银行卡");
                ivTwo.setBackgroundResource(R.mipmap.two_select);
                if (renZhengBackFragment == null) {
                    //实例化fragment2
                    renZhengBackFragment = new RenZhengBackFragment();
                    fragmentTransaction.add(R.id.fl_fragment, renZhengBackFragment).commit();
                } else {
                    //有的话就显示
                    fragmentTransaction.show(renZhengBackFragment).commit();
                }
                break;

            case 3:
                tvTitle.setText("紧急联系人");
                ivThree.setBackgroundResource(R.mipmap.three_select);
                if (renZhengContectFragment == null) {
                    //实例化fragment2
                    renZhengContectFragment = new RenZhengContectFragment();
                    fragmentTransaction.add(R.id.fl_fragment, renZhengContectFragment).commit();
                } else {
                    //有的话就显示
                    fragmentTransaction.show(renZhengContectFragment).commit();
                }
                break;
            case 4:
                tvTitle.setText("运营商");
                ivFour.setBackgroundResource(R.mipmap.four_select);
                if (renZhengWebOpeFragment == null) {
                    //实例化fragment2
                    renZhengWebOpeFragment = new RenZhengWebOpeFragment();
                    fragmentTransaction.add(R.id.fl_fragment, renZhengWebOpeFragment).commit();
                } else {
                    //有的话就显示
                    fragmentTransaction.show(renZhengWebOpeFragment).commit();
                }
                break;
            case 5:
                tvTitle.setText("工作认证");
                ivFive.setBackgroundResource(R.mipmap.five_select);
                if (renZhengWorkFragment == null) {
                    //实例化fragment2
                    renZhengWorkFragment = new RenZhengWorkFragment();
                    fragmentTransaction.add(R.id.fl_fragment, renZhengWorkFragment).commit();
                } else {
                    //有的话就显示
                    fragmentTransaction.show(renZhengWorkFragment).commit();
                }
                break;
            case 6:
                tvTitle.setText("芝麻分认证");
                ivSix.setBackgroundResource(R.mipmap.six_select);
                if (renZhengZhiMaFragment == null) {
                    //实例化fragment2
                    renZhengZhiMaFragment = new RenZhengZhiMaFragment();
                    fragmentTransaction.add(R.id.fl_fragment, renZhengZhiMaFragment).commit();
                } else {
                    //有的话就显示
                    fragmentTransaction.show(renZhengZhiMaFragment).commit();
                }
                break;

        }
    }
    private void hideFrag() {
        //再重新获取一个开启事务
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        //不等于空或者是否添加的时候
        if (renZhengBackFragment != null && renZhengBackFragment.isAdded()) {
            fragmentTransaction.hide(renZhengBackFragment);
        }
        if (renZhengContectFragment != null && renZhengContectFragment.isAdded()) {
            fragmentTransaction.hide(renZhengContectFragment);
        }
        if (renZhengUserinfoFragment != null && renZhengUserinfoFragment.isAdded()) {
            fragmentTransaction.hide(renZhengUserinfoFragment);
        }
        if (renZhengWebOpeFragment != null && renZhengWebOpeFragment.isAdded()) {
            fragmentTransaction.hide(renZhengWebOpeFragment);
        }
        if (renZhengWorkFragment != null && renZhengWorkFragment.isAdded()) {
            fragmentTransaction.hide(renZhengWorkFragment);
        }
        if (renZhengZhiMaFragment != null && renZhengZhiMaFragment.isAdded()) {
            fragmentTransaction.hide(renZhengZhiMaFragment);
        }

        fragmentTransaction.commit();
        ivSix.setBackgroundResource(R.mipmap.six);
        ivFive.setBackgroundResource(R.mipmap.five);
        ivFour.setBackgroundResource(R.mipmap.four);
        ivThree.setBackgroundResource(R.mipmap.three_renzheng);
        ivTwo.setBackgroundResource(R.mipmap.two_rengzheng);
        ivOne.setBackgroundResource(R.mipmap.one_renzheng);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        NetRequestBusinessDefine.login = true;
        startActivity(intent);
        this.finish();
    }
}

package com.example.qqlaobing.Umoney.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.AdAdapter;
import com.example.qqlaobing.Umoney.view.myview.PageIndicator;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends AppCompatActivity {
    private List<View> myview;
    private ViewPager ad_viewpager;
    private LinearLayout dot_horizontal;
    private Button btn_go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        SharedPreferences share = getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (!share.getString("username", "").equals("")) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            StartActivity.this.finish();
        } else {
            dot_horizontal = (LinearLayout) findViewById(R.id.dot_horizontal);
            ad_viewpager = (ViewPager) findViewById(R.id.ad_viewpager);
            btn_go = (Button) findViewById(R.id.btn_go);
            btn_go.setVisibility(View.GONE);
            myview = new ArrayList<>();
            View view = LayoutInflater.from(this).inflate(R.layout.start_one, null);
            View view1 = LayoutInflater.from(this).inflate(R.layout.start_two, null);
            View view2 = LayoutInflater.from(this).inflate(R.layout.start_three, null);
            myview.add(view);
            myview.add(view1);
            myview.add(view2);
            AdAdapter adAdapter = new AdAdapter(this, myview);
            ad_viewpager.setAdapter(adAdapter);
            dot_horizontal.removeAllViews();
            ad_viewpager.addOnPageChangeListener(new PageIndicator(this, dot_horizontal, myview.size()));
            ad_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 2) {
                        btn_go.setVisibility(View.VISIBLE);
                    } else {
                        btn_go.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            btn_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    StartActivity.this.finish();
                }
            });
        }
    }

}

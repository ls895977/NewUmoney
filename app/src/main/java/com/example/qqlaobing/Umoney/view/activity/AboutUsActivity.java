package com.example.qqlaobing.Umoney.view.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;


import com.example.qqlaobing.Umoney.R;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

public class AboutUsActivity extends AppCompatActivity {
    private ImageView tv_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        tv_exit=(ImageView)findViewById(R.id.tv_exit);
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutUsActivity.this.finish();
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

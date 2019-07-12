package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;

import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class WebOpeActivity extends AppCompatActivity {
    private WebView webView;
    private String orderno="D-180725CAF1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_ope);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        webView=(WebView)findViewById(R.id.web);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView=(WebView)findViewById(R.id.web);
        Map<String, String > map = new HashMap<String, String>() ;
                     map.put( "token" , NetRequestBusinessDefine.K_TOKEN ) ;

        webView.loadUrl(NetRequestBusinessDefine.K_HOST_NAME+"/public/tmobile?token="+NetRequestBusinessDefine.K_TOKEN);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        //通过intent对象返回结果，必须要调用一个setResult方法，
        //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
        setResult(5, intent);
        WebOpeActivity.this.finish();
    }
    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        //通过intent对象返回结果，必须要调用一个setResult方法，
        //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
        setResult(5, intent);
        WebOpeActivity.this.finish();
        super.onDestroy();
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

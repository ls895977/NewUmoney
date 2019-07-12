package com.example.qqlaobing.Umoney.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class KeFuActivity extends AppCompatActivity {
    private WebView webView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_fu);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        webView=(WebView)findViewById(R.id.web);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView=(WebView)findViewById(R.id.web);
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "token" , NetRequestBusinessDefine.K_TOKEN ) ;

        webView.loadUrl(NetRequestBusinessDefine.K_HOST_NAME+"/public/tmobile?token="+NetRequestBusinessDefine.K_TOKEN);
        SharedPreferences share = getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (share != null) {
            name = share.getString("username", "");
        }
        webView.loadUrl("http://ms.kakulu.cn/index/index/chat?group=1&id="+NetRequestBusinessDefine.K_TOKEN+"&name="+name+"&avatar=http://wx2.sinaimg.cn/mw690/5db11ff4gy1flxmew7edlj203d03wt8n.jpg");
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

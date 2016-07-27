package com.wangyi.news.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyi.news.R;
import com.wangyi.news.main.IndexActivity;
import com.wangyi.news.splash.bean.ActionInfo;
import com.wangyi.news.utils.IntentUtils;


/**
 * @author 熊亦涛
 * @time 16/6/23  19:41
 * @desc ${TODD}
 */
public class WebViewActivity extends Activity implements View.OnClickListener {
    private WebView webview;
    private ImageView back;
    private String url;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webview = (WebView) findViewById(R.id.webview);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            ActionInfo action = (ActionInfo) intent.getSerializableExtra(SplashActivity.ACTION_NAME);
            url = action.getLink_url();
            webview.loadUrl(url);
        }
        //支持js
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override   //防止跳到系统浏览器
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);

                return true;
            }

            @Override   //开始加载
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override   //加载结束
            public void onPageFinished(WebView view, String url) {
                //这里有可能获取不到标题
                String pageTitle = view.getTitle();
                title.setText(pageTitle);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override   //获取标题
            public void onReceivedTitle(WebView view, String pageTitle) {
                title.setText(pageTitle);
            }

            @Override   //允许JS弹出框
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                IntentUtils.startActivityAndFinish(WebViewActivity.this, IndexActivity.class);
                break;
        }
    }
}

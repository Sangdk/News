package com.t3h.newsproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();

        webView = findViewById(R.id.web_view);
        Intent intent = getIntent();
        String url = intent.getStringExtra(Const.EXTRA_URL);
        String path = intent.getStringExtra(Const.EXTRA_PATH);
        if (url != null) {
            webView.loadUrl(url);
            Log.d("WebView","load: "+url);
        } else {
            webView.loadUrl("file://" + path);
            Log.d("WebView","load: "+path);

        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }
        });
    }

}

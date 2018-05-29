package com.shallowan.milutv.webview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shallowan.milutv.R;

import static com.shallowan.milutv.R.id.webview;

/**
 * Created by ShallowAn.
 */

public class WebViewActivity extends AppCompatActivity {
    private Toolbar mWVTitlebar;
    private WebView mWebView;

    private String mUrl = "file:///android_asset/milu.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        findAllViews();
        setTitleBar();
        openWebPage();
    }

    private void setTitleBar() {
        mWVTitlebar.setTitle("麋鹿直播用户服务协议");
        mWVTitlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mWVTitlebar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWVTitlebar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openWebPage() {
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void findAllViews() {
        mWVTitlebar = (Toolbar) findViewById(R.id.wvtitlebar);
        mWebView = (WebView) findViewById(R.id.webview);
    }

}

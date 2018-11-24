package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hackerkernel.user.sqrfactor.R;

public class BrowserActivity extends AppCompatActivity {

    private String url, title;
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Toolbar toolbar = findViewById(R.id.toolbar);
        url = getIntent().getStringExtra("url");

        Log.v("urlAtBrowser",url);
        Toast.makeText(this,url,Toast.LENGTH_LONG).show();

        title = getIntent().getStringExtra("title");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);


        if (TextUtils.isEmpty(url)) {
            finish();
        }

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        webView.loadUrl(url);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString) {
                if (paramAnonymousString.endsWith(".pdf")) {
                    BrowserActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString)));
                    return true;
                }
                if ((paramAnonymousString != null) && (paramAnonymousString.startsWith("whatsapp://"))) {
                    paramAnonymousWebView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString)));
                    return true;
                }

                return false;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);


        initWebView();


    }

    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.endsWith(".pdf")) {
                    BrowserActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                }
                if ((url != null) && (url.startsWith("whatsapp://"))) {
                    view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                } else {
                    webView.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        private MyWebChromeClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            if (Uri.parse(paramString).getHost().equals(url)) {
                return false;
            }
            Intent i = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
            BrowserActivity.this.startActivity(i);
            return true;
        }

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}


package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hackerkernel.user.sqrfactor.R;

public class W extends AppCompatActivity {
    private String postUrl;
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    Handler handler = new Handler();
    Bundle bundle = new Bundle();
    String link, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bundle = getIntent().getExtras();

        link = bundle.getString("link");
        title = bundle.getString("title");

        toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView.loadUrl(link);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString) {
                if (paramAnonymousString.endsWith(".pdf")) {
                    W.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString)));
                    return true;
                }
                if ((paramAnonymousString != null) && (paramAnonymousString.startsWith("whatsapp://"))) {
                    paramAnonymousWebView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramAnonymousString)));
                    return true;
                }

                return true;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);


        if (!TextUtils.isEmpty(getIntent().getStringExtra("link"))) {
            postUrl = getIntent().getStringExtra("link");
        }

        initWebView();
        renderPost();
    }

    private void initWebView() {
        webView.setWebViewClient(new MyWebViewClient());
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
                    W.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                }
                if ((url != null) && (url.startsWith("whatsapp://"))) {
                    view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                } else {
                    openInAppBrowser(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
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

    private void renderPost() {
        webView.loadUrl(postUrl);
    }

    private void openInAppBrowser(String url) {
        Intent intent = new Intent(W.this, BrowserActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private class MyWebViewClient extends WebViewClient {

        private MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            if (Uri.parse(paramString).getHost().equals(link)) {
                return false;
            }
            Intent i = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
            W.this.startActivity(i);
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        private MyWebChromeClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            if (Uri.parse(paramString).getHost().equals(link)) {
                return false;
            }
            Intent i = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
            W.this.startActivity(i);
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
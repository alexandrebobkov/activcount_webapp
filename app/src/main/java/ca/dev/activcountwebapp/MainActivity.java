/**
 *
 *  Date Created:       August 30, 2020
 *  Last time updated:  September 7, 2020
 *  Revision:
 *
 *  Author: Alexandre Bobkov
 *  Company: Alexandre Comptabilite Specialise Ltee.
 *
 *  Program purpose: To display mobile website within app.
 *
 **/
package ca.dev.activcountwebapp;
//import android.annotation.SuppressLint;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
//import android.os.Handler;
//import android.view.MotionEvent;
//import android.view.View;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.web_view = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        //web_view.setWebViewClient(webViewClient);
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Load mobile website.
        web_view.loadUrl("https://mobile.activcount.ca");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.web_view.canGoBack()) {
            this.web_view.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
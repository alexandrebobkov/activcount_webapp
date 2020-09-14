/*
 * MIT License
 *
 * Copyright (c) 2020 Alexandre Comptabilite Specialise Ltee
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 *
 *  Date Created:        August 30, 2020
 *  Last time updated:   September 7, 2020
 *  Revision:
 *
 *  Author:              Alexandre Bobkov
 *  Company:             Alexandre Comptabilite Specialise Ltee.
 *
 *  Program description: application with launcher widget.
 *
 */

package ca.dev.activcountwebapp;
import android.os.Bundle;
import android.app.Activity;
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
        // Enable navigation keys
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.web_view.canGoBack()) {
            this.web_view.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

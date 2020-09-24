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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private WebView web_view;

    private static final String PROJECT_ID = "<YOUR-PROJECT-ID>";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    private static final String TITLE = "FCM Notification";
    private static final String BODY = "Notification from FCM";
    public static final String MESSAGE_KEY = "message";

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
        //web_view.loadUrl("https://mobile.activcount.ca");
        web_view.loadUrl("https://www.activcount.ca");

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras.
        // Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
                // navigate the app based on param
            }
        }
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

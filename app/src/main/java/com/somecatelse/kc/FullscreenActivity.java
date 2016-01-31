package com.somecatelse.kc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class FullscreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }
    // 共享文字处理
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            WebView t = (WebView)findViewById(R.id.webView);
            //t.loadData(getString(R.string.full_screen_loading), "text/html", null);
            // 画面選択時カンマとドットが選ばれるが、検索できないため削除する
            String s = sharedText.trim().replaceAll("\\.", "").replaceAll(",", "");
            t.loadUrl("http://dict.baidu.com/s?wd=" + s);
        }
    }
}

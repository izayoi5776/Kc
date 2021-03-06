package com.somecatelse.kc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.content.Context;

public class FullscreenActivity extends Activity {
    private String mShareText;  // 传递来的单词
    private DBHelper mDBHelper; // 保存历史用数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // debug start 显示保存的内容
        SharedPreferences settings = getSharedPreferences(getString(R.string.save_file_name), MODE_PRIVATE);
        Map<String,?> m = settings.getAll();
        if(m != null){
            Log.d("fullscreen", m.toString());
        }
        // debug end

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

    /* 查询信息保存
       注：本来信息保存应该写在onPause()中，因为在紧急情况下此事件
            可能被忽略不调用。但此程序查询信息仅用于统计目的，没有
            很高的正确性要求，反而是在容易发生资源不足的环境中更需要
            减少系统负担，所以选择放在此处。
     */
    @Override
/*    protected void onDestroy() {
        super.onDestroy();
        if(!mShareText.isEmpty()){
            String key = mShareText;
            SharedPreferences settings = getSharedPreferences(getString(R.string.save_file_name), MODE_PRIVATE);
            long hitCount = settings.getLong(key, 0L) + 1;
            Set<String> ks =new HashSet<>();
            ks.add(String.valueOf(hitCount));
            ks.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            SharedPreferences.Editor editor = settings.edit();
            //editor.putLong(key, hitCount);
            editor.putStringSet(key, ks);
            editor.apply();
        }
    }*/
    protected void onDestroy() {
        super.onDestroy();
        if(!mShareText.isEmpty()){
            String key = mShareText;
            SQLiteDatabase db = new DBHelper(getApplicationContext()).getWritableDatabase();
            // select cnt from history where key='$key';
            Cursor c = db.query("history", new String[]{"cnt"}, "word=?", new String[]{key}, null, null, null);
            boolean exists = c.getCount() > 0;
            int hitCount = 1;
            if(exists){
                c.moveToFirst();
                hitCount += c.getInt(0);
            }
            c.close();
            String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // insert into table values($key, $count, $date)
            ContentValues values = new ContentValues();
            values.put("cnt", hitCount);
            values.put("lastupdated", dt);
            if(exists){
                db.update("history", values, "word=?", new String[]{key});
            }else{
                values.put("word", key);
                db.insert("history", null, values);
            }
            db.close();
            //SharedPreferences settings = getSharedPreferences(getString(R.string.save_file_name), MODE_PRIVATE);
            //long hitCount = settings.getLong(key, 0L) + 1;
            //Set<String> ks =new HashSet<>();
            //ks.add(String.valueOf(hitCount));
            //ks.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //SharedPreferences.Editor editor = settings.edit();
            //editor.putLong(key, hitCount);
            //editor.putStringSet(key, ks);
            //editor.apply();
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
            String s = sharedText.trim().replaceAll("\\.\"()", "").replaceAll(",", "");
            t.loadUrl("http://dict.baidu.com/s?wd=" + s);
            mShareText=s;
        }
    }
}

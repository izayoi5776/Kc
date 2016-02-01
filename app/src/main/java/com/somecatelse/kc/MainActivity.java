package com.somecatelse.kc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button:
                //実行したいコード
                Intent intent = new Intent(this, ShowHistory.class);
                startActivity(intent);
                break;
        }
    }
}

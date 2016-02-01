package com.somecatelse.kc;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Map;

public class ShowHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        ArrayList<String> list = new ArrayList<>();
        SharedPreferences settings = getSharedPreferences(getString(R.string.save_file_name), MODE_PRIVATE);
        Map<String,?> m = settings.getAll();
        if(m != null){
            Log.d("preferences list", m.toString());
            for (Map.Entry<String, ?> entry :m.entrySet())
            {
                list.add(entry.getKey());
                list.add(entry.getValue().toString());
                //list.add("-");
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),android.R.layout.simple_list_item_1 , list);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }
}

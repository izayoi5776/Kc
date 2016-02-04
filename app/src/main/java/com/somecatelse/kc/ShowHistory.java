package com.somecatelse.kc;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ShowHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        ArrayList<String> list = new ArrayList<>();
        /*SharedPreferences settings = getSharedPreferences(getString(R.string.save_file_name), MODE_PRIVATE);
        Map<String,?> m = settings.getAll();
        if(m != null){
            Log.d("preferences list", m.toString());
            for (Map.Entry<String, ?> entry :m.entrySet())
            {
                list.add(entry.getKey());
                Iterator iter = (HashSet<String>)(entry.getValue()).iterator();
                while (iter.hasNext()) {
                    System.out.println(iter.next());
                }
                //list.add("-");
            }

        }*/
        SQLiteDatabase db = new DBHelper(getApplicationContext()).getReadableDatabase();
        // select * from history order by cnt desc;
        Cursor c = db.query("history", new String[]{"word", "cnt", "lastupdated"}, null, null, null, null, "cnt desc");
        while (c.moveToNext()) {
            list.add(c.getString(0));
            list.add(String.valueOf(c.getInt(1)));
            list.add(c.getString(2));
        }
        c.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),android.R.layout.simple_list_item_1 , list);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }
}

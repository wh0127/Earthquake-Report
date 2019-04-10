package com.example.earthquakereport;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        final EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 查找单击的当前地震
                Earthquake currentEarthquake = adapter.getItem(position);

                // 将字符串 URL 转换为 URI 对象（以传递至 Intent 中 constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // 创建一个新的 Intent 以查看地震 URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // 发送 Intent 以启动新活动
                startActivity(websiteIntent);
            }
        });
    }
}

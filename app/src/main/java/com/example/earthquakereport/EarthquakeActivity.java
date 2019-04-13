package com.example.earthquakereport;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    /**
     * 地震 loader ID 的常量值。我们可选择任意整数。
     * 仅当使用多个 loader 时该设置才起作用。
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** 地震列表的适配器 */
    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 在布局中查找 {@link ListView} 的引用
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // 创建新适配器，将空地震列表作为输入
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        // 在 {@link ListView} 上设置适配器
        // 以便可以在用户界面中填充列表
        earthquakeListView.setAdapter(mAdapter);
        // 在 ListView 上设置项目单击监听器，该监听器会向 Web 浏览器发送 intent，
        // 打开包含有关所选地震详细信息的网站。
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 查找单击的当前地震
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // 将字符串 URL 转换成 URI 对象（传递到 Intent 构造函数中）
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // 创建新 intent 以查看地震 URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // 发送 intent 以启动新活动
                startActivity(websiteIntent);
            }
        });
        // 引用 LoaderManager，以便与 loader 进行交互。
        LoaderManager loaderManager = getSupportLoaderManager();
        // 初始化 loader。传递上面定义的整数 ID 常量并为为捆绑
        // 传递 null。为 LoaderCallbacks 参数（由于
        // 此活动实现了 LoaderCallbacks 接口而有效）传递此活动。
        Log.i(LOG_TAG, "TEST:calling initLoader()...");
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null,this );
/**
 ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
 // 启动 AsyncTask 以获取地震数据
 EarthquakeAsyncTask task = new EarthquakeAsyncTask();
 task.execute(USGS_REQUEST_URL);
 **/

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // 为给定 URL 创建新 loader
        Log.i(LOG_TAG, "TEST:onCreateLoader()called ... ");
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
        //       return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG, "TEST:onLoaderFinished called ...");
        // 清除之前地震数据的适配器
        mAdapter.clear();

        // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
        // 数据集。这将触发 ListView 执行更新。
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "onLoaderReset called ...");
        // 重置 Loader，以便能够清除现有数据。
        mAdapter.clear();
    }



    /**
     * {@link AsyncTask} 用于在后台线程上执行网络请求，然后
     * 使用响应中的地震列表更新 UI。
     *
     * AsyncTask 有三个泛型参数：输入类型、用于进度更新的类型和
     * 输出类型。我们的任务将获取字符串 URL 并返回地震。我们不会执行
     * 进度更新，因此第二个泛型是无效的。
     *
     * 我们将仅覆盖 AsyncTask 的两个方法：doInBackground() 和 onPostExecute()。
     * doInBackground() 方法会在后台线程上运行，因此可以运行长时间运行的代码
     * （如网络活动），而不会干扰应用的响应性。
     * onPostExecute() 在 UI 线程上运行，系统会将 doInBackground() 方法的结果传递给它，
     * 因此该方法可使用生成的数据更新 UI。

     private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

     /**
     * 此方法在后台线程上运行并执行网络请求。
     * 我们不能够通过后台线程更新 UI，因此我们返回
     * {@link Earthquake} 的列表作为结果。

     @Override
     protected List<Earthquake> doInBackground(String... urls) {
     // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
     if (urls.length < 1 || urls[0] == null) {
     return null;
     }

     List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
     return result;
     }

     /**
      * 后台工作完成后，此方法会在主 UI 线程上
      * 运行。此方法接收 doInBackground() 方法的返回值
      * 作为输入。首先，我们将清理适配器，除去先前 USGS 查询的地震
      * 数据。然后，我们使用新地震列表更新适配器，
      * 这将触发 ListView 重新填充其列表项。

     @Override
     protected void onPostExecute(List<Earthquake> data) {
     // 清除之前地震数据的适配器
     mAdapter.clear();

     // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
     // 数据集。这将触发 ListView 执行更新。
     if (data != null && !data.isEmpty()) {
     mAdapter.addAll(data);
     }
     }
     }
     */
}

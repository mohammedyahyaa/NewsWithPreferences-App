package com.example.mohamed.newswithpreferences;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.data;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>,
        SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String THE_GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=";

    public static final String LOG_TAG = NewsActivity.class.getName();

    public static final int ARTICLE_LOADER_ID = 1;

    ArticleAdapter mAdapter;

    LoaderManager loaderManager;

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {


        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        String category = sharedPrefs.getString(
                getString(R.string.settings_category_key), getString(R.string.settings_category_default));


        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(THE_GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("", category);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-references", "author");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q", "Android");


        return new ArticleLoader(this, uriBuilder.toString().replace("&=", ""));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ListView articleListView = (ListView) findViewById(R.id.list);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        articleListView.setAdapter(mAdapter);
        loaderManager = getLoaderManager();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (loaderManager.getLoader(ARTICLE_LOADER_ID) != null) {
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, NewsActivity.this);
        }

        loaderManager.initLoader(ARTICLE_LOADER_ID, null, NewsActivity.this);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {


                Article article = mAdapter.getItem(position);
                String url = article.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.contains((getString(R.string.settings_category_key)))) {
            loaderManager.restartLoader(ARTICLE_LOADER_ID, null, NewsActivity.this);
        } else if (key.contains(getString(R.string.settings_order_by_key))) {
            loaderManager.restartLoader(ARTICLE_LOADER_ID, null, NewsActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

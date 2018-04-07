package com.example.user.newstest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    public static final int TOP_HEADLINES_LOADER_ID = 1;



    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private View loadingIndicator;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = findViewById(R.id.loading_indicator);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(this, new ArrayList<News>());
        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportLoaderManager().initLoader(TOP_HEADLINES_LOADER_ID, null, MainActivity.this);
        } else {
            loadingIndicator.setVisibility(View.GONE);

            emptyView.setText(R.string.no_connection);
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(LOG_TAG, "OnCreateLoader");

        final String BASE_URL = "https://newsapi.org/v2/top-headlines?";
        final String CATEGORY_PARAM = "category";
        final String COUNTRY_PARAM = "country";
        final String API_KEY_PARAM = "apiKey";

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String country = sharedPref.getString(
                getString(R.string.settings_country_key),
                getString(R.string.settings_country_default));

        String builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(COUNTRY_PARAM, country)
                .appendQueryParameter(CATEGORY_PARAM, "general")
                .appendQueryParameter(API_KEY_PARAM, "636fabe95997449195a6e4d1c9b96b44")
                .build().toString();

        return new NewsLoader(this, builtUri);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        Log.v(LOG_TAG, "onLoadFinished");
        loadingIndicator.setVisibility(View.GONE);

        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setText(R.string.no_news);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        Log.v(LOG_TAG, "onLoaderReset");

        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_setting:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

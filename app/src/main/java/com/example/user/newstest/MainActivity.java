package com.example.user.newstest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        return new NewsLoader(this);
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
}

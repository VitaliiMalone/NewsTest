package com.example.user.newstest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    public static final String URL_AUTHORITY = "https://newsapi.org";
    public static final String URL_PATH = "/v2/top-headlines?";
    public static final String URL_PARAM = "country=us&";
    public static final String URL_API_KEY = "apiKey=636fabe95997449195a6e4d1c9b96b44";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(new ArrayList<News>());
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(1, null, MainActivity.this);

    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        String url = URL_AUTHORITY + URL_PATH + URL_PARAM + URL_API_KEY;
        return new NewsLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        adapter.clear();
    }
}

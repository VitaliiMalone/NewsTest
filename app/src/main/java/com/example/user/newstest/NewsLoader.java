package com.example.user.newstest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    public static final String LOG_TAG = NewsLoader.class.getName();

    private String uri;

    public NewsLoader(@NonNull Context context, String uri) {
        super(context);
        Log.v(LOG_TAG, "NewsLoader");
        this.uri = uri;

    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        Log.v(LOG_TAG, "loadInBackground");
        return QueryUtils.fetchNews(uri);
    }
}

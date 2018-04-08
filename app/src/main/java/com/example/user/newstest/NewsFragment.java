package com.example.user.newstest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>{

    public static final String LOG_TAG = NewsFragment.class.getName();

    public static final String ARG_NEWS_CATEGORY = "news_category";
    public static final String ARG_LOADER_ID = "loader_id";

    private int loaderId;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private View loadingIndicator;
    private TextView emptyView;
    private String category;

    public static Fragment newInstance(String category, int loaderId) {
        Bundle args = new Bundle();
        args.putString(ARG_NEWS_CATEGORY, category);
        args.putInt(ARG_LOADER_ID, loaderId);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString(ARG_NEWS_CATEGORY);
        loaderId = getArguments().getInt(ARG_LOADER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        loadingIndicator = v.findViewById(R.id.loading_indicator);
        emptyView = v.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RecyclerViewAdapter(getActivity(), new ArrayList<News>());
        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getActivity().getSupportLoaderManager().initLoader(loaderId, null, NewsFragment.this);
        } else {
            loadingIndicator.setVisibility(View.GONE);

            emptyView.setText(R.string.no_connection);
            emptyView.setVisibility(View.VISIBLE);
        }

        return v;
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        final String BASE_URL = "https://newsapi.org/v2/top-headlines?";
        final String CATEGORY_PARAM = "category";
        final String COUNTRY_PARAM = "country";
        final String API_KEY_PARAM = "apiKey";

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String country = sharedPref.getString(
                getString(R.string.settings_country_key),
                getString(R.string.settings_country_default));

        String builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(COUNTRY_PARAM, country)
                .appendQueryParameter(CATEGORY_PARAM, category)
                .appendQueryParameter(API_KEY_PARAM, "636fabe95997449195a6e4d1c9b96b44")
                .build().toString();
        Log.v(LOG_TAG, "OnCreateLoader: builtUrl: " + builtUri);
        return new NewsLoader(getActivity(), builtUri);
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

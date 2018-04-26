package com.example.user.newstest.ui;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newstest.R;
import com.example.user.newstest.adapter.RecyclerViewAdapter;
import com.example.user.newstest.model.News;
import com.example.user.newstest.network.NewsApiClient;
import com.example.user.newstest.network.ServiceGenerator;
import com.example.user.newstest.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SearchableFragment";
    public static final String ARG_SEARCH_QUERY = "query";


    @BindView(R.id.empty_view_button)
    Button emptyViewButton;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String query;

    public SearchableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_SEARCH_QUERY)) {
            query = getArguments().getString(ARG_SEARCH_QUERY);
            Log.v(TAG, "onCreate: recieved query is " + query);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, v);

        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @OnClick(R.id.empty_view_button)
    public void emptyViewRefresh() {
        updateUI();
    }

    private void updateUI() {
        if (NetworkUtils.isOnline(getContext())) {
            setEmptyView(false);
            setRefreshing(true);

            String apiKey = getString(R.string.news_api_key);

            NewsApiClient newsApiClient = ServiceGenerator.createService(NewsApiClient.class);
            Call<News> call = newsApiClient.getSearchNews(query, apiKey);
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.body() != null) {
                        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), response.body()));
                        setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Log.d(TAG, "onFailure: throwable message " + t.getMessage());

                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                    setRefreshing(false);
                }
            });
        } else {
            setEmptyView(true);
            setRefreshing(false);

        }
    }

    @Override
    public void onRefresh() {
        Log.v(TAG, "OnRefresh call");
        if (NetworkUtils.isOnline(getContext())) {
            updateUI();
        } else {
            Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
            setRefreshing(false);
        }
    }

    private void setRefreshing (boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);

    }

    private void setEmptyView(boolean isEmpty) {
        if (isEmpty) emptyViewButton.setVisibility(View.VISIBLE);
        else emptyViewButton.setVisibility(View.GONE);
    }
}

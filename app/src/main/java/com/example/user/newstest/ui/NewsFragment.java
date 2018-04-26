package com.example.user.newstest.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "NewsFragment";
    public static final String ARG_NEWS_CATEGORY = "news_category";

    @BindView(R.id.empty_view_button)
    Button emptyViewButton;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String category;

    public static Fragment newInstance(String category) {
        Bundle args = new Bundle();
        args.putString(ARG_NEWS_CATEGORY, category);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_NEWS_CATEGORY)) {
            category = getArguments().getString(ARG_NEWS_CATEGORY);
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

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String country = sharedPref.getString(getString(R.string.settings_country_key),
                getString(R.string.settings_country_default));
            String apiKey = getString(R.string.news_api_key);

            NewsApiClient newsApiClient = ServiceGenerator.createService(NewsApiClient.class);
            Call<News> call = newsApiClient.getTopNews(category, country, apiKey);
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

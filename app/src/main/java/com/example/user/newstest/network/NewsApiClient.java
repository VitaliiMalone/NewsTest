package com.example.user.newstest.network;

import com.example.user.newstest.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiClient {

    String BASE_URL = "https://newsapi.org/v2/";

    @GET("top-headlines")
    Call<News> getTopNews(@Query("category") String category,
                                @Query("country") String country,
                                @Query("apiKey") String apiKey);

    @GET("everything")
    Call<News> getSearchNews(@Query("q") String query,
                                   @Query("apiKey") String apiKey);

}

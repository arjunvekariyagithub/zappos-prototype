package com.zappos.android.app.prototype.rest;

import com.zappos.android.app.prototype.models.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("Search")
    Call<SearchResponse> getSearchResults(@Query("key") String apiKey, @Query("term") String term,
                                          @Query("limit") int limit, @Query("page") int page);
}

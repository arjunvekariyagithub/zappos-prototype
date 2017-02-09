package com.zappos.android.app.prototype.data;

import android.util.Log;

import com.zappos.android.app.prototype.fragments.ProductListFragment;
import com.zappos.android.app.prototype.models.ProductInfo;
import com.zappos.android.app.prototype.models.SearchResponse;
import com.zappos.android.app.prototype.rest.ApiClient;
import com.zappos.android.app.prototype.rest.ApiInterface;
import com.zappos.android.app.prototype.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arjun on 2/5/17.
 *
 * A Singleton class, responsible for fetching and writing data from and to server.
 */

public class DataHandler {
    private static final String TAG = ProductListFragment.class.getSimpleName();
    private static DataHandler sInstance = null;
    public ApiInterface mApiService;
    public DataListener mDataListener;

    public DataHandler() {
        mApiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public static DataHandler getInstance() {
        if (sInstance == null) {
            sInstance = new DataHandler();

        }
        return sInstance;
    }

    public interface DataListener {

        /**
         * Called when data is successfully fetched from server.
         *
         * @param productList list of ProductInfo objects.
         */
        void onDataResponse(List<ProductInfo> productList);

        /**
         * Called when data request is failed.
         *
         * @param t throwable object holding error info
         */

        void onRequestFailure(Throwable t);
    }

    public void setDataListener(DataListener listener) {
        mDataListener = listener;
    }

    /**
     * request server to fetch search results for specific search term
     * Using {@link retrofit2.Retrofit} as HTTP client.
     * Limiting search results to 100 for this demo app.
     *
     * @param term search term
     */
    public void requestData4Term(String term) {
        Call<SearchResponse> call = mApiService.getSearchResults(Constants.API_KEY, term,
                Constants.SEARCH_RESULTS_LIMIT, Constants.PAGE_NO);
        call.enqueue(new Callback<SearchResponse>() {

            /**
             * Called when request is successful.
             *
             * @param call callback for request.
             * @param response object containing serialized response in {@link SearchResponse} form
             */

            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (mDataListener != null)
                        mDataListener.onDataResponse(response.body().getResults());
                }
            }

            /**
             * Called when request is failed.
             *
             * @param call callback for request.
             * @param t throwable object holding error info
             */

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                mDataListener.onRequestFailure(t);
            }
        });
    }
}

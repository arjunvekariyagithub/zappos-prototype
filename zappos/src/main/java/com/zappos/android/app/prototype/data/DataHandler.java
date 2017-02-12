package com.zappos.android.app.prototype.data;

import android.util.Log;

import com.zappos.android.app.prototype.activity.ProductPageActivity;
import com.zappos.android.app.prototype.fragments.ProductListFragment;
import com.zappos.android.app.prototype.models.ImageInfo;
import com.zappos.android.app.prototype.models.ProductImagesResponse;
import com.zappos.android.app.prototype.models.ProductInfo;
import com.zappos.android.app.prototype.models.SearchResponse;
import com.zappos.android.app.prototype.models.SimilarProductsResponse;
import com.zappos.android.app.prototype.models.SingleProductInfo;
import com.zappos.android.app.prototype.models.SingleProductResponse;
import com.zappos.android.app.prototype.rest.ApiClient;
import com.zappos.android.app.prototype.rest.ApiInterface;
import com.zappos.android.app.prototype.utils.Constants;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arjun on 2/5/17.
 * <p>
 * A Singleton class, responsible for fetching and writing data from and to server.
 */

public class DataHandler {
    private static final String TAG = ProductListFragment.class.getSimpleName();
    private static DataHandler sInstance = null;
    public ApiInterface mApiService;
    public DataListener mDataListener;
    public ProductDetailsListener mProductDetailsListener;

    public DataHandler() {
        mApiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public static DataHandler getInstance() {
        if (sInstance == null) {
            sInstance = new DataHandler();

        }
        return sInstance;
    }

    public void setDataListener(DataListener listener) {
        mDataListener = listener;
    }

    public void setProductDetailsListener(ProductDetailsListener listener) {
        mProductDetailsListener = listener;
    }

    /**
     * request server to fetch search results for specific search term
     * Using {@link retrofit2.Retrofit} as HTTP client.
     * Limiting search results to 100 for this demo app.
     *
     * @param term search term
     * @see ProductListFragment#onDataResponse(List)
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

    /**
     * request server to fetch images for specific product
     * Using {@link retrofit2.Retrofit} as HTTP client.
     *
     * @param productId product id to retrieve images for
     * @see ProductPageActivity#onImageInfoResponse(HashMap)
     */

    public void requestImages4Product(final String productId) {
        Call<ProductImagesResponse> call = mApiService.getImages4Product(Constants.API_KEY,
                productId, Constants.IMAGE_TYPE_LIST, Constants.IMAGE_RECIPE);
        call.enqueue(new Callback<ProductImagesResponse>() {

            /**
             * Called when request is successful.
             *
             * @param call callback for request.
             * @param response object containing serialized response in {@link SearchResponse} form
             */

            @Override
            public void onResponse(Call<ProductImagesResponse> call, Response<ProductImagesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.d("Zappos", "Style count: " + response.body().getProductImages().size());
                    if (mProductDetailsListener != null) {
                        mProductDetailsListener.onImageInfoResponse(response.body().getProductImages());
                    }
                }

            }

            /**
             * Called when request is failed.
             *
             * @param call callback for request.
             * @param t throwable object holding error info
             */

            @Override
            public void onFailure(Call<ProductImagesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                mDataListener.onRequestFailure(t);
            }
        });
    }

    /**
     * request server to retrieving similar products.
     *
     * @param styleId style id to retrieve similar products for
     * @see ProductPageActivity#onSimilarProductsResponse(List)
     */

    public void requestSimilarProduct(final String styleId) {
        Call<SimilarProductsResponse> call = mApiService.getSimilarProducts(Constants.API_KEY,
                styleId, Constants.SIMILARITY_TYPE, Constants.SIMILARITY_EMPHASIS, 10);
        call.enqueue(new Callback<SimilarProductsResponse>() {

            /**
             * Called when request is successful.
             *
             * @param call callback for request.
             * @param response object containing serialized response in {@link SearchResponse} form
             */

            @Override
            public void onResponse(Call<SimilarProductsResponse> call, Response<SimilarProductsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<ProductInfo> productInfoList = response.body().getResults();
                    if (mProductDetailsListener != null) {
                        mProductDetailsListener.onSimilarProductsResponse(productInfoList);
                    }
                }

            }

            /**
             * Called when request is failed.
             *
             * @param call callback for request.
             * @param t throwable object holding error info
             */

            @Override
            public void onFailure(Call<SimilarProductsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                mDataListener.onRequestFailure(t);
            }
        });
    }

    /**
     * request server to retrieve information about individual product.
     *
     * @param productId product id to retrieve information for
     * @see ProductPageActivity#onSingleProductResponse(List)
     */
    public void requestSingleProduct(final String productId) {
        Call<SingleProductResponse> call = mApiService.getProductsInfo(productId, Constants.API_KEY);
        call.enqueue(new Callback<SingleProductResponse>() {

            /**
             * Called when request is successful.
             *
             * @param call callback for request.
             * @param response object containing serialized response in {@link SearchResponse} form
             */

            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<SingleProductInfo> productInfoList = response.body().getProduct();
                    if (mProductDetailsListener != null) {
                        mProductDetailsListener.onSingleProductResponse(productInfoList);
                    }
                }

            }

            /**
             * Called when request is failed.
             *
             * @param call callback for request.
             * @param t throwable object holding error info
             */

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                mDataListener.onRequestFailure(t);
            }
        });
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

    /**
     * data listener for {@link ProductPageActivity}
     */
    public interface ProductDetailsListener {

        /**
         * Called when data is successfully fetched from server.
         *
         * @param productImages list of ImageInfo objects.
         */
        void onImageInfoResponse(HashMap<String, List<ImageInfo>> productImages);

        void onSimilarProductsResponse(List<ProductInfo> productInfoList);

        void onSingleProductResponse(List<SingleProductInfo> product);

        /**
         * Called when data request is failed.
         *
         * @param t throwable object holding error info
         */

        void onRequestFailure(Throwable t);
    }
}

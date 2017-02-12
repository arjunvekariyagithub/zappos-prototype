package com.zappos.android.app.prototype.rest;

import com.zappos.android.app.prototype.models.ProductImagesResponse;
import com.zappos.android.app.prototype.models.SearchResponse;
import com.zappos.android.app.prototype.models.SimilarProductsResponse;
import com.zappos.android.app.prototype.models.SingleProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    /**
     * retrofit callback signature for retrieving search results.
     *
     * @param apiKey api key for accessing data from api.zappos.com
     * @param term   search term
     * @param limit  search result limit
     * @param page   page number
     * @return data in {@link SearchResponse} form
     */
    @GET("Search")
    Call<SearchResponse> getSearchResults(@Query("key") String apiKey,
                                          @Query("term") String term,
                                          @Query("limit") int limit,
                                          @Query("page") int page);

    /**
     * retrofit callback signature for retrieving product images.
     *
     * @param apiKey    api key for accessing data from api.zappos.com
     * @param productId product id to retrieve images for
     * @param type      images type attribute
     * @param recipe    image resolution attribute
     * @return data in {@link ProductImagesResponse} form
     */
    @GET("Image")
    Call<ProductImagesResponse> getImages4Product(@Query("key") String apiKey,
                                                  @Query("productId") String productId,
                                                  @Query("type") String[] type,
                                                  @Query("recipe") String recipe);

    /**
     * retrofit callback signature for retrieving similar products.
     *
     * @param apiKey   api key for accessing data from api.zappos.com
     * @param styleId  style id to retrieve similar products for
     * @param type     images type attribute
     * @param emphasis attribute to put emphasis on while searching similar product
     * @return data in {@link SimilarProductsResponse} form
     */
    @GET("Search/Similarity")
    Call<SimilarProductsResponse> getSimilarProducts(@Query("key") String apiKey,
                                                     @Query("styleId") String styleId,
                                                     @Query("type") String type,
                                                     @Query("emphasis") String emphasis,
                                                     @Query("limit") int limit);

    /**
     * retrofit callback signature for retrieving information
     * about individual product.
     *
     * @param apiKey    api key for accessing data from api.zappos.com
     * @param productId product id to retrieve information for
     * @return data in {@link SingleProductResponse} form
     */
    @GET("Product/{id}")
    Call<SingleProductResponse> getProductsInfo(@Path("id") String productId,
                                                @Query("key") String apiKey);

}

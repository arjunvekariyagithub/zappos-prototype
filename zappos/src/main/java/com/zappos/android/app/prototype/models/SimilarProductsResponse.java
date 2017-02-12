package com.zappos.android.app.prototype.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Model class representing similar products {@link retrofit2.Response} from server side.
 *
 * @SerializedName is used by g-son for data serialization.
 */
public class SimilarProductsResponse {

    @SerializedName("currentResultCount")
    private String currentResultCount;
    @SerializedName("totalResultCount")
    private String totalResultCount;
    @SerializedName("methodUsed")
    private String methodUsed;
    @SerializedName("limit")
    private String limit;
    @SerializedName("currentPage")
    private String currentPage;
    @SerializedName("results")
    private List<ProductInfo> results;
    @SerializedName("statusCode")
    private String statusCode;

    public String getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(String totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(String currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    public String getMethodUsed() {
        return methodUsed;
    }

    public void setMethodUsed(String methodUsed) {
        this.methodUsed = methodUsed;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public List<ProductInfo> getResults() {
        return results;
    }

    public void setResults(List<ProductInfo> results) {
        this.results = results;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}

package com.zappos.android.app.prototype.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arjun on 2/1/17.
 *
 * Model class representing search {@link retrofit2.Response} from server side.
 * @SerializedName is used by g-son for data serialization.
 */
public class SearchResponse {

    @SerializedName("originalTerm")
    private String originalTerm;
    @SerializedName("currentResultCount")
    private String currentResultCount;
    @SerializedName("totalResultCount")
    private String totalResultCount;
    @SerializedName("term")
    private String term;
    @SerializedName("results")
    private List<ProductInfo> results;
    @SerializedName("statusCode")
    private String statusCode;

    public String getOriginalTerm() {
        return originalTerm;
    }

    public void setOriginalTerm(String originalTerm) {
        this.originalTerm = originalTerm;
    }

    public String getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(String currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    public String getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(String totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public List<ProductInfo> getResults() {
        return results;
    }

    public void setResults(List<ProductInfo> results) {
        this.results = results;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}

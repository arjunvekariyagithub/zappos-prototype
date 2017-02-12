package com.zappos.android.app.prototype.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Model class representing single product {@link retrofit2.Response} from server side.
 *
 * @SerializedName is used by g-son for data serialization.
 */
public class SingleProductResponse {


    @SerializedName("product")
    private List<SingleProductInfo> product;
    @SerializedName("statusCode")
    private String statusCode;

    public List<SingleProductInfo> getProduct() {
        return product;
    }

    public void setProduct(List<SingleProductInfo> product) {
        this.product = product;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}

package com.zappos.android.app.prototype.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by arjun on 2/5/17.
 * <p>
 * Model class representing product images {@link retrofit2.Response} from server side.
 *
 * @SerializedName is used by g-son for data serialization.
 */
public class ProductImagesResponse {

    @SerializedName("images")
    private HashMap<String, List<ImageInfo>> productImages;
    @SerializedName("productId")
    private String productId;
    @SerializedName("statusCode")
    private String statusCode;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public HashMap<String, List<ImageInfo>> getProductImages() {
        return productImages;
    }

    public void setProductImages(HashMap<String, List<ImageInfo>> productImages) {
        this.productImages = productImages;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}

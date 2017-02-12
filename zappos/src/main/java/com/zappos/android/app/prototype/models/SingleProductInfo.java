package com.zappos.android.app.prototype.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Model class representing individual product information.
 *
 * @SerializedName is used by g-son for data serialization.
 */
public class SingleProductInfo {

    @SerializedName("brandId")
    private String brandId;
    @SerializedName("brandName")
    private String brandName;
    @SerializedName("defaultImageUrl")
    private String defaultImageUrl;
    @SerializedName("defaultProductUrl")
    private String defaultProductUrl;
    @SerializedName("productId")
    private String productId;
    @SerializedName("productName")
    private String productName;

    public SingleProductInfo(String brandId, String brandName, String defaultImageUrl, String defaultProductUrl, String productId, String productName) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.defaultImageUrl = defaultImageUrl;
        this.defaultProductUrl = defaultProductUrl;
        this.productId = productId;
        this.productName = productName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    public String getDefaultProductUrl() {
        return defaultProductUrl;
    }

    public void setDefaultProductUrl(String defaultProductUrl) {
        this.defaultProductUrl = defaultProductUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

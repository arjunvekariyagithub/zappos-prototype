package com.zappos.android.app.prototype.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Model class representing information about individual product image.
 *
 * @SerializedName is used by g-son for data serialization.
 */
public class ImageInfo {

    @SerializedName("filename")
    private String filename;
    @SerializedName("imageId")
    private String imageId;
    @SerializedName("recipeName")
    private String recipeName;
    @SerializedName("format")
    private String format;
    @SerializedName("styleId")
    private String styleId;
    @SerializedName("productId")
    private String productId;
    @SerializedName("type")
    private String type;

    public ImageInfo(String imageId, String filename,
                     String recipeName, String format,
                     String styleId, String productId, String type) {
        this.imageId = imageId;
        this.filename = filename;
        this.recipeName = recipeName;
        this.format = format;
        this.styleId = styleId;
        this.productId = productId;
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

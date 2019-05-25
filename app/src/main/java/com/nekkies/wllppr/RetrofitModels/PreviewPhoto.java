package com.nekkies.wllppr.RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviewPhoto {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("urls")
    @Expose
    private Urls__1 urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Urls__1 getUrls() {
        return urls;
    }

    public void setUrls(Urls__1 urls) {
        this.urls = urls;
    }

}

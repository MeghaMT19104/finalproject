package com.example.appproject;
public class upload {

    private String mImageUrl;

    public upload(String mImageUrl)
    {
        this.mImageUrl=mImageUrl;
    }

    public upload() {
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
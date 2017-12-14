package com.apps.fernandes.ashley.wallpapercentral.models;

/**
 * Created by gf on 15-07-2017.
 */

public class PopularPhotos {
    String artistUserName,artistUrl,artistName;
    String photoRawUrl,photoFullUrl,photoThumbUrl,photoColor,photoDownloadUrl,photoRegularUrl;
    String photoSmallUrl = null;
    String photoId = "";
    String photoSource = "";
    int PhotoHeight,PhotoWidth;
    boolean liked = false;





    public String getArtistUserName() {
        return artistUserName;
    }

    public void setArtistUserName(String artistUserName) {
        this.artistUserName = artistUserName;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPhotoRawUrl() {
        return photoRawUrl;
    }

    public void setPhotoRawUrl(String photoRawUrl) {
        this.photoRawUrl = photoRawUrl;
    }

    public String getPhotoFullUrl() {
        return photoFullUrl;
    }

    public void setPhotoFullUrl(String photoFullUrl) {
        this.photoFullUrl = photoFullUrl;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public void setPhotoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public String getPhotoColor() {
        return photoColor;
    }

    public void setPhotoColor(String photoColor) {
        this.photoColor = photoColor;
    }

    public String getPhotoDownloadUrl() {
        return photoDownloadUrl;
    }

    public void setPhotoDownloadUrl(String photoDownloadUrl) {
        this.photoDownloadUrl = photoDownloadUrl;
    }

    public String getPhotoRegularUrl() {
        return photoRegularUrl;
    }

    public void setPhotoRegularUrl(String photoRegularUrl) {
        this.photoRegularUrl = photoRegularUrl;
    }

    public String getPhotoSmallUrl() {
        return photoSmallUrl;
    }

    public void setPhotoSmallUrl(String photoSmallUrl) {
        this.photoSmallUrl = photoSmallUrl;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoSource() {
        return photoSource;
    }

    public void setPhotoSource(String photoSource) {
        this.photoSource = photoSource;
    }

    public int getPhotoHeight() {
        return PhotoHeight;
    }

    public void setPhotoHeight(int photoHeight) {
        PhotoHeight = photoHeight;
    }

    public int getPhotoWidth() {
        return PhotoWidth;
    }

    public void setPhotoWidth(int photoWidth) {
        PhotoWidth = photoWidth;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}

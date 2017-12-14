package com.apps.fernandes.ashley.wallpapercentral.models;

import io.realm.RealmObject;

/**
 * Created by gf on 06-07-2017.
 */

public class FavouritePhotos extends RealmObject{
    String vArtistName,vArtistUrl,vPhotoFullUrl,vPhotoColor,vPhotoRegularUrl,vPhotoSmallUrl,vPhotoId;
    String vPhotoSource;


    public FavouritePhotos() {
    }

    public FavouritePhotos(String vArtistName, String vArtistUrl, String vPhotoFullUrl, String vPhotoColor, String vPhotoRegularUrl, String vPhotoSmallUrl, String vPhotoId ,String vPhotoSource) {
        this.vArtistName = vArtistName;
        this.vArtistUrl = vArtistUrl;
        this.vPhotoFullUrl = vPhotoFullUrl;
        this.vPhotoColor = vPhotoColor;
        this.vPhotoRegularUrl = vPhotoRegularUrl;
        this.vPhotoSmallUrl = vPhotoSmallUrl;
        this.vPhotoId = vPhotoId;
        this.vPhotoSource = vPhotoSource;
    }

    public String getvPhotoSource() {
        return vPhotoSource;
    }

    public void setvPhotoSource(String vPhotoSource) {
        this.vPhotoSource = vPhotoSource;
    }


    public String getvArtistName() {
        return vArtistName;
    }

    public void setvArtistName(String vArtistName) {
        this.vArtistName = vArtistName;
    }

    public String getvArtistUrl() {
        return vArtistUrl;
    }

    public void setvArtistUrl(String vArtistUrl) {
        this.vArtistUrl = vArtistUrl;
    }

    public String getvPhotoFullUrl() {
        return vPhotoFullUrl;
    }

    public void setvPhotoFullUrl(String vPhotoFullUrl) {
        this.vPhotoFullUrl = vPhotoFullUrl;
    }

    public String getvPhotoColor() {
        return vPhotoColor;
    }

    public void setvPhotoColor(String vPhotoColor) {
        this.vPhotoColor = vPhotoColor;
    }

    public String getvPhotoRegularUrl() {
        return vPhotoRegularUrl;
    }

    public void setvPhotoRegularUrl(String vPhotoRegularUrl) {
        this.vPhotoRegularUrl = vPhotoRegularUrl;
    }

    public String getvPhotoSmallUrl() {
        return vPhotoSmallUrl;
    }

    public void setvPhotoSmallUrl(String vPhotoSmallUrl) {
        this.vPhotoSmallUrl = vPhotoSmallUrl;
    }

    public String getvPhotoId() {
        return vPhotoId;
    }

    public void setvPhotoId(String vPhotoId) {
        this.vPhotoId = vPhotoId;
    }
}

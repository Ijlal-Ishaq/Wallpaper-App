package com.example.wallpaperapp;

import android.os.Parcel;
import android.os.Parcelable;

public class wall_obj implements Parcelable {

    private String link;
    private String att;
    private String likes;
    private String id;
    private String download_link;


    public wall_obj(String link, String att, String likes, String id, String download_link) {
        this.link = link;
        this.att = att;

        this.likes = likes;
        this.id = id;
        this.download_link = download_link;
    }


    protected wall_obj(Parcel in) {
        link = in.readString();
        att = in.readString();

        likes = in.readString();
        id = in.readString();
        download_link = in.readString();
    }

    public static final Creator<wall_obj> CREATOR = new Creator<wall_obj>() {
        @Override
        public wall_obj createFromParcel(Parcel in) {
            return new wall_obj(in);
        }

        @Override
        public wall_obj[] newArray(int size) {
            return new wall_obj[size];
        }
    };

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAtt() {
        return att;
    }

    public void setAtt(String att) {
        this.att = att;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(link);
        parcel.writeString(att);
        parcel.writeString(likes);
        parcel.writeString(id);
        parcel.writeString(download_link);
    }


}

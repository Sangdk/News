package com.t3h.newsproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "news")
public class News {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String desc;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    @ColumnInfo(name = "urlToImage")
    @SerializedName("urlToImage")
    private String img;

    public News(long id, String title, String desc, String url, String img, String date) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.img = img;
        this.date = date;
    }

    @ColumnInfo(name = "publishedAt")
    @SerializedName("publishedAt")


    private String date;

    private int isFavorite =0;

    public int isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

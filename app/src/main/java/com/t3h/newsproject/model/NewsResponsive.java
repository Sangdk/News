package com.t3h.newsproject.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponsive {

    @SerializedName("articles")
    private List<News> news;

    public List<News> getNews() {
        return news;
    }
}

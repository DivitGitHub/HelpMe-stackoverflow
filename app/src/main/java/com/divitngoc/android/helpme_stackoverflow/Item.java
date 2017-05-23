package com.divitngoc.android.helpme_stackoverflow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private String title;
    private Boolean isAnswered;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;

    public Integer getScore() {
        return score;
    }

    public String getLink() {
        return link;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public String getTitle() {
        return title;
    }

}

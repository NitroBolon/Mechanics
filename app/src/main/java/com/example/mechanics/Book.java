package com.example.mechanics;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {
    @SerializedName("title")
    private String title;

    @SerializedName("author_name")
    private List<String> authors;

    @SerializedName("cover_i")
    private String cover;

    public String getTitle(){return title;}

    public List<String> getAuthors(){return authors;}

    public String getCover(){return cover;}

    public void setTitle(String title){this.title=title;}
    public void setAuthors(List<String> authors){this.authors=authors;}
    public void setCover(String cover){this.cover=cover;}
}

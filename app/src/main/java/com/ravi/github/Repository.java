package com.ravi.github;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repositories")
public class Repository {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("stargazers_count")
    private int stargazersCount;

    public Repository(String name, String description, int stargazersCount) {
        this.name = name;
        this.description = description;
        this.stargazersCount = stargazersCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public int getStargazersCount() {
        return stargazersCount;
    }

}

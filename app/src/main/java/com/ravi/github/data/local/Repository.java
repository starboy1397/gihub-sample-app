package com.ravi.github.data.local;

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

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("language")
    private String language;

    public Repository(String name, String description, int stargazersCount, String language, Owner owner) {
        this.name = name;
        this.description = description;
        this.stargazersCount = stargazersCount;
        this.language = language;
        this.owner = owner != null ? owner : new Owner();
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

    public String getLanguage() {
        return language != null ? language : "Unknown";
    }



    public Owner getOwner() {
        return owner != null ? owner : new Owner();
    }

    public static class Owner {
        @SerializedName("login")
        private String login;

        public String getLogin() {
            return login != null ? login : "Unknown";
        }
    }

}

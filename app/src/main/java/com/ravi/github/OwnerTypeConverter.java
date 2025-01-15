package com.ravi.github;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.ravi.github.data.local.Repository;

public class OwnerTypeConverter {

    @TypeConverter
    public static String fromOwner(Repository.Owner owner) {
        return new Gson().toJson(owner);  // Convert Owner object to JSON string
    }

    @TypeConverter
    public static Repository.Owner toOwner(String ownerJson) {
        return new Gson().fromJson(ownerJson, Repository.Owner.class);  // Convert JSON string back to Owner object
    }

}

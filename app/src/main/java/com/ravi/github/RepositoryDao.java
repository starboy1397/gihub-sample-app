package com.ravi.github;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RepositoryDao {


    @Insert( onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Repository> repositories);


    @Query("SELECT * FROM repositories")
    LiveData<List<Repository>> getAllRepositories();

}

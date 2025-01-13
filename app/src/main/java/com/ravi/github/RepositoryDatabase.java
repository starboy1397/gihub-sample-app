package com.ravi.github;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Repository.class}, version = 1)
public abstract class RepositoryDatabase extends RoomDatabase {

    public static volatile RepositoryDatabase INSTANCE;
    public abstract RepositoryDao repositoryDao();

    public static RepositoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RepositoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RepositoryDatabase.class,
                            "repository_database"
                    ).fallbackToDestructiveMigration() // Clears data on schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

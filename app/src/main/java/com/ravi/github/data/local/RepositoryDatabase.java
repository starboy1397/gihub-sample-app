package com.ravi.github.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ravi.github.OwnerTypeConverter;


@Database(entities = {Repository.class}, version = 1)
@TypeConverters(OwnerTypeConverter.class)
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

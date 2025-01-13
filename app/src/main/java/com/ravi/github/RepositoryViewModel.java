package com.ravi.github;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RepositoryViewModel extends AndroidViewModel {

    private final RepositoryRepository repositoryRepository;
    private final LiveData<List<Repository>> allRepositories;

    public RepositoryViewModel(@NonNull Application application) {
        super(application);
        repositoryRepository = new RepositoryRepository(application);
        allRepositories = repositoryRepository.getRepositories();
    }

    public LiveData<List<Repository>> getAllRepositories() {
        Log.d("RepositoryViewModel", "Returning LiveData of repositories");
        return allRepositories;
    }

    public void fetchRepositories(String username) {
        repositoryRepository.fetchRepositoriesFromApi(username);
    }
}

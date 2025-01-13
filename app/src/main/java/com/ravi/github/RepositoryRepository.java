package com.ravi.github;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryRepository {

    private final RepositoryDao repositoryDao;
    private final GitHubApiService apiService;
    private final ExecutorService executorService;

    public RepositoryRepository(Context context) {

        RepositoryDatabase database = RepositoryDatabase.getInstance(context);
        repositoryDao = database.repositoryDao();

        // Logging interceptor for detailed logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GitHubApiService.class);
        executorService = Executors.newSingleThreadExecutor();

    }

    public LiveData<List<Repository>> getRepositories() {
        return repositoryDao.getAllRepositories();
    }

    public void fetchRepositoriesFromApi(String userName) {
        executorService.execute(() -> {
            try {
                Log.d("RepositoryRepository", "Fetching repositories for user: " + userName);
                List<Repository> repositories = apiService.getRepositories(userName).execute().body();
                if (repositories != null && !repositories.isEmpty()) {
                    repositoryDao.insertAll(repositories);
                    Log.d("RepositoryRepository", "Inserted " + repositories.size() + " repositories into database");
                } else {
                    Log.e("RepositoryRepository", "No repositories found or response is null");
                }
            } catch (Exception e) {
                Log.e("RepositoryRepository", "Error fetching repositories", e);
            }
        });
    }
}

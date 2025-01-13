package com.ravi.github;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApiService {

    @GET("users/{username}/repos")
    Call<List<Repository>> getRepositories(@Path("username") String username);

}

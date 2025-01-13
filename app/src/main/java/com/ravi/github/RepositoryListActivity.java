package com.ravi.github;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ravi.github.databinding.ActivityRepositoryListBinding;

public class RepositoryListActivity extends AppCompatActivity {

    private ActivityRepositoryListBinding binding;
    private RepositoryViewModel repositoryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRepositoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = binding.repositoryRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RepositoryAdapter adapter = new RepositoryAdapter(repository -> {
            Intent intent = new Intent(RepositoryListActivity.this, RepositoryDetailActivity.class);

            // Ensure that owner is not null before accessing getLogin()
            String ownerName = repository.getOwner() != null && repository.getOwner().getLogin() != null
                    ? repository.getOwner().getLogin()
                    : "Unknown Owner";

            intent.putExtra("repository_name", repository.getName());
            intent.putExtra("owner_name", ownerName);
            intent.putExtra("description", repository.getDescription());
            intent.putExtra("language", repository.getLanguage());
            intent.putExtra("star_count", repository.getStargazersCount());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        repositoryViewModel = new ViewModelProvider(this).get(RepositoryViewModel.class);
        repositoryViewModel.getAllRepositories().observe(this, repositories -> {
            if (repositories != null && !repositories.isEmpty()) {
                adapter.setRepositories(repositories);
                Log.d("RepositoryListActivity", "Repositories loaded: " + repositories.size());
            }else {
                Log.e("RepositoryListActivity", "No repositories in database");
            }
        });
        repositoryViewModel.fetchRepositories("starboy1397");

    }
}
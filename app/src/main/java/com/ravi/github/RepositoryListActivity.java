package com.ravi.github;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ravi.github.databinding.ActivityRepositoryListBinding;

import java.util.ArrayList;
import java.util.List;

public class RepositoryListActivity extends AppCompatActivity {

    private ActivityRepositoryListBinding binding;
    private RepositoryViewModel repositoryViewModel;
    private RepositoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRepositoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanelId = getString(R.string.default_notification_channel_id);
            String chanelName = getString(R.string.default_notification_channel_name);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(chanelId,
                    chanelName,
                    NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MyWorker", String.format("Key: %s, Value: %s", key, value));
            }
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("RepositoryListActivity", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("RepositoryListActivity", msg);
                        Toast.makeText(RepositoryListActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });




        RecyclerView recyclerView = binding.repositoryRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialToolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

         adapter = new RepositoryAdapter(repository -> {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repository_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Search Repositories...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterRepositories(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterRepositories(newText);
                    return true;
                }
            });
        } else {
            Log.e("RepositoryListActivity", "SearchView is null");
        }


        return super.onCreateOptionsMenu(menu);
    }

    private void filterRepositories(String query) {
        repositoryViewModel.getAllRepositories().observe(this, repositories -> {
            if (repositories != null) {
                List<Repository> filteredList = new ArrayList<>();
                for (Repository repo : repositories) {
                    if (repo.getName().toLowerCase().startsWith(query.toLowerCase())) {
                        filteredList.add(repo);
                    }
                }
                adapter.setRepositories(filteredList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            // Navigate to the SettingsActivity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
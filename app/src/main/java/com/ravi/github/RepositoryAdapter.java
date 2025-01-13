package com.ravi.github;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private List<Repository> repositories = new ArrayList<>();
    private final OnRepositoryClickListener onRepositoryClickListener;

    public RepositoryAdapter(OnRepositoryClickListener listener) {
        this.onRepositoryClickListener = listener;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
        Log.d("RepositoryAdapter", "Updating adapter with " + repositories.size() + " repositories");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        Repository repository = repositories.get(position);
        holder.name.setText(repository.getName());
        holder.description.setText(repository.getDescription() != null ? repository.getDescription() : "No description available");
        holder.stars.setText(String.valueOf(repository.getStargazersCount()));

        String ownerLogin = (repository.getOwner() != null && repository.getOwner().getLogin() != null) ? repository.getOwner().getLogin() : "Unknown";
        holder.itemView.setOnClickListener(v -> onRepositoryClickListener.onRepositoryClick(repository));
        Log.d("RepositoryAdapter", "Owner login: " + ownerLogin);
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, stars;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            stars = itemView.findViewById(R.id.starsTextView);
        }
    }

    public interface OnRepositoryClickListener {
        void onRepositoryClick(Repository repository);
    }
}

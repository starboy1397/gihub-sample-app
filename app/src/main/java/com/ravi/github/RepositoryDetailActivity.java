package com.ravi.github;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ravi.github.databinding.ActivityRepositoryDetailBinding;

public class RepositoryDetailActivity extends AppCompatActivity {

    private ActivityRepositoryDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRepositoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView nameTextView = binding.nameTextView;
        TextView ownerTextView = binding.ownerTextView;
        TextView descriptionTextView = binding.descriptionTextView;
        TextView languageTextView = binding.languageTextView;
        TextView starsTextView = binding.starsTextView;
        View languageColor = binding.languageColor;

        Intent intent = getIntent();
        String name = intent.getStringExtra("repository_name");
        String owner = intent.getStringExtra("owner_name");
        String description = intent.getStringExtra("description");
        String language = intent.getStringExtra("language");
        int starCount = intent.getIntExtra("star_count", 0);

        nameTextView.setText(name);
        ownerTextView.setText(owner);
        descriptionTextView.setText(description != null ? description : "No Description available");
        languageTextView.setText(language);
        starsTextView.setText(String.valueOf(starCount));

        int color = getColorForLanguage(language);
        languageColor.setBackgroundColor(color);

    }

    private int getColorForLanguage(String language) {
        switch (language != null ? language.toLowerCase() : "") {
            case "java": return getResources().getColor(R.color.javaColor);
            case "kotlin": return getResources().getColor(R.color.kotlinColor);
            default: return getResources().getColor(R.color.defaultColor);
        }
    }
}
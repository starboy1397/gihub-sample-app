package com.ravi.github;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference signOutPreference = findPreference("sign_out");
            if (signOutPreference != null) {
                signOutPreference.setOnPreferenceClickListener(preference -> {
                    showSignOutConfirmationDialog();  // Show confirmation dialog
                    return true;
                });
            }

        }

        private void showSignOutConfirmationDialog() {

            new AlertDialog.Builder(requireContext())
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        requireContext().getSharedPreferences(MainActivity.PREF_NAME, requireContext().MODE_PRIVATE)
                                .edit().putBoolean(MainActivity.IS_LOGGED_IN, false).apply();

                        // Sign out from Firebase
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(requireContext(), MainActivity.class));
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        }
    }
}
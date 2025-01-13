package com.ravi.github;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ravi.github.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> signInLauncher;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);

        // Configure the sign-in request
        signInRequest = new BeginSignInRequest.Builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId("888184185187-vri5uj32od10ro0drrujvpupf93v601f.apps.googleusercontent.com")
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .setAutoSelectEnabled(true)
                .build();

        // Initialize the ActivityResultLauncher
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        try {
                            // Extract SignInCredential
                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();
                            if (idToken != null) {
                                firebaseAuthWithGoogle(idToken);
                            } else {
                                Log.e(TAG, "Google ID Token is null");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error handling Google Sign-In result", e);
                        }
                    } else {
                        Log.w(TAG, "Google Sign-In canceled or failed.");
                    }
                }
        );

        // Set up the sign-in button click listener
        binding.signinBtn.setOnClickListener(v -> startGoogleSignIn());
    }

    private void startGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender())
                                .build();
                        signInLauncher.launch(intentSenderRequest);
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting Google Sign-In", e);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to initiate Google Sign-In", e);
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.i(TAG, "Google Sign-In successful. User: " + user.getDisplayName());
                            startActivity(new Intent(MainActivity.this, RepositoryListActivity.class));
                            finish();
                        }
                    } else {
                        Log.e(TAG, "Firebase Authentication with Google Sign-In failed", task.getException());
                    }
                });
    }
}

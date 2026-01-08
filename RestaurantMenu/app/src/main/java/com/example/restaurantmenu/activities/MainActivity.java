package com.example.restaurantmenu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmenu.R;
import com.example.restaurantmenu.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity serves as the login screen for the Restaurant Menu app.
 * Uses Firebase Anonymous Authentication to allow guest access.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setupClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMenu();
        }
    }

    private void setupClickListeners() {
        binding.btnGuestLogin.setOnClickListener(v -> signInAnonymously());
    }

    private void signInAnonymously() {
        showLoading(true);
        
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    showLoading(false);
                    
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInAnonymously:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(MainActivity.this, 
                                    "Welcome, Guest!", 
                                    Toast.LENGTH_SHORT).show();
                            navigateToMenu();
                        }
                    } else {
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(MainActivity.this, 
                                "Authentication failed. Please try again.", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnGuestLogin.setEnabled(!isLoading);
    }
}

package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.atamerica.databinding.ActivityAuthenticateBinding;
import com.example.atamerica.ui.login.LoginFragment;

public class AuthenticateActivity extends AppCompatActivity {

    ActivityAuthenticateBinding binding;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Prevent landscape mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new LoginFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.authenticateContainer, fragment);
        transaction.commit();
    }
}
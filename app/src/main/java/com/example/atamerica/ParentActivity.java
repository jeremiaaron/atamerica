package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.atamerica.databinding.ActivityParentBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.zip.Inflater;

public class ParentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ActivityParentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityParentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomePageFragment()).commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.home:
                        selectedFragment = new HomePageFragment();
                        break;
                    case R.id.upcoming:
                        selectedFragment = new UpcomingPageFragment();
                        break;
                    case R.id.archived:
                        selectedFragment = new ArchivePageFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            super.onBackPressed();
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void backActivity(View view) {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            super.onBackPressed();
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
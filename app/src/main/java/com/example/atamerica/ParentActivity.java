package com.example.atamerica;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.databinding.ActivityParentBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.atamerica.databinding.ActivityParentBinding binding = ActivityParentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomePageFragment()).commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            switch (item.getItemId()) {
                case (R.id.upcoming):
                    selectedFragment = new UpcomingPageFragment();
                    break;
                case (R.id.archived):
                    selectedFragment = new ArchivePageFragment();
                    break;
                default:
                    selectedFragment = new HomePageFragment();
                    break;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame_layout, selectedFragment, null);
            fragmentTransaction.commit();

            return true;
        });
    }
}
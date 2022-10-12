package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class HomePageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.home:
                        return true;
                    case R.id.upcoming:
                        startActivity(new Intent(getApplicationContext(), UpcomingPageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.archived:
                        startActivity(new Intent(getApplicationContext(), ArchivePageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        ViewPager viewPager = findViewById(R.id.viewPager);
        Adapter adapter = new Adapter(this);
        viewPager.setAdapter(adapter);
    }
}
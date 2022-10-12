package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UpcomingPageActivity extends AppCompatActivity {

//    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_page);

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.upcoming);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home:
//                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.upcoming:
//                        return true;
//                    case R.id.archived:
//                        startActivity(new Intent(getApplicationContext(), ArchivePageActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                }
//
//                return false;
//            }
//        });
    }
}
package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ArchivePageActivity extends AppCompatActivity {

//    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_page);

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.archived);
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
//                        startActivity(new Intent(getApplicationContext(), UpcomingPageActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.archived:
//                        return true;
//                }
//
//                return false;
//            }
//        });
    }
}
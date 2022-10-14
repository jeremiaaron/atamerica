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
import android.view.View;

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

    public void moveToProfile(View view) {
        Intent intent = new Intent(HomePageActivity.this, ProfilePageActivity.class);
        startActivity(intent);
    }

    public void moveToDetail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        startActivity(intent);
    }

    public void moveToEvt1Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt1_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt1_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt1_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt1_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt1_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt1_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt2Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt2_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt2_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt2_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt2_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt2_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt2_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt3Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt3_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt3_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt3_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt3_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt3_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt3_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt4Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt4_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt4_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt4_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt4_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt4_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt4_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt5Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt5_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt5_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt5_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt5_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt5_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt5_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt6Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt6_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt6_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt6_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt6_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt6_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt6_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt7Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt7_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt7_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt7_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt7_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt7_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt7_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }

    public void moveToEvt8Detail(View view) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        String titleStr = getResources().getString(R.string.evt8_title);
        intent.putExtra("title", titleStr);

        String descStr = getResources().getString(R.string.evt8_desc);
        intent.putExtra("desc", descStr);

        Integer imgId = R.drawable.evt8_detail_img;
        intent.putExtra("imgId", Integer.toString(imgId));

        String dateStr = getResources().getString(R.string.evt8_date);
        intent.putExtra("date", dateStr);

        String timeStr = getResources().getString(R.string.evt8_time);
        intent.putExtra("time", timeStr);

        String guestStr = getResources().getString(R.string.evt8_guest);
        intent.putExtra("guest", guestStr);

        startActivity(intent);
    }
}
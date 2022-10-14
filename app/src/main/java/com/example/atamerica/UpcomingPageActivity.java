package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UpcomingPageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.upcoming);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.upcoming:
                        return true;
                    case R.id.archived:
                        startActivity(new Intent(getApplicationContext(), ArchivePageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        final Button b1 = (Button) findViewById(R.id.filterEntertainment);
        final Button b2 = (Button) findViewById(R.id.filterEducation);
        final Button b3 = (Button) findViewById(R.id.filterMusic);
        final Button b4 = (Button) findViewById(R.id.filterEntrepreneur);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                b1.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b2.setBackgroundColor(ContextCompat.getColor(
                                    UpcomingPageActivity.this, R.color.light_gray));
                b3.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b4.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b1.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.white));
                b2.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b3.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b4.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                b2.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b1.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b3.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b4.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b2.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.white));
                b1.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b3.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b4.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                b3.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b1.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b2.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b4.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b3.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.white));
                b1.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b2.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b4.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                b4.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b1.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b2.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b3.setBackgroundColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.light_gray));
                b4.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.white));
                b1.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b2.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
                b3.setTextColor(ContextCompat.getColor(
                        UpcomingPageActivity.this, R.color.black));
            }
        });
    }

    public void moveToProfile(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, ProfilePageActivity.class);
        startActivity(intent);
    }

    public void moveToRegister(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, RegisterPageActivity.class);
        startActivity(intent);
    }

    public void moveToDetail(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
        startActivity(intent);
    }

    public void moveToEvt1Detail(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
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

    public void moveToEvt2Detail(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
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

    public void moveToEvt3Detail(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
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
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
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

    public void moveToEvt5Detail(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
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

    public void moveToEvt6Detail(View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
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
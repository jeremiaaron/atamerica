package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Arrays;
import java.util.List;

public class UpcomingPageActivity extends AppCompatActivity implements AdapterRecyclerUpcoming.OnEventUpcomingClickListener {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    AdapterRecyclerUpcoming adapter;

    List<String> evt_titles, evt_dates, evt_times, evt_guests, evt_descs;
    TypedArray evt_front_images_ids, evt_detail_images_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_page);

        //For page transition
        Fade fade = new Fade();
        fade.excludeTarget(R.id.bottomNavigationView, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.setDuration(250);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UpcomingPageActivity.this);

        // Define recycle view in the activity
        recyclerView = findViewById(R.id.recyclerViewUpcoming);

        // Retrieve event info from string arrays in strings xml and event images from drawables
        evt_titles = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Define recycler adapter for the recycler view
        adapter = new AdapterRecyclerUpcoming(this, evt_titles, evt_front_images_ids, this);

        // GridLayoutManager for grid layout of the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.upcoming);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class), options.toBundle());
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.upcoming:
                        return true;
                    case R.id.archived:
                        startActivity(new Intent(getApplicationContext(), ArchivePageActivity.class), options.toBundle());
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });
    }

    public void moveToProfile (View view) {
        Intent intent = new Intent(UpcomingPageActivity.this, ProfilePageActivity.class);
        startActivity(intent);
    }

    public void moveToRegister (View view){
        Intent intent = new Intent(UpcomingPageActivity.this, RegisterPageActivity.class);
        startActivity(intent);
    }

    public void moveToEvtDetail (View view){
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

    @Override
    public void onEventUpcomingClick(int position) {
        Intent intent = new Intent(UpcomingPageActivity.this, DetailPageActivity.class);
        intent.putExtra("title", evt_titles.get(position));
        intent.putExtra("date", evt_dates.get(position));
        intent.putExtra("time", evt_times.get(position));
        intent.putExtra("guest", evt_guests.get(position));
        intent.putExtra("desc", evt_descs.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_ids.getResourceId(position, 0)));
        startActivity(intent);
    }
}
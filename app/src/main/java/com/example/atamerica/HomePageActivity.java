package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;

public class HomePageActivity extends AppCompatActivity implements AdapterRecyclerHomeLike.OnEventHomeClickListener, AdapterRecyclerHomeTop.OnEventTopClickListener {

    RecyclerView recyclerViewHomeLike;
    RecyclerView recyclerViewHomeTop;
    AdapterRecyclerHomeLike adapterLike;
    AdapterRecyclerHomeTop adapterTop;

    List<String> evt_titles_like, evt_dates_like, evt_times_like, evt_guests_like, evt_descs_like;
    TypedArray evt_front_images_like_ids, evt_detail_images_like_ids;

    List<String> evt_titles_top, evt_dates_top, evt_times_top, evt_guests_top, evt_descs_top;
    TypedArray evt_front_images_top_ids, evt_detail_images_top_ids;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // For page transition
        Fade fade = new Fade();
        fade.excludeTarget(R.id.bottomNavigationView, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.setDuration(250);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomePageActivity.this);

        // Define recycle views in the activity (EYML and Top Events)
        recyclerViewHomeLike = findViewById(R.id.recyclerViewHomeLike);
        recyclerViewHomeTop = findViewById(R.id.recyclerViewHomeTop);

        // Retrieve eyml event info from string arrays in strings xml (event image ids from integer arrays)
        evt_titles_like = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates_like = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times_like = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests_like = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs_like = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_like_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_like_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Retrieve top event info from string arrays in strings xml (event image ids from integer arrays)
        evt_titles_top = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates_top = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times_top = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests_top = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs_top = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_top_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_top_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Define recycler adapter for each EYML and Top Events recycle views
        adapterLike = new AdapterRecyclerHomeLike(this, evt_titles_like, evt_front_images_like_ids, this);
        adapterTop = new AdapterRecyclerHomeTop(this, evt_titles_top, evt_front_images_top_ids, this);

        // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
        LinearLayoutManager linearLayoutManagerLike = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHomeLike.setLayoutManager(linearLayoutManagerLike);
        recyclerViewHomeLike.setAdapter(adapterLike);

        // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
        LinearLayoutManager linearLayoutManagerTop = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHomeTop.setLayoutManager(linearLayoutManagerTop);
        recyclerViewHomeTop.setAdapter(adapterTop);

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
                        startActivity(new Intent(getApplicationContext(), UpcomingPageActivity.class), options.toBundle());
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.archived:
                        startActivity(new Intent(getApplicationContext(), ArchivePageActivity.class), options.toBundle());
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

    @Override
    public void onEventLikeClick(int position) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        intent.putExtra("title", evt_titles_like.get(position));
        intent.putExtra("date", evt_dates_like.get(position));
        intent.putExtra("time", evt_times_like.get(position));
        intent.putExtra("guest", evt_guests_like.get(position));
        intent.putExtra("desc", evt_descs_like.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_like_ids.getResourceId(position, 0)));
        startActivity(intent);
    }

    @Override
    public void onEventTopClick(int position) {
        Intent intent = new Intent(HomePageActivity.this, DetailPageActivity.class);
        intent.putExtra("title", evt_titles_top.get(position));
        intent.putExtra("date", evt_dates_top.get(position));
        intent.putExtra("time", evt_times_top.get(position));
        intent.putExtra("guest", evt_guests_top.get(position));
        intent.putExtra("desc", evt_descs_top.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_top_ids.getResourceId(position, 0)));
        startActivity(intent);
    }
}
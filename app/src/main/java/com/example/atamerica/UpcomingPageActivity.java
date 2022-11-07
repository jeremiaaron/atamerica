package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class UpcomingPageActivity extends AppCompatActivity implements AdapterRecyclerUpcoming.OnEventUpcomingClickListener {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    AdapterRecyclerUpcoming adapter;
    Button categoryButton;
    CheckBox cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy, cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;

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

        // Category button on click
        categoryButton = findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UpcomingPageActivity.this);

                View bottomSheetView = LayoutInflater.from(UpcomingPageActivity.this).inflate(
                        R.layout.bottom_sheet_layout, (LinearLayout) findViewById(R.id.bottomSheetContainer)
                );

                TextView clearFilter = (TextView) bottomSheetView.findViewById(R.id.clear_filter);
                clearFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearFilter();
                    }
                });

                cbMusic = (CheckBox) bottomSheetView.findViewById(R.id.musicCheck); setOnClick(cbMusic);
                cbMovie = (CheckBox) bottomSheetView.findViewById(R.id.movieCheck); setOnClick(cbMovie);
                cbEducation = (CheckBox) bottomSheetView.findViewById(R.id.educationCheck); setOnClick(cbEducation);
                cbScience = (CheckBox) bottomSheetView.findViewById(R.id.scienceCheck); setOnClick(cbScience);
                cbDemocracy = (CheckBox) bottomSheetView.findViewById(R.id.democracyCheck); setOnClick(cbDemocracy);
                cbEntrepreneurship = (CheckBox) bottomSheetView.findViewById(R.id.entrepreneurshipCheck); setOnClick(cbEntrepreneurship);
                cbArts = (CheckBox) bottomSheetView.findViewById(R.id.artsCheck); setOnClick(cbArts);
                cbProtecting = (CheckBox) bottomSheetView.findViewById(R.id.protectingCheck); setOnClick(cbProtecting);
                cbWomen = (CheckBox) bottomSheetView.findViewById(R.id.womenCheck); setOnClick(cbWomen);
                cbYseali = (CheckBox) bottomSheetView.findViewById(R.id.ysealiCheck); setOnClick(cbYseali);

                Button applyButton = (Button) bottomSheetView.findViewById(R.id.apply_button);
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(UpcomingPageActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

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

    public void setOnClick (CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CompoundButtonCompat.setButtonTintList(
                        checkBox,
                        ContextCompat.getColorStateList(UpcomingPageActivity.this, R.color.checkbox_color)
                );
            }
        });
    }

    public void clearFilter () {
        cbMusic.setChecked(false);
        cbMovie.setChecked(false);
        cbEducation.setChecked(false);
        cbScience.setChecked(false);
        cbDemocracy.setChecked(false);
        cbEntrepreneurship.setChecked(false);
        cbArts.setChecked(false);
        cbProtecting.setChecked(false);
        cbWomen.setChecked(false);
        cbYseali.setChecked(false);
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
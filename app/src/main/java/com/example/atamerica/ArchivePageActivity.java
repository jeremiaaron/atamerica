package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class ArchivePageActivity extends AppCompatActivity implements AdapterRecyclerArchive.OnEventArchiveClickListener {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    AdapterRecyclerArchive adapter;
    Button categoryButton, sortButton;
    CheckBox cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy, cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;
    RadioButton rbNewest, rbLatest;

    List<String> evt_titles, evt_dates, evt_times, evt_guests, evt_descs;
    TypedArray evt_front_images_ids, evt_detail_images_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_page);

        //For page transition
        Fade fade = new Fade();
        fade.excludeTarget(R.id.applicationLogo, true);
        fade.excludeTarget(R.id.btnAccount, true);
        fade.excludeTarget(R.id.bottomNavigationView, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.setDuration(250);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ArchivePageActivity.this);

        // Define recycle view in the activity
        recyclerView = findViewById(R.id.recyclerViewArchive);

        // Retrieve event info from string arrays in strings xml and event images from drawables
        evt_titles = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Define recycler adapter for the recycler view
        adapter = new AdapterRecyclerArchive(this, evt_titles, evt_front_images_ids, this);

        // GridLayoutManager for grid layout of the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        // Category button on click
        categoryButton = findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ArchivePageActivity.this);

                View bottomSheetView = LayoutInflater.from(ArchivePageActivity.this).inflate(
                        R.layout.bottom_sheet_category_layout, (LinearLayout) findViewById(R.id.bottomSheetContainer)
                );

                TextView clearFilter = (TextView) bottomSheetView.findViewById(R.id.clear_filter);
                clearFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearFilterCheck();
                    }
                });

                cbMusic = bottomSheetView.findViewById(R.id.musicCheck); setOnClickCheck(cbMusic);
                cbMovie = bottomSheetView.findViewById(R.id.movieCheck); setOnClickCheck(cbMovie);
                cbEducation = bottomSheetView.findViewById(R.id.educationCheck); setOnClickCheck(cbEducation);
                cbScience = bottomSheetView.findViewById(R.id.scienceCheck); setOnClickCheck(cbScience);
                cbDemocracy = bottomSheetView.findViewById(R.id.democracyCheck); setOnClickCheck(cbDemocracy);
                cbEntrepreneurship = bottomSheetView.findViewById(R.id.entrepreneurshipCheck); setOnClickCheck(cbEntrepreneurship);
                cbArts = bottomSheetView.findViewById(R.id.artsCheck); setOnClickCheck(cbArts);
                cbProtecting = bottomSheetView.findViewById(R.id.protectingCheck); setOnClickCheck(cbProtecting);
                cbWomen = bottomSheetView.findViewById(R.id.womenCheck); setOnClickCheck(cbWomen);
                cbYseali = bottomSheetView.findViewById(R.id.ysealiCheck); setOnClickCheck(cbYseali);

                Button applyButton = (Button) bottomSheetView.findViewById(R.id.apply_button);
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ArchivePageActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        // Sort button on click
        sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ArchivePageActivity.this);

                View bottomSheetView = LayoutInflater.from(ArchivePageActivity.this).inflate(
                        R.layout.bottom_sheet_sort_layout, (LinearLayout) findViewById(R.id.bottomSheetContainer)
                );

                rbNewest = bottomSheetView.findViewById(R.id.newestRadio); setOnClickRadio(rbNewest);
                rbLatest = bottomSheetView.findViewById(R.id.latestRadio); setOnClickRadio(rbLatest);

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.archived);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomePageFragment.class), options.toBundle());
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.upcoming:
                        startActivity(new Intent(getApplicationContext(), UpcomingPageFragment.class), options.toBundle());
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.archived:
                        return true;
                }

                return false;
            }
        });
    }

    public void setOnClickCheck (CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CompoundButtonCompat.setButtonTintList(
                        checkBox,
                        ContextCompat.getColorStateList(ArchivePageActivity.this, R.color.checkbox_color)
                );
            }
        });
    }

    public void setOnClickRadio (RadioButton radioButton) {
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CompoundButtonCompat.setButtonTintList(
                        radioButton,
                        ContextCompat.getColorStateList(ArchivePageActivity.this, R.color.checkbox_color)
                );
            }
        });
    }

    public void clearFilterCheck () {
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
        Intent intent = new Intent(ArchivePageActivity.this, ProfilePageActivity.class);
        startActivity(intent);
    }

    public void moveToRegister (View view){
        Intent intent = new Intent(ArchivePageActivity.this, RegisterPageFragment.class);
        startActivity(intent);
    }

    @Override
    public void onEventArchiveClick(int position) {
        Intent intent = new Intent(ArchivePageActivity.this, ArchiveDetailPageActivity.class);
        intent.putExtra("title", evt_titles.get(position));
        intent.putExtra("date", evt_dates.get(position));
        intent.putExtra("time", evt_times.get(position));
        intent.putExtra("guest", evt_guests.get(position));
        intent.putExtra("desc", evt_descs.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_ids.getResourceId(position, 0)));
        startActivity(intent);
    }
}
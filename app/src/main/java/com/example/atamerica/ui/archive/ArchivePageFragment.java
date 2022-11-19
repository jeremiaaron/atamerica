package com.example.atamerica.ui.archive;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentArchivePageBinding;
import com.example.atamerica.ui.archive.AdapterRecyclerArchive;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class ArchivePageFragment extends Fragment implements AdapterRecyclerArchive.OnEventArchiveClickListener {

    RecyclerView recyclerView;
    AdapterRecyclerArchive adapter;
    Button categoryButton, sortButton;
    ImageView profileButton;

    CheckBox cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy, cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;
    RadioButton rbNewest, rbLatest;

    List<String> evt_titles, evt_dates, evt_times, evt_guests, evt_descs;
    TypedArray evt_front_images_ids, evt_detail_images_ids;

    FragmentArchivePageBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentArchivePageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        profileButton = mView.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChildActivity.class);
                intent.putExtra("destination", "profileFragment");
                startActivity(intent);
            }
        });

        // Define recycle view in the activity
        recyclerView = mView.findViewById(R.id.recyclerViewArchive);

        // Retrieve event info from string arrays in strings xml and event images from drawables
        evt_titles = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Define recycler adapter for the recycler view
        adapter = new AdapterRecyclerArchive(getActivity(), evt_titles, evt_front_images_ids, this);

        // GridLayoutManager for grid layout of the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        // Category button on click
        categoryButton = mView.findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());

                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.bottom_sheet_category_layout, (LinearLayout) mView.findViewById(R.id.bottomSheetContainer)
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
                        Toast.makeText(getActivity(), "Applied", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        // Sort button on click
        sortButton = mView.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());

                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.bottom_sheet_sort_layout, (LinearLayout) mView.findViewById(R.id.bottomSheetContainer)
                );

                rbNewest = bottomSheetView.findViewById(R.id.newestRadio); setOnClickRadio(rbNewest);
                rbLatest = bottomSheetView.findViewById(R.id.latestRadio); setOnClickRadio(rbLatest);

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        return mView;
    }

    public void setOnClickCheck (CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CompoundButtonCompat.setButtonTintList(
                        checkBox,
                        ContextCompat.getColorStateList(getActivity(), R.color.checkbox_color)
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
                        ContextCompat.getColorStateList(getActivity(), R.color.checkbox_color)
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

    @Override
    public void onEventArchiveClick(int position) {
        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "archiveDetailPageFragment");
        intent.putExtra("title", evt_titles.get(position));
        intent.putExtra("desc", evt_descs.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_ids.getResourceId(position, 0)));
        intent.putExtra("date", evt_dates.get(position));
        intent.putExtra("time", evt_times.get(position));
        intent.putExtra("guest", evt_guests.get(position));
        startActivity(intent);
    }
}
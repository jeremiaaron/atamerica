package com.example.atamerica.ui.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.controllers.ArchiveController;
import com.example.atamerica.databinding.FragmentArchivePageBinding;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class ArchivePageFragment extends Fragment implements AdapterRecyclerArchive.OnEventArchiveClickListener {

    private ConstraintLayout            topBarLayout, filterLayout;
    private RecyclerView                recyclerView;
    private AdapterRecyclerArchive      adapter;

    private CheckBox                    cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy, cbEntrepreneurship,
                                        cbArts, cbProtecting, cbWomen, cbYseali;
    private RadioButton                 rbNewest, rbLatest;

    private List<VwEventThumbnailModel> events;

    private CircularProgressIndicator progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FragmentArchivePageBinding binding = FragmentArchivePageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Define recycle view in the activity
        recyclerView        = mView.findViewById(R.id.recyclerViewArchive);
        topBarLayout        = mView.findViewById(R.id.topBarLayout);
        filterLayout        = mView.findViewById(R.id.filterLayout);
        progressIndicator   = mView.findViewById(R.id.progressIndicator);

        // Asynchronously bind
        new TaskRunner().executeAsyncPool(new ArchiveController.GetEvent(), (data) -> {
            if (!HelperClass.isEmpty(data)) {
                new TaskRunner().executeAsyncPool(new ArchiveController.ConvertToThumbnailEvent(data), (filterEvents) -> {
                    if (!HelperClass.isEmpty(filterEvents)) {
                        this.events = new ArrayList<>(filterEvents);

                        // Define recycler adapter for the recycler view
                        adapter = new AdapterRecyclerArchive(getActivity(), this.events, this);

                        // GridLayoutManager for grid layout of the recycler view
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setVisibility(View.VISIBLE);
                        topBarLayout.setVisibility(View.VISIBLE);
                        filterLayout.setVisibility(View.VISIBLE);
                        progressIndicator.setVisibility(View.GONE);
                    }
                });
            }
            else {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
        });

        // Profile button
        ImageView profileButton = mView.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChildActivity.class);
            intent.putExtra("destination", "profileFragment");
            startActivity(intent);
        });

        // Category button on click
        Button categoryButton = mView.findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());

            View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.bottom_sheet_category_layout, mView.findViewById(R.id.bottomSheetContainer)
            );

            TextView clearFilter = bottomSheetView.findViewById(R.id.clear_filter);
            clearFilter.setOnClickListener(view12 -> clearFilterCheck());

            cbMusic             = bottomSheetView.findViewById(R.id.musicCheck); setOnClickCheck(cbMusic);
            cbMovie             = bottomSheetView.findViewById(R.id.movieCheck); setOnClickCheck(cbMovie);
            cbEducation         = bottomSheetView.findViewById(R.id.educationCheck); setOnClickCheck(cbEducation);
            cbScience           = bottomSheetView.findViewById(R.id.scienceCheck); setOnClickCheck(cbScience);
            cbDemocracy         = bottomSheetView.findViewById(R.id.democracyCheck); setOnClickCheck(cbDemocracy);
            cbEntrepreneurship  = bottomSheetView.findViewById(R.id.entrepreneurshipCheck); setOnClickCheck(cbEntrepreneurship);
            cbArts              = bottomSheetView.findViewById(R.id.artsCheck); setOnClickCheck(cbArts);
            cbProtecting        = bottomSheetView.findViewById(R.id.protectingCheck); setOnClickCheck(cbProtecting);
            cbWomen             = bottomSheetView.findViewById(R.id.womenCheck); setOnClickCheck(cbWomen);
            cbYseali            = bottomSheetView.findViewById(R.id.ysealiCheck); setOnClickCheck(cbYseali);

            Button applyButton = bottomSheetView.findViewById(R.id.apply_button);
            applyButton.setOnClickListener(view1 -> {
                Toast.makeText(getActivity(), "Applied", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        // Sort button on click
        Button sortButton = mView.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());

            View bottomSheetView = LayoutInflater.from(requireActivity()).inflate(
                    R.layout.bottom_sheet_sort_layout, mView.findViewById(R.id.bottomSheetContainer)
            );

            rbNewest = bottomSheetView.findViewById(R.id.newestRadio); setOnClickRadio(rbNewest);
            rbLatest = bottomSheetView.findViewById(R.id.latestRadio); setOnClickRadio(rbLatest);

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return mView;
    }

    public void setOnClickCheck (CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> CompoundButtonCompat.setButtonTintList(
                checkBox,
                ContextCompat.getColorStateList(requireActivity(), R.color.checkbox_color)
        ));
    }

    public void setOnClickRadio (RadioButton radioButton) {
        radioButton.setOnCheckedChangeListener((compoundButton, isChecked) -> CompoundButtonCompat.setButtonTintList(
                radioButton,
                ContextCompat.getColorStateList(requireActivity(), R.color.checkbox_color)
        ));
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
        intent.putExtra("event_id", this.events.get(position).EventId);
        startActivity(intent);
    }
}
package com.example.atamerica.ui.upcoming;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.cache.ConfigCache;
import com.example.atamerica.controllers.UpcomingController;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpcomingPageFragment extends Fragment implements AdapterRecyclerUpcoming.OnEventUpcomingClickListener {

    private RecyclerView                    recyclerView;
    private Button                          categoryButton, sortButton;
    private CheckBox                        cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy,
                                            cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;
    private RadioButton                     rbNewest, rbLatest;
    private ImageView                       profileButton;

    AdapterRecyclerUpcoming                 adapter;
    private FragmentUpcomingPageBinding     binding;

    private List<VwAllEventModel>           events;
    private List<VwEventThumbnailModel>     thumbnailModels;

    private List<String>                    categories;
    private int                             sortConfig;

    private CircularProgressIndicator       progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"SourceLockedOrientationActivity", "NotifyDataSetChanged"})
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentUpcomingPageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Get views
        recyclerView        = rootView.findViewById(R.id.recyclerViewUpcoming);
        categoryButton      = rootView.findViewById(R.id.categoryButton);
        sortButton          = rootView.findViewById(R.id.sortButton);
        profileButton       = rootView.findViewById(R.id.profileButton);
        progressIndicator   = rootView.findViewById(R.id.progressIndicator);

        // Asynchronously bind recycler view
        new TaskRunner().executeAsyncPool(new UpcomingController.GetEvents(), (data) -> {
            if (!HelperClass.isEmpty(data)) {
                this.events = new ArrayList<>(data);

                new TaskRunner().executeAsyncPool(new UpcomingController.ConvertToThumbnailEvent(data), (filteredEvents) -> {
                    this.thumbnailModels = new ArrayList<>(filteredEvents);

                    adapter = new AdapterRecyclerUpcoming(getActivity(), thumbnailModels, this);

                    // GridLayoutManager for grid layout of the recycler view
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);
                    progressIndicator.setVisibility(View.GONE);
                });
            }
            else {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
        });

        // Profile button on click
        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChildActivity.class);
            intent.putExtra("destination", "profileFragment");
            startActivity(intent);
        });

        // Category button on click
        categoryButton.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());

            View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.bottom_sheet_category_layout, rootView.findViewById(R.id.bottomSheetContainer)
            );

            categories = ConfigCache.Categories;

            TextView clearFilter = bottomSheetView.findViewById(R.id.clear_filter);
            clearFilter.setOnClickListener(view12 -> clearFilterCheck());

            cbMusic             = bottomSheetView.findViewById(R.id.musicCheck);            setOnClickCheck(cbMusic);
            cbMovie             = bottomSheetView.findViewById(R.id.movieCheck);            setOnClickCheck(cbMovie);
            cbEducation         = bottomSheetView.findViewById(R.id.educationCheck);        setOnClickCheck(cbEducation);
            cbScience           = bottomSheetView.findViewById(R.id.scienceCheck);          setOnClickCheck(cbScience);
            cbDemocracy         = bottomSheetView.findViewById(R.id.democracyCheck);        setOnClickCheck(cbDemocracy);
            cbEntrepreneurship  = bottomSheetView.findViewById(R.id.entrepreneurshipCheck); setOnClickCheck(cbEntrepreneurship);
            cbArts              = bottomSheetView.findViewById(R.id.artsCheck);             setOnClickCheck(cbArts);
            cbProtecting        = bottomSheetView.findViewById(R.id.protectingCheck);       setOnClickCheck(cbProtecting);
            cbWomen             = bottomSheetView.findViewById(R.id.womenCheck);            setOnClickCheck(cbWomen);
            cbYseali            = bottomSheetView.findViewById(R.id.ysealiCheck);           setOnClickCheck(cbYseali);

            for (String category : categories) {
                if (Objects.equals(category, "Music and Culture")) cbMusic.setChecked(true);
                if (Objects.equals(category, "Movie Screening")) cbMovie.setChecked(true);
                if (Objects.equals(category, "American Education and Skills")) cbEducation.setChecked(true);
                if (Objects.equals(category, "Science, Technology, and Innovation")) cbScience.setChecked(true);
                if (Objects.equals(category, "Democracy and Governance")) cbDemocracy.setChecked(true);
                if (Objects.equals(category, "Entrepreneurship and Business")) cbEntrepreneurship.setChecked(true);
                if (Objects.equals(category, "Arts and Culture")) cbArts.setChecked(true);
                if (Objects.equals(category, "Protecting Natural Resources")) cbProtecting.setChecked(true);
                if (Objects.equals(category, "Women's Empowerment")) cbWomen.setChecked(true);
                if (Objects.equals(category, "YSEALI and Alumi")) cbYseali.setChecked(true);
            }

            Button applyButton = bottomSheetView.findViewById(R.id.apply_button);
            applyButton.setOnClickListener(view1 -> {
                categories = new ArrayList<>();

                if (cbMusic.isChecked()) categories.add("Music and Culture");
                if (cbMovie.isChecked()) categories.add("Movie Screening");
                if (cbEducation.isChecked()) categories.add("American Education and Skills");
                if (cbScience.isChecked()) categories.add("Science, Technology, and Innovation");
                if (cbDemocracy.isChecked()) categories.add("Democracy and Governance");
                if (cbEntrepreneurship.isChecked()) categories.add("Entrepreneurship and Business");
                if (cbArts.isChecked()) categories.add("Arts and Culture");
                if (cbProtecting.isChecked()) categories.add("Protecting Natural Resources");
                if (cbWomen.isChecked()) categories.add("Women's Empowerment");
                if (cbYseali.isChecked()) categories.add("YSEALI and Alumi");

                ConfigCache.Categories = categories;

                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(events, categories, sortConfig), (data) -> {
                    this.thumbnailModels.clear();
                    this.thumbnailModels.addAll(data);
                    adapter.notifyDataSetChanged();
                });

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        // Sort button on click
        sortButton.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet_sort_layout, rootView.findViewById(R.id.bottomSheetContainer));

            this.sortConfig = ConfigCache.SortConfig;

            rbNewest = bottomSheetView.findViewById(R.id.newestRadio); setOnClickRadio(rbNewest);
            rbLatest = bottomSheetView.findViewById(R.id.latestRadio); setOnClickRadio(rbLatest);

            if (sortConfig == 1) rbLatest.setChecked(true);
            else rbNewest.setChecked(true);

            Button applyButton = bottomSheetView.findViewById(R.id.apply_button);
            applyButton.setOnClickListener(view1 -> {
                this.sortConfig = (rbNewest.isChecked()) ? -1 : 1;

                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(events, categories, sortConfig), (data) -> {
                    this.thumbnailModels.clear();
                    this.thumbnailModels.addAll(data);
                    adapter.notifyDataSetChanged();
                });

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return rootView;
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
    public void onEventUpcomingClick(int position) {
        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("event_id", thumbnailModels.get(position).EventId);
        startActivity(intent);
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
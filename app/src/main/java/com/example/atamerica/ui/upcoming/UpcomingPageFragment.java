package com.example.atamerica.ui.upcoming;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private CheckBox                        cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy,
                                            cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;
    private RadioButton                     rbNewest, rbLatest;
    private SearchView                      searchBar;

    AdapterRecyclerUpcoming                 adapter;
    private FragmentUpcomingPageBinding     binding;

    private List<VwAllEventModel>           events;
    private List<VwEventThumbnailModel>     thumbnailModels;

    private boolean                         isQuerying, queryAble;
    private CircularProgressIndicator       progressIndicator;

    private SwipeRefreshLayout              swipeRefreshLayout;

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
        recyclerView            = rootView.findViewById(R.id.recyclerViewUpcoming);
        Button categoryButton   = rootView.findViewById(R.id.categoryButton);
        Button sortButton       = rootView.findViewById(R.id.sortButton);
        ImageView profileButton = rootView.findViewById(R.id.profileButton);
        searchBar               = rootView.findViewById(R.id.searchBar);
        progressIndicator       = rootView.findViewById(R.id.progressIndicator);
        swipeRefreshLayout      = rootView.findViewById(R.id.swipeRefreshLayout);

        events                  = new ArrayList<>();
        thumbnailModels         = new ArrayList<>();
        isQuerying              = false;
        queryAble               = true;

        // Asynchronously bind recycler view
        new TaskRunner().executeAsyncPool(new UpcomingController.GetEvents(false), (data) -> {
            if (!HelperClass.isEmpty(data)) {
                this.events.clear();
                this.events.addAll(data);

                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(data, null), (filteredEvents) -> {
                    this.thumbnailModels.clear();
                    this.thumbnailModels.addAll(filteredEvents);

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

            for (String category : ConfigCache.UpcomingCategories) {
                if (Objects.equals(category, "Music and Culture")) cbMusic.setChecked(true);
                if (Objects.equals(category, "Movie Screening")) cbMovie.setChecked(true);
                if (Objects.equals(category, "American Education and Skills")) cbEducation.setChecked(true);
                if (Objects.equals(category, "Science, Technology, and Innovation")) cbScience.setChecked(true);
                if (Objects.equals(category, "Democracy and Governance")) cbDemocracy.setChecked(true);
                if (Objects.equals(category, "Entrepreneurship and Business")) cbEntrepreneurship.setChecked(true);
                if (Objects.equals(category, "Arts and Culture")) cbArts.setChecked(true);
                if (Objects.equals(category, "Protecting Natural Resources")) cbProtecting.setChecked(true);
                if (Objects.equals(category, "Women's Empowerment")) cbWomen.setChecked(true);
                if (Objects.equals(category, "YSEALI and Alumni")) cbYseali.setChecked(true);
            }

            Button applyButton = bottomSheetView.findViewById(R.id.apply_button);
            applyButton.setOnClickListener(view1 -> {
                ConfigCache.UpcomingCategories.clear();

                if (cbMusic.isChecked()) ConfigCache.UpcomingCategories.add("Music and Culture");
                if (cbMovie.isChecked()) ConfigCache.UpcomingCategories.add("Movie Screening");
                if (cbEducation.isChecked()) ConfigCache.UpcomingCategories.add("American Education and Skills");
                if (cbScience.isChecked()) ConfigCache.UpcomingCategories.add("Science, Technology, and Innovation");
                if (cbDemocracy.isChecked()) ConfigCache.UpcomingCategories.add("Democracy and Governance");
                if (cbEntrepreneurship.isChecked()) ConfigCache.UpcomingCategories.add("Entrepreneurship and Business");
                if (cbArts.isChecked()) ConfigCache.UpcomingCategories.add("Arts and Culture");
                if (cbProtecting.isChecked()) ConfigCache.UpcomingCategories.add("Protecting Natural Resources");
                if (cbWomen.isChecked()) ConfigCache.UpcomingCategories.add("Women's Empowerment");
                if (cbYseali.isChecked()) ConfigCache.UpcomingCategories.add("YSEALI and Alumni");

                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(events, searchBar.getQuery().toString()), (data) -> {
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

            rbNewest = bottomSheetView.findViewById(R.id.newestRadio); setOnClickRadio(rbNewest);
            rbLatest = bottomSheetView.findViewById(R.id.latestRadio); setOnClickRadio(rbLatest);

            if (ConfigCache.UpcomingSortConfig == 1) rbLatest.setChecked(true);
            else rbNewest.setChecked(true);

            Button applyButton = bottomSheetView.findViewById(R.id.apply_button);
            applyButton.setOnClickListener(view1 -> {
                ConfigCache.UpcomingSortConfig = (rbNewest.isChecked()) ? -1 : 1;

                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(events, searchBar.getQuery().toString()), (data) -> {
                    this.thumbnailModels.clear();
                    this.thumbnailModels.addAll(data);
                    adapter.notifyDataSetChanged();
                });

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        // Search event bar
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Calls for filter events
                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(events, newText), (data) -> {
                    thumbnailModels.clear();
                    thumbnailModels.addAll(data);
                    adapter.notifyDataSetChanged();
                });

                return true;
            }
        });

        // On scroll bottom reached listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // direction integers: -1 for up, 1 for down, 0 will always return false.
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !isQuerying) {
                    if (queryAble) {
                         progressIndicator.setVisibility(View.VISIBLE);

                        isQuerying = true;
                        ConfigCache.UpcomingScrollIndex += 1;

                        // Asynchronously bind recycler view
                        new TaskRunner().executeAsyncPool(new UpcomingController.GetEvents(true), (data) -> {
                            events.clear();
                            events.addAll(data);
                            queryAble = ConfigCache.UpcomingQueryable;

                            new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(data, searchBar.getQuery().toString()), (filteredFilteredEvents) -> {
                                thumbnailModels.clear();
                                thumbnailModels.addAll(filteredFilteredEvents);
                                adapter.notifyDataSetChanged();
                                isQuerying = false;
                            });

                             progressIndicator.setVisibility(View.GONE);
                        });
                    }
                }
            }
        });

        // on below line we are adding refresh listener
        // for our swipe to refresh method.
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // on below line we are setting is refreshing to false.
            swipeRefreshLayout.setRefreshing(false);

            // Resetting row number in cache to fetch new events
            int currentRow = ConfigCache.UpcomingScrollIndex;
            ConfigCache.UpcomingScrollIndex = 0;

            // Fetch new event and refresh adapter
            new TaskRunner().executeAsyncPool(new UpcomingController.GetEvents(true), (data) -> {
                events.clear();
                events.addAll(data);
                ConfigCache.UpcomingScrollIndex = currentRow;

                new TaskRunner().executeAsyncPool(new UpcomingController.FilterEvents(data, searchBar.getQuery().toString()), (filteredFilteredEvents) -> {
                    thumbnailModels.clear();
                    thumbnailModels.addAll(filteredFilteredEvents);
                    adapter.notifyDataSetChanged();
                });
            });
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
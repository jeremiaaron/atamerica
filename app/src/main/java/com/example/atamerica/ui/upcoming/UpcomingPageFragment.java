package com.example.atamerica.ui.upcoming;

import android.Manifest;
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
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;
import java.util.concurrent.Callable;

public class UpcomingPageFragment extends Fragment implements AdapterRecyclerUpcoming.OnEventUpcomingClickListener {

    private class QueryUpcomingEvent implements Callable<Boolean> {

        @Override
        public Boolean call() {
            try {
                // Check for cache
                if (!HelperClass.isEmpty(EventItemCache.UpcomingEventList)) {
                    models = EventItemCache.UpcomingEventList;
                }
                else {
                    models = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwEventThumbnail; ", VwEventThumbnailModel.class, null);
                    if (!HelperClass.isEmpty(models)) {
                        EventItemCache.UpcomingEventList = models;
                    }
                }

                return (!HelperClass.isEmpty(models));
            }
            catch (Exception e) {
                Log.e("ERROR", "Error querying event items!");
                e.printStackTrace();
            }

            return false;
        }
    }

    private RecyclerView                    recyclerView;
    private Button                          categoryButton, sortButton;
    private CheckBox                        cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy,
                                            cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;
    private RadioButton                     rbNewest, rbLatest;
    private ImageView                       profileButton;

    private AdapterRecyclerUpcoming         adapter;
    private FragmentUpcomingPageBinding     binding;

    private List<VwEventThumbnailModel>     models;

    private CircularProgressIndicator       progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // request for current activity to allow internet connection
        // this must be in every activity/fragments
        ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding = FragmentUpcomingPageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Get views
        recyclerView        = rootView.findViewById(R.id.recyclerViewUpcoming);
        categoryButton      = rootView.findViewById(R.id.categoryButton);
        sortButton          = rootView.findViewById(R.id.sortButton);
        profileButton       = rootView.findViewById(R.id.profileButton);
        progressIndicator   = rootView.findViewById(R.id.progressIndicator);

        // Asynchronously bind recycler view
        new TaskRunner().executeAsyncPool(new QueryUpcomingEvent(), (data) -> {
            if (data != null && data) {
                progressIndicator.setVisibility(View.GONE);
                adapter = new AdapterRecyclerUpcoming(getActivity(), models, this);

                // GridLayoutManager for grid layout of the recycler view
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
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

            Button applyButton = bottomSheetView.findViewById(R.id.apply_button);
            applyButton.setOnClickListener(view1 -> {
                Toast.makeText(getActivity(), "Applied", Toast.LENGTH_SHORT).show();
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
        intent.putExtra("", models.get(position).EventId);
        startActivity(intent);
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.atamerica;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.example.atamerica.java_class.DataHelper;
import com.example.atamerica.models.AppEventModel;
import com.example.atamerica.models.EventDocumentModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpcomingPageFragment extends Fragment implements AdapterRecyclerUpcoming.OnEventUpcomingClickListener {

    @SuppressLint("StaticFieldLeak")
    public class BindRecyclerTask extends AsyncTask<Void, Void, Void> {

        AdapterRecyclerUpcoming.OnEventUpcomingClickListener onEventClickListener;

        public BindRecyclerTask(AdapterRecyclerUpcoming.OnEventUpcomingClickListener onEventClickListener) {
            this.onEventClickListener = onEventClickListener;
        }

        @Override
        protected void onPreExecute() {
            progressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            models = DataHelper.Query.ReturnAsObjectList("SELECT * FROM AppEvent ORDER BY EventId ASC; ", AppEventModel.class, null);
            modelDocuments = DataHelper.Query.ReturnAsObjectList("SELECT * FROM EventDocument WHERE Title = 'thumbnail' ORDER BY EventId; ", EventDocumentModel.class, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressIndicator.setVisibility(View.GONE);

            adapter = new AdapterRecyclerUpcoming(getActivity(), models, modelDocuments, onEventClickListener);

            // GridLayoutManager for grid layout of the recycler view
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
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
    private List<AppEventModel>             models;
    private List<EventDocumentModel>        modelDocuments;

    private CircularProgressIndicator progressIndicator;

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
        new BindRecyclerTask(this).execute();

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
        Date date = new Date();
        date.setTime(models.get(position).EventStartTime.getTime());

        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("title", models.get(position).EventName);
        intent.putExtra("desc", models.get(position).EventDescription);
        intent.putExtra("imgId", modelDocuments.get(position).Path);
        intent.putExtra("date", new SimpleDateFormat("EEEEE, dd MMMMM yyyy", Locale.ENGLISH).format(date));
        intent.putExtra("time", new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date));
        intent.putExtra("guest", "");
        startActivity(intent);
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
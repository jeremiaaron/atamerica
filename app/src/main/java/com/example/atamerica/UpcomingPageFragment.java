package com.example.atamerica;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atamerica.databinding.FragmentHomePageBinding;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.example.atamerica.java_class.DataHelper;
import com.example.atamerica.models.AppEventModel;
import com.example.atamerica.models.EventDocumentModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UpcomingPageFragment extends Fragment implements AdapterRecyclerUpcoming.OnEventUpcomingClickListener {

    private RecyclerView                    recyclerView;
    private Button                          categoryButton, sortButton;
    private CheckBox                        cbMusic, cbMovie, cbEducation, cbScience, cbDemocracy,
                                            cbEntrepreneurship, cbArts, cbProtecting, cbWomen, cbYseali;
    private RadioButton                     rbNewest, rbLatest;
    private ImageView                       profileButton;

    private List<String>                    evt_titles, evt_dates, evt_times, evt_guests, evt_descs;
    private TypedArray                      evt_front_images_ids, evt_detail_images_ids;

    private AdapterRecyclerUpcoming         adapter;
    private FragmentUpcomingPageBinding     binding;
    private List<AppEventModel>             models;
    private List<EventDocumentModel>        modelDocuments;

    private CircularProgressIndicator progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        // Load variables async
        Thread load = new Thread(() -> {
            models = DataHelper.Query.ReturnAsObjectList("SELECT * FROM AppEvent ORDER BY EventId ASC; ", AppEventModel.class, null);
            modelDocuments = DataHelper.Query.ReturnAsObjectList("SELECT * FROM EventDocument WHERE Title = 'thumbnail' ORDER BY EventId; ", EventDocumentModel.class, null);
        });
        load.start();

        try {
            load.join();
            progressIndicator.setVisibility(View.GONE);

            // Define recycler adapter for the recycler view
            adapter = new AdapterRecyclerUpcoming(getActivity(), models, modelDocuments, this);

            // GridLayoutManager for grid layout of the recycler view
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Profile button on click
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChildActivity.class);
                intent.putExtra("destination", "profileFragment");
                startActivity(intent);
            }
        });

        // Category button on click
        categoryButton.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());

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
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
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
                ContextCompat.getColorStateList(getActivity(), R.color.checkbox_color)
        ));
    }

    public void setOnClickRadio (RadioButton radioButton) {
        radioButton.setOnCheckedChangeListener((compoundButton, isChecked) -> CompoundButtonCompat.setButtonTintList(
                radioButton,
                ContextCompat.getColorStateList(getActivity(), R.color.checkbox_color)
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
//        DetailPageFragment detailPageFragment = new DetailPageFragment();
//        Bundle bundle = new Bundle();

        Date date = new Date();
        date.setTime(models.get(position).EventStartTime.getTime());

        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("title", models.get(position).EventName);
        intent.putExtra("desc", models.get(position).EventDescription);
        intent.putExtra("imgId", modelDocuments.get(position).Path);
        intent.putExtra("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
        intent.putExtra("time", new SimpleDateFormat("HH:mm").format(date));
        intent.putExtra("guest", "");
        startActivity(intent);

//        bundle.putString("title", models.get(position).EventName);
//        bundle.putString("desc", models.get(position).EventDescription);
////        bundle.putString("imgId", Integer.toString(evt_detail_images_ids.getResourceId(position, 0)));
//        bundle.putString("imgUrl", modelDocuments.get(position).Path);
//        bundle.putString("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
//        bundle.putString("time", new SimpleDateFormat("HH:mm").format(date));
//        bundle.putString("guest", "");
//        detailPageFragment.setArguments(bundle);
//
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.frame_layout, detailPageFragment, null);
//        if(fragmentManager.getBackStackEntryCount() != 1){
//            fragmentTransaction.addToBackStack(null);
//        }
//        fragmentTransaction.commit();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
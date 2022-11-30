package com.example.atamerica.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.controllers.HomeController;
import com.example.atamerica.databinding.FragmentHomePageBinding;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment implements AdapterRecyclerHomeLike.OnEventHomeClickListener, AdapterRecyclerHomeTop.OnEventTopClickListener {

    private FragmentHomePageBinding     binding;

    private ConstraintLayout            contentContainer;
    private RecyclerView                recyclerViewHomeLike;
    private RecyclerView                recyclerViewHomeTop;
    private AdapterRecyclerHomeLike     adapterLike;
    private AdapterRecyclerHomeTop      adapterTop;
    private AdapterSlider               adapterSlider;
    private BottomNavigationView        bottomNavigationView;

    private SliderView                  sliderView;

    private List<VwHomeBannerModel>     eventsBanner = new ArrayList<>();
    private List<VwEventThumbnailModel> eventsLike = new ArrayList<>();
    private List<VwEventThumbnailModel> eventsTop = new ArrayList<>();

    private CircularProgressIndicator   progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Define recycle views in the activity (EYML and Top Events)
        contentContainer = rootView.findViewById(R.id.contentContainer);
        recyclerViewHomeLike = rootView.findViewById(R.id.recyclerViewHomeLike);
        recyclerViewHomeTop = rootView.findViewById(R.id.recyclerViewHomeTop);
        sliderView = rootView.findViewById(R.id.slider_view);
        progressIndicator = rootView.findViewById(R.id.progressIndicator);
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view);
        ImageView profileButton = rootView.findViewById(R.id.profileButton);

        // Query data into fragment all event model list
        new TaskRunner().executeAsyncPool(new HomeController.GetEvents(), (data) -> {
            if (!HelperClass.isEmpty(data)) {
                // Filter home banner event
                new TaskRunner().executeAsyncPool(new HomeController.FilterHomeBannerEvent(data), (eventFilter) -> {
                    new TaskRunner().executeAsyncPool(new HomeController.FilterHomeLikeEvent(data), (eventFilter2) -> {
                        new TaskRunner().executeAsyncPool(new HomeController.FilterHomeTopEvent(data), (eventFilter3) -> {
                            if (!HelperClass.isEmpty(eventFilter3)) {
                                this.eventsTop = new ArrayList<>(eventFilter3);
                                adapterTop = new AdapterRecyclerHomeTop(getActivity(), eventsTop, HomePageFragment.this);

                                // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
                                LinearLayoutManager linearLayoutManagerTop = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewHomeTop.setLayoutManager(linearLayoutManagerTop);
                                recyclerViewHomeTop.setAdapter(adapterTop);
                            }
                            else {
                                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
                            }
                        });

                        if (!HelperClass.isEmpty(eventFilter2)) {
                            this.eventsLike = new ArrayList<>(eventFilter2);
                            adapterLike = new AdapterRecyclerHomeLike(getActivity(), eventsLike, HomePageFragment.this);

                            // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
                            LinearLayoutManager linearLayoutManagerLike = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerViewHomeLike.setLayoutManager(linearLayoutManagerLike);
                            recyclerViewHomeLike.setAdapter(adapterLike);
                        }
                        else {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
                        }

                        progressIndicator.setVisibility(View.GONE);
                        contentContainer.setVisibility(View.VISIBLE);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    });

                    if (!HelperClass.isEmpty(eventFilter)) {
                        this.eventsBanner = new ArrayList<>(eventFilter); // Copy local to fragment

                        // SliderView for home banner
                        adapterSlider = new AdapterSlider(getActivity(), eventsBanner);
                        sliderView.setSliderAdapter(adapterSlider);
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        sliderView.startAutoCycle();
                    }
                    else {
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
        });

        // Profile button
        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChildActivity.class);
            intent.putExtra("destination", "profileFragment");
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onEventLikeClick(int position) {
        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("event_id", eventsLike.get(position).EventId);
        startActivity(intent);
    }

    @Override
    public void onEventTopClick(int position) {
        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("event_id", eventsTop.get(position).EventId);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
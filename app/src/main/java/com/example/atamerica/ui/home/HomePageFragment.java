package com.example.atamerica.ui.home;

import android.content.Intent;
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
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class HomePageFragment extends Fragment implements AdapterRecyclerHomeLike.OnEventHomeClickListener, AdapterRecyclerHomeTop.OnEventTopClickListener {

    private FragmentHomePageBinding     binding;

    private ConstraintLayout            contentContainer;
    private RecyclerView                recyclerViewHomeLike;
    private RecyclerView                recyclerViewHomeTop;
    private AdapterRecyclerHomeLike     adapterLike;
    private AdapterRecyclerHomeTop      adapterTop;
    private AdapterSlider               adapterSlider;

    private SliderView                  sliderView;

    private List<VwHomeBannerModel>     eventsBanner;
    private List<VwEventThumbnailModel> eventsLike;
    private List<VwEventThumbnailModel> eventsTop;

    private CircularProgressIndicator   progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Define recycle views in the activity (EYML and Top Events)
        contentContainer = rootView.findViewById(R.id.contentContainer);
        recyclerViewHomeLike = rootView.findViewById(R.id.recyclerViewHomeLike);
        recyclerViewHomeTop = rootView.findViewById(R.id.recyclerViewHomeTop);
        sliderView = rootView.findViewById(R.id.slider_view);
        progressIndicator = rootView.findViewById(R.id.progressIndicator);
        ImageView profileButton = rootView.findViewById(R.id.profileButton);

        new TaskRunner().executeAsyncPool(new HomeController.GetEvents(), (data) -> {
            if (!HelperClass.isEmpty(data)) {
                new TaskRunner().executeAsyncPool(new HomeController.FilterHomeBannerEvent(data), (eventFilter) -> {
                    if (!HelperClass.isEmpty(eventFilter)) {
                        this.eventsBanner = eventFilter;
                        adapterSlider = new AdapterSlider(getActivity(), eventsBanner);
                        // SliderView for home banner
                        sliderView.setSliderAdapter(adapterSlider);
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        sliderView.startAutoCycle();
                    }
                    else {
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

                new TaskRunner().executeAsyncPool(new HomeController.FilterHomeLikeEvent(data), (eventFilter) -> {
                    if (!HelperClass.isEmpty(eventFilter)) {
                        this.eventsLike = eventFilter;
                        adapterLike = new AdapterRecyclerHomeLike(getActivity(), eventsLike, HomePageFragment.this);
                        // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
                        LinearLayoutManager linearLayoutManagerLike = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewHomeLike.setLayoutManager(linearLayoutManagerLike);
                        recyclerViewHomeLike.setAdapter(adapterLike);
                    }
                    else {
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

                new TaskRunner().executeAsyncPool(new HomeController.FilterHomeTopEvent(data), (eventFilter) -> {
                    if (!HelperClass.isEmpty(eventFilter)) {
                        this.eventsTop = eventFilter;
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

                progressIndicator.setVisibility(View.GONE);
                contentContainer.setVisibility(View.VISIBLE);
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
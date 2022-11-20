package com.example.atamerica.ui.home;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.databinding.FragmentHomePageBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;
import java.util.concurrent.Callable;

public class HomePageFragment extends Fragment implements AdapterRecyclerHomeLike.OnEventHomeClickListener, AdapterRecyclerHomeTop.OnEventTopClickListener {

    public class QueryHomeItemTask implements Callable<Boolean> {

        @Override
        public Boolean call() {
            try {
                // Check if cache exists for Home Banners
                if (!HelperClass.isEmpty(EventItemCache.HomeBannerList)) {
                    eventsBanner = EventItemCache.HomeBannerList;
                }
                else {
                    eventsBanner = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwHomeBanner; ", VwHomeBannerModel.class, null);
                    EventItemCache.HomeBannerList = eventsBanner;
                }

                // Check if cache exists for Liked Events
                if (!HelperClass.isEmpty(EventItemCache.HomeEventLikeList)) {
                    eventsLike = EventItemCache.HomeEventLikeList;
                }
                else {
                    eventsLike = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwEventThumbnail LIMIT 10; ", VwEventThumbnailModel.class, null);
                    EventItemCache.HomeEventLikeList = eventsLike;
                }

                // Check if cache exists for Top Events
                if (!HelperClass.isEmpty(EventItemCache.HomeEventTopList)) {
                    eventsTop = EventItemCache.HomeEventTopList;
                }
                else {
                    eventsTop = DataHelper.Query.ReturnAsObjectList("SELECT ET.EventId, ET.EventName, ET.Path FROM VwEventThumbnail ET, MemberRegister MR WHERE ET.EventId = MR.EventId GROUP BY ET.EventId, ET.EventName, ET.Path ORDER BY COUNT(MR.EventId) DESC, ET.EventId ASC LIMIT 10; ", VwEventThumbnailModel.class, null);
                    EventItemCache.HomeEventTopList = eventsTop;
                }

                return (!HelperClass.isEmpty(eventsBanner) || !HelperClass.isEmpty(eventsLike) || !HelperClass.isEmpty(eventsTop));
            }
            catch (Exception e) {
                Log.e("ERROR", "Error query-ing home items.");
                e.printStackTrace();
            }

            return false;
        }
    }

    private FragmentHomePageBinding     binding;

    private ConstraintLayout            contentContainer;
    private RecyclerView                recyclerViewHomeLike;
    private RecyclerView                recyclerViewHomeTop;
    private AdapterRecyclerHomeLike     adapterLike;
    private AdapterRecyclerHomeTop      adapterTop;
    private AdapterSlider               adapterSlider;

    private SliderView                  sliderView;
    private ImageView                   buttonProfile;

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
         requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // request for current activity to allow internet connection
        // this must be in every activity/fragments
        ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Define recycle views in the activity (EYML and Top Events)
        contentContainer = rootView.findViewById(R.id.contentContainer);
        recyclerViewHomeLike = rootView.findViewById(R.id.recyclerViewHomeLike);
        recyclerViewHomeTop = rootView.findViewById(R.id.recyclerViewHomeTop);
        sliderView = rootView.findViewById(R.id.slider_view);
        progressIndicator = rootView.findViewById(R.id.progressIndicator);

        // Asynchronously bind events you might like adapter
        new TaskRunner().executeAsyncPool(new QueryHomeItemTask(), (data) -> {
            if(data != null && data) {
                contentContainer.setVisibility(View.VISIBLE);

                // Home banner slider
                if (!HelperClass.isEmpty(eventsBanner)) {
                    adapterSlider = new AdapterSlider(getActivity(), eventsBanner);
                    // SliderView for home banner
                    sliderView.setSliderAdapter(adapterSlider);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.startAutoCycle();
                }

                if (!HelperClass.isEmpty(eventsLike)) {
                    adapterLike = new AdapterRecyclerHomeLike(getActivity(), eventsLike, HomePageFragment.this);
                    // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
                    LinearLayoutManager linearLayoutManagerLike = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewHomeLike.setLayoutManager(linearLayoutManagerLike);
                    recyclerViewHomeLike.setAdapter(adapterLike);
                }

                if (!HelperClass.isEmpty(eventsTop)) {
                    adapterTop = new AdapterRecyclerHomeTop(getActivity(), eventsTop, HomePageFragment.this);
                    // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
                    LinearLayoutManager linearLayoutManagerTop = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewHomeTop.setLayoutManager(linearLayoutManagerTop);
                    recyclerViewHomeTop.setAdapter(adapterTop);
                }

                progressIndicator.setVisibility(View.GONE);
            }
            else {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
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
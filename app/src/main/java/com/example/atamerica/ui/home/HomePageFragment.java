package com.example.atamerica.ui.home;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentHomePageBinding;
import com.example.atamerica.models.AppEventModel;
import com.example.atamerica.models.EventAttributeModel;
import com.example.atamerica.models.EventDocumentModel;
import com.example.atamerica.ui.home.AdapterRecyclerHomeLike;
import com.example.atamerica.ui.home.AdapterRecyclerHomeTop;
import com.example.atamerica.ui.home.AdapterSlider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.util.Arrays;
import java.util.List;

public class HomePageFragment extends Fragment implements AdapterRecyclerHomeLike.OnEventHomeClickListener, AdapterRecyclerHomeTop.OnEventTopClickListener {
    
    private FragmentHomePageBinding binding;

    private RecyclerView                recyclerViewHomeLike;
    private RecyclerView                recyclerViewHomeTop;
    private AdapterRecyclerHomeLike     adapterLike;
    private AdapterRecyclerHomeTop      adapterTop;

    private ImageView                   buttonProfile;

    private List<AppEventModel>         eventModels;
    private List<EventDocumentModel>    eventDocumentModels;
    private List<EventAttributeModel>   eventAttributeModels;

    List<String> evt_titles_like, evt_dates_like, evt_times_like, evt_guests_like, evt_descs_like;
    TypedArray evt_front_images_like_ids, evt_detail_images_like_ids;

    List<String> evt_titles_top, evt_dates_top, evt_times_top, evt_guests_top, evt_descs_top;
    TypedArray evt_front_images_top_ids, evt_detail_images_top_ids;

    List<String> home_titles, home_dates, home_times, home_descs, home_guests;
    TypedArray home_banners;

    private class BindHomePageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... vOids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Define recycle views in the activity (EYML and Top Events)
        recyclerViewHomeLike = rootView.findViewById(R.id.recyclerViewHomeLike);
        recyclerViewHomeTop = rootView.findViewById(R.id.recyclerViewHomeTop);

        // Retrieve eyml event info from string arrays in strings xml (event image ids from integer arrays)
        evt_titles_like = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates_like = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times_like = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests_like = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs_like = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_like_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_like_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Retrieve top event info from string arrays in strings xml (event image ids from integer arrays)
        evt_titles_top = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
        evt_dates_top = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
        evt_times_top = Arrays.asList(getResources().getStringArray(R.array.evt_times));
        evt_guests_top = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
        evt_descs_top = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
        evt_front_images_top_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
        evt_detail_images_top_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Retrieve banner info for image slider
        home_titles = Arrays.asList(getResources().getStringArray(R.array.home_titles));
        home_dates = Arrays.asList(getResources().getStringArray(R.array.home_dates));
        home_times = Arrays.asList(getResources().getStringArray(R.array.home_times));
        home_guests = Arrays.asList(getResources().getStringArray(R.array.home_guests));
        home_descs = Arrays.asList(getResources().getStringArray(R.array.home_descs));
        home_banners = getResources().obtainTypedArray(R.array.home_banners);

        // Define recycler adapter for each EYML and Top Events recycle views
        adapterLike = new AdapterRecyclerHomeLike(getActivity(), evt_titles_like, evt_front_images_like_ids, this);
        adapterTop = new AdapterRecyclerHomeTop(getActivity(), evt_titles_top, evt_front_images_top_ids, this);

        // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
        LinearLayoutManager linearLayoutManagerLike = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHomeLike.setLayoutManager(linearLayoutManagerLike);
        recyclerViewHomeLike.setAdapter(adapterLike);

        // LinearLayoutManager for horizontal scrolling layout for EYML recycler view
        LinearLayoutManager linearLayoutManagerTop = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHomeTop.setLayoutManager(linearLayoutManagerTop);
        recyclerViewHomeTop.setAdapter(adapterTop);

        SliderView sliderView = rootView.findViewById(R.id.slider_view);
        AdapterSlider adapterSlider = new AdapterSlider(getActivity(), home_titles, home_dates, home_times,
                                                        home_descs, home_guests, home_banners);
        sliderView.setSliderAdapter(adapterSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.startAutoCycle();

        return rootView;
    }

    @Override
    public void onEventLikeClick(int position) {
        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("title", evt_titles_like.get(position));
        intent.putExtra("desc", evt_descs_like.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_like_ids.getResourceId(position, 0)));
        intent.putExtra("date", evt_dates_like.get(position));
        intent.putExtra("time", evt_times_like.get(position));
        intent.putExtra("guest", evt_guests_like.get(position));
        startActivity(intent);
    }

    @Override
    public void onEventTopClick(int position) {
        Intent intent = new Intent(getActivity(), ChildActivity.class);
        intent.putExtra("destination", "detailPageFragment");
        intent.putExtra("title", evt_titles_top.get(position));
        intent.putExtra("desc", evt_descs_top.get(position));
        intent.putExtra("imgId", Integer.toString(evt_detail_images_top_ids.getResourceId(position, 0)));
        intent.putExtra("date", evt_dates_top.get(position));
        intent.putExtra("time", evt_times_top.get(position));
        intent.putExtra("guest", evt_guests_top.get(position));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
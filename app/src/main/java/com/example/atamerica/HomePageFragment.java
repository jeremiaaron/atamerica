package com.example.atamerica;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.atamerica.databinding.FragmentHomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomePageFragment extends Fragment implements AdapterRecyclerHomeLike.OnEventHomeClickListener, AdapterRecyclerHomeTop.OnEventTopClickListener {

    private FragmentHomePageBinding binding;

    RecyclerView recyclerViewHomeLike;
    RecyclerView recyclerViewHomeTop;
    AdapterRecyclerHomeLike adapterLike;
    AdapterRecyclerHomeTop adapterTop;

    List<String> evt_titles_like, evt_dates_like, evt_times_like, evt_guests_like, evt_descs_like;
    TypedArray evt_front_images_like_ids, evt_detail_images_like_ids;

    List<String> evt_titles_top, evt_dates_top, evt_times_top, evt_guests_top, evt_descs_top;
    TypedArray evt_front_images_top_ids, evt_detail_images_top_ids;

    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Define recycle views in the activity (EYML and Top Events)
        recyclerViewHomeLike = mView.findViewById(R.id.recyclerViewHomeLike);
        recyclerViewHomeTop = mView.findViewById(R.id.recyclerViewHomeTop);

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

        ViewPager viewPager = mView.findViewById(R.id.viewPager);
        Adapter adapter = new Adapter(getActivity());
        viewPager.setAdapter(adapter);

        return mView;
    }

    @Override
    public void onEventLikeClick(int position) {
        DetailPageFragment detailPageFragment = new DetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", evt_titles_like.get(position));
        bundle.putString("desc", evt_descs_like.get(position));
        bundle.putString("imgId", Integer.toString(evt_detail_images_like_ids.getResourceId(position, 0)));
        bundle.putString("date", evt_dates_like.get(position));
        bundle.putString("time", evt_times_like.get(position));
        bundle.putString("guest", evt_guests_like.get(position));
        detailPageFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(
                R.id.frame_layout, detailPageFragment, null);
        if(fragmentManager.getBackStackEntryCount() != 1){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onEventTopClick(int position) {
        DetailPageFragment detailPageFragment = new DetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", evt_titles_top.get(position));
        bundle.putString("desc", evt_descs_top.get(position));
        bundle.putString("imgId", Integer.toString(evt_detail_images_top_ids.getResourceId(position, 0)));
        bundle.putString("date", evt_dates_top.get(position));
        bundle.putString("time", evt_times_top.get(position));
        bundle.putString("guest", evt_guests_top.get(position));
        detailPageFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(
                R.id.frame_layout, detailPageFragment, null);
        if(fragmentManager.getBackStackEntryCount() != 1){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
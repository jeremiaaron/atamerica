package com.example.atamerica.ui.booked;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.atamerica.R;
import com.example.atamerica.controllers.BookedController;
import com.example.atamerica.databinding.FragmentBookedPageBinding;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.example.atamerica.ui.detail.DetailPageFragment;

import java.util.ArrayList;
import java.util.List;

public class BookedPageFragment extends Fragment implements AdapterRecyclerBooked.OnEventBookedClickListener {

    private FragmentBookedPageBinding       binding;
    private AdapterRecyclerBooked           adapter;

    private RecyclerView                    recyclerView;
    private List<VwAllEventModel>           events;

    private SwipeRefreshLayout              swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"SourceLockedOrientationActivity", "NotifyDataSetChanged"})
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentBookedPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Define recycle view in the activity
        recyclerView        = mView.findViewById(R.id.recyclerViewBooked);
        swipeRefreshLayout  = mView.findViewById(R.id.swipeRefreshLayout);
        events              = new ArrayList<>();

        // Define recycler adapter for the recycler view
        new TaskRunner().executeAsyncPool(new BookedController.GetEvents(false), (data) -> {
            this.events.clear();
            this.events.addAll(data);

            adapter = new AdapterRecyclerBooked(getActivity(), this.events, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        });

        // on below line we are adding refresh listener
        // for our swipe to refresh method.
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // on below line we are setting is refreshing to false.
            swipeRefreshLayout.setRefreshing(false);

            // Asynchronously bind recycler view
            new TaskRunner().executeAsyncPool(new BookedController.GetEvents(true), (data) -> {
                events.clear();
                events.addAll(data);
                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "QUERY", Toast.LENGTH_LONG).show();
            });
        });

        return mView;
    }

    @Override
    public void onEventBookedClick(int position) {
        // intent.putExtra("event_id", models.get(position).EventId);
        DetailPageFragment detailPageFragment = new DetailPageFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("event_id", this.events.get(position).EventId);
        detailPageFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit);
        );
        fragmentTransaction.replace(R.id.event_container, detailPageFragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
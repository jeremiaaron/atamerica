package com.example.atamerica.ui.booked;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentBookedPageBinding;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.AppEventModel;
import com.example.atamerica.models.EventDocumentModel;
import com.example.atamerica.taskhandler.QueryVwEventThumbnailTask;
import com.example.atamerica.taskhandler.TaskRunner;
import com.example.atamerica.ui.detail.DetailPageFragment;
import com.example.atamerica.ui.upcoming.AdapterRecyclerUpcoming;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class BookedPageFragment extends Fragment implements AdapterRecyclerBooked.OnEventBookedClickListener {

    RecyclerView recyclerView;
    AdapterRecyclerBooked adapter;
    ImageView profileButton;

    List<String> evt_titles, evt_dates, evt_times, evt_guests, evt_descs;
    TypedArray evt_front_images_ids, evt_detail_images_ids;

    FragmentBookedPageBinding binding;

    private List<AppEventModel>             models;
    private List<EventDocumentModel>        modelDocuments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentBookedPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        models = DataHelper.Query.ReturnAsObjectList("SELECT * FROM AppEvent ORDER BY EventId ASC; ", AppEventModel.class, null);
        modelDocuments = DataHelper.Query.ReturnAsObjectList("SELECT * FROM EventDocument WHERE Title = 'thumbnail' ORDER BY EventId; ", EventDocumentModel.class, null);

        // Define recycle view in the activity
        recyclerView = mView.findViewById(R.id.recyclerViewBooked);

        // Retrieve event info from string arrays in strings xml and event images from drawables
//        evt_titles = Arrays.asList(getResources().getStringArray(R.array.evt_titles));
//        evt_dates = Arrays.asList(getResources().getStringArray(R.array.evt_dates));
//        evt_times = Arrays.asList(getResources().getStringArray(R.array.evt_times));
//        evt_guests = Arrays.asList(getResources().getStringArray(R.array.evt_guests));
//        evt_descs = Arrays.asList(getResources().getStringArray(R.array.evt_descs));
//        evt_front_images_ids = getResources().obtainTypedArray(R.array.evt_front_images_ids);
//        evt_detail_images_ids = getResources().obtainTypedArray(R.array.evt_detail_images_ids);

        // Define recycler adapter for the recycler view
        new TaskRunner().executeAsyncPool(new QueryVwEventThumbnailTask("SELECT * FROM VwEventThumbnail ORDER BY EventId ASC; ", null), (data) -> {
            adapter = new AdapterRecyclerBooked(getActivity(), data, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        });

        return mView;
    }

    @Override
    public void onEventBookedClick(int position) {
        // intent.putExtra("event_id", models.get(position).EventId);
        DetailPageFragment detailPageFragment = new DetailPageFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("event_id", models.get(position).EventId);
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

/*    @Override
    public void onEventUpcomingClick(int position) {
        DetailPageFragment detailPageFragment = new DetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", evt_titles.get(position));
        bundle.putString("desc", evt_descs.get(position));
        bundle.putString("imgId", Integer.toString(evt_detail_images_ids.getResourceId(position, 0)));
        bundle.putString("date", evt_dates.get(position));
        bundle.putString("time", evt_times.get(position));
        bundle.putString("guest", evt_guests.get(position));
        detailPageFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_up,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit);
        );
        fragmentTransaction.replace(R.id.frame_layout, detailPageFragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
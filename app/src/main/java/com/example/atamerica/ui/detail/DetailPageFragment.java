package com.example.atamerica.ui.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.atamerica.R;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.taskhandler.DownloadBitmapTask;
import com.example.atamerica.taskhandler.QueryVwAllEventTask;
import com.example.atamerica.taskhandler.TaskRunner;
import com.example.atamerica.ui.register.RegisterPageFragment;
import com.example.atamerica.databinding.FragmentDetailPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class DetailPageFragment extends Fragment {

    private FrameLayout                 frameLayout;
    private ScrollView                  scrollView;

    private MaterialButton              registerBtn;
    private BottomNavigationView        navView;
    private FragmentDetailPageBinding   binding;

    private TabLayout                   tabLayout;
    private ViewPager2                  viewPager2;
    private AdapterTabViewPager         viewPagerAdapter;

    private String                      eventId;
    private VwAllEventModel             model;

    private CircularProgressIndicator   progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getArguments() != null ? getArguments().getString("event_id") : "";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentDetailPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Get views
        tabLayout           = binding.tabLayout;
        viewPager2          = binding.viewPager;
        frameLayout         = mView.findViewById(R.id.frameLayout);
        scrollView          = mView.findViewById(R.id.scroll_view);
        registerBtn         = mView.findViewById(R.id.register_button);
        TextView evtTitle   = mView.findViewById(R.id.evtTitle);
        ImageView evtImg    = mView.findViewById(R.id.evtImg);
        TextView evtDate    = mView.findViewById(R.id.dateContent);
        TextView evtTime    = mView.findViewById(R.id.timeContent);
        progressIndicator   = mView.findViewById(R.id.progressIndicator);

        // Get model detail asynchronously
        new TaskRunner().executeAsyncPool(new QueryVwAllEventTask(this.eventId), (data) -> {
            if (data != null) {
                this.model = data;

                // Bind tab layout detail
                viewPagerAdapter = new AdapterTabViewPager(requireActivity(), model.EventId, model.EventDescription);
                viewPager2.setAdapter(viewPagerAdapter);
                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, (tab, position) -> { });
                tabLayoutMediator.attach();

                Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Description");
                Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Guest");
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager2.setCurrentItem(tab.getPosition());
                        ViewGroup.LayoutParams params = viewPager2.getLayoutParams();
                        params.height = viewPager2.getMeasuredHeight();
                        viewPager2.requestLayout();

                        Log.i("INFO", String.valueOf(tab.getPosition()));
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) { }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                });

                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
                    }
                });

                Date startDate = new Date(model.EventStartTime.getTime());
                Date endDate = new Date(model.EventEndTime.getTime());
                String eventDuration = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(startDate) + " - " + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(endDate);

                evtTitle.setText(model.EventName);

                new TaskRunner().executeAsyncPool(new DownloadBitmapTask(model.DocumentList
                        .stream()
                        .filter(doc -> Objects.equals(doc.Title, "detail_header"))
                        .collect(Collectors.toList()).get(0).Path), evtImg::setImageBitmap);

                evtDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(startDate));
                evtTime.setText(eventDuration);

                progressIndicator.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        // Register button
        registerBtn.setOnClickListener(view -> {
            RegisterPageFragment registerPageFragment = new RegisterPageFragment();

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit);
            );

            fragmentTransaction.replace(R.id.event_container, registerPageFragment, null);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return mView;
    }
}
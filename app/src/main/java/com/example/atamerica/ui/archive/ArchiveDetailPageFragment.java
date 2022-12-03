package com.example.atamerica.ui.archive;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentArchiveDetailPageBinding;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.taskhandler.DownloadBitmapTask;
import com.example.atamerica.taskhandler.QueryVwAllEventTask;
import com.example.atamerica.taskhandler.TaskRunner;
import com.example.atamerica.ui.detail.AdapterTabViewPager;
import com.example.atamerica.ui.video.VideoFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArchiveDetailPageFragment extends Fragment {

    private FrameLayout                         frameLayout;
    private ScrollView                          scrollView;

    private String                              eventId;
    private MaterialButton                      watchBtn, evtCategory;
    private FragmentArchiveDetailPageBinding    binding;

    private TabLayout                           tabLayout;
    private ViewPager2                          viewPager2;
    private AdapterTabViewPager                 viewPagerAdapter;

    private VwAllEventModel                     model;

    private CircularProgressIndicator           progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eventId = getArguments() != null ? getArguments().getString("event_id") : "";
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentArchiveDetailPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Get views
        frameLayout         = mView.findViewById(R.id.frameLayout);
        scrollView          = mView.findViewById(R.id.scroll_view);
        watchBtn            = mView.findViewById(R.id.watch_button);
        progressIndicator   = mView.findViewById(R.id.progressIndicator);
        evtCategory         = mView.findViewById(R.id.evtCategory);

        // Get model detail asynchronously
        new TaskRunner().executeAsyncPool(new QueryVwAllEventTask(this.eventId), (data) -> {
            if (data != null) {
                this.model = data;

                // Bind tab layout detail

                tabLayout = binding.tabLayout;
                viewPager2 = binding.viewPager;
                viewPagerAdapter = new AdapterTabViewPager(requireActivity(), model.EventId, model.EventDescription);
                viewPager2.setAdapter(viewPagerAdapter);

                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, (tab, position) -> {
                    // position of the current tab and that tab
                });
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
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
                    }
                });

                Date startDate = new Date(model.EventStartTime.getTime());

                TextView evtTitle   = mView.findViewById(R.id.evtTitle);
                ImageView evtImg    = mView.findViewById(R.id.evtImg);
                TextView evtYear    = mView.findViewById(R.id.evtYear);

                evtTitle.setText(model.EventName);
                evtCategory.setText(model.CategoryName);
                new TaskRunner().executeAsyncPool(new DownloadBitmapTask(model.DocumentList
                        .stream()
                        .filter(doc -> Objects.equals(doc.Title, "detail_header"))
                        .collect(Collectors.toList()).get(0).Path), evtImg::setImageBitmap);
                evtYear.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(startDate));

                progressIndicator.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });


        // Watch button on click
        watchBtn.setOnClickListener(view -> {
            VideoFragment videoFragment = new VideoFragment();

            Bundle bundle_register = new Bundle();
            bundle_register.putString("event_link", model.EventLink);
            videoFragment.setArguments(bundle_register);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.event_container, videoFragment, null);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return mView;
    }
}
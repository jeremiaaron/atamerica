package com.example.atamerica.ui.detail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.atamerica.R;
import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.controllers.RegisterController;
import com.example.atamerica.databinding.FragmentDetailPageBinding;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.taskhandler.DownloadBitmapTask;
import com.example.atamerica.taskhandler.QueryVwAllEventTask;
import com.example.atamerica.taskhandler.TaskRunner;
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

    private MaterialButton              fullBtn, unregisterBtn, registerBtn;

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

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        com.example.atamerica.databinding.FragmentDetailPageBinding binding = FragmentDetailPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Get views
        tabLayout           = binding.tabLayout;
        viewPager2          = binding.viewPager;
        frameLayout         = mView.findViewById(R.id.frameLayout);
        scrollView          = mView.findViewById(R.id.scroll_view);
        fullBtn             = mView.findViewById(R.id.fully_booked_button);
        unregisterBtn       = mView.findViewById(R.id.unregister_button);
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

                // Button detail
                if (model.Registered) {
                    unregisterBtn.setVisibility(View.VISIBLE);
                }
                else if (model.MaxCapacity == model.RegisteredCount) {
                    fullBtn.setVisibility(View.VISIBLE);
                }
                else {
                    registerBtn.setVisibility(View.VISIBLE);
                }

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
            progressIndicator.setVisibility(View.VISIBLE);

            new TaskRunner().executeAsyncPool(new RegisterController.RegisterUser(AccountManager.User.Email, model.EventId), (data) -> {
                if (Objects.equals(data, "A")) {
                    Dialog successDialog = new Dialog(getContext());
                    successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    successDialog.setContentView(R.layout.fragment_popup_success);

                    successDialog.setOnDismissListener(dialog -> {
                        unregisterBtn.setVisibility(View.VISIBLE);
                        registerBtn.setVisibility(View.GONE);
                    });

                    successDialog.findViewById(R.id.btnDismiss).setOnClickListener(view2 -> successDialog.dismiss());

                    successDialog.show();
                }
                else {
                    Dialog failDialog = new Dialog(getContext());
                    failDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    failDialog.setContentView(R.layout.fragment_popup_fails);

                    failDialog.findViewById(R.id.btnDismiss).setOnClickListener(view2 -> failDialog.dismiss());

                    failDialog.show();
                }

                progressIndicator.setVisibility(View.GONE);
            });
        });

        // Unregister button
        unregisterBtn.setOnClickListener(view -> {
            progressIndicator.setVisibility(View.VISIBLE);

            new TaskRunner().executeAsyncPool(new RegisterController.UnregisterUser(AccountManager.User.Email, model.EventId), (data) -> {
                if (Objects.equals(data, "A")) {
                    Dialog successDialog = new Dialog(getContext());
                    successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    successDialog.setContentView(R.layout.fragment_popup_success);

                    successDialog.setOnDismissListener(dialog -> {
                        unregisterBtn.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.VISIBLE);
                    });

                    successDialog.findViewById(R.id.btnDismiss).setOnClickListener(view2 -> successDialog.dismiss());

                    successDialog.show();
                }
                else {
                    Dialog failDialog = new Dialog(getContext());
                    failDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    failDialog.setContentView(R.layout.fragment_popup_fails);

                    failDialog.findViewById(R.id.btnDismiss).setOnClickListener(view2 -> failDialog.dismiss());

                    failDialog.show();
                }

                progressIndicator.setVisibility(View.GONE);
            });
        });

        // Fully booked button
        fullBtn.setOnClickListener(view -> Toast.makeText(getContext(), "Event is already fully booked.", Toast.LENGTH_LONG).show());

        return mView;
    }
}
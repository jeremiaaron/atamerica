package com.example.atamerica.ui.archive;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atamerica.ui.detail.AdapterTabViewPager;
import com.example.atamerica.R;
import com.example.atamerica.ui.video.VideoFragment;
import com.example.atamerica.databinding.FragmentArchiveDetailPageBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ArchiveDetailPageFragment extends Fragment {

    String title, desc, imgId, date, guest;
    MaterialButton watchBtn;
    FragmentArchiveDetailPageBinding binding;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    AdapterTabViewPager viewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        desc = getArguments().getString("desc");
        imgId = getArguments().getString("imgId");
        date = getArguments().getString("date");
        guest = getArguments().getString("guest");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentArchiveDetailPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

/*        navView = getActivity().findViewById(R.id.bottom_navigation_view);
        navView.setVisibility(View.GONE);*/

        watchBtn = mView.findViewById(R.id.watch_button);
        watchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.event_container, videoFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager;
        viewPagerAdapter = new AdapterTabViewPager(getActivity(), desc, guest);
        viewPager2.setAdapter(viewPagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                // position of the current tab and that tab
            }
        });
        tabLayoutMediator.attach();

        tabLayout.getTabAt(0).setText("Description");
        tabLayout.getTabAt(1).setText("Guest");

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
                tabLayout.getTabAt(position).select();
            }
        });

        TextView evtTitle = mView.findViewById(R.id.evtTitle);
        ImageView evtImg = mView.findViewById(R.id.evtImg);
        TextView evtYear = mView.findViewById(R.id.evtYear);
        evtTitle.setText(title);
        evtImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), Integer.parseInt(imgId), null));
        evtYear.setText(date);

        return mView;
    }
}
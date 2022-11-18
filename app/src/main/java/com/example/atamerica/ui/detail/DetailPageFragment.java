package com.example.atamerica.ui.detail;

import androidx.annotation.Nullable;
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

import com.example.atamerica.R;
import com.example.atamerica.ui.register.RegisterPageFragment;
import com.example.atamerica.databinding.FragmentDetailPageBinding;
import com.example.atamerica.taskhandler.DownloadImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailPageFragment extends Fragment {

    String title, desc, imgId, date, time, guest;
    MaterialButton registerBtn;
    BottomNavigationView navView;
    FragmentDetailPageBinding binding;

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
        time = getArguments().getString("time");
        guest = getArguments().getString("guest");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentDetailPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        registerBtn = mView.findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPageFragment registerPageFragment = new RegisterPageFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
        TextView evtDate = mView.findViewById(R.id.dateContent);
        TextView evtTime = mView.findViewById(R.id.timeContent);
        evtTitle.setText(title);
        // new DownloadImageTask(evtImg).execute(imgId);
        evtDate.setText(date);
        evtTime.setText(time);

        return mView;
    }
}
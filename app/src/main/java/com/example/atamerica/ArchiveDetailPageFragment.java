package com.example.atamerica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atamerica.databinding.FragmentArchiveDetailPageBinding;
import com.example.atamerica.databinding.FragmentDetailPageBinding;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ArchiveDetailPageFragment extends Fragment {

    String title, desc, imgId, date, time, guest;
    MaterialButton watchBtn;
    BottomNavigationView navView;
    FragmentArchiveDetailPageBinding binding;

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

        binding = FragmentArchiveDetailPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        navView = getActivity().findViewById(R.id.bottom_navigation_view);
        navView.setVisibility(View.GONE);

        watchBtn = mView.findViewById(R.id.watch_button);
        watchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(
                        R.id.frame_layout, videoFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        TextView evtTitle = mView.findViewById(R.id.evtTitle);
        TextView evtDesc = mView.findViewById(R.id.evtDesc);
        ImageView evtImg = mView.findViewById(R.id.evtImg);
        TextView evtDate = mView.findViewById(R.id.dateContent);
        TextView evtTime = mView.findViewById(R.id.timeContent);
        TextView evtGuest = mView.findViewById(R.id.guestContent);
        evtTitle.setText(title);
        evtDesc.setText(desc);
        evtImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), Integer.parseInt(imgId), null));
        evtDate.setText(date);
        evtTime.setText(time);
        evtGuest.setText(guest);

        return mView;
    }
}
package com.example.atamerica;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.atamerica.databinding.FragmentProfileBinding;
import com.example.atamerica.databinding.FragmentRegisterPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    BottomNavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        navView = getActivity().findViewById(R.id.bottom_navigation_view);
        navView.setVisibility(View.GONE);

        return mView;
    }
}
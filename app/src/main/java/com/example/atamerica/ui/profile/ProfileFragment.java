package com.example.atamerica.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.atamerica.AuthenticateActivity;
import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private Button buttonLogout;

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

        buttonLogout = (Button) mView.findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(view -> {
            LogOut();
        });

        return mView;
    }

    private void LogOut() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("com.example.atamerica", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        Intent intent = new Intent(getActivity(), AuthenticateActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atamerica.databinding.FragmentRegisterPageBinding;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;

public class RegisterPageFragment extends Fragment {

    private FragmentRegisterPageBinding binding;
    BottomNavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentRegisterPageBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        navView = getActivity().findViewById(R.id.bottom_navigation_view);
        navView.setVisibility(View.GONE);

        return mView;
    }
}
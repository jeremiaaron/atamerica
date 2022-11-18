package com.example.atamerica.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentDescriptionBinding;
import com.example.atamerica.databinding.FragmentGuestBinding;

public class GuestFragment extends Fragment {

    String guest;
    TextView evtGuest;
    FragmentGuestBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guest = getArguments().getString("guest");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuestBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        evtGuest = mView.findViewById(R.id.evt_guest);
        evtGuest.setText(guest);

        return mView;
    }
}
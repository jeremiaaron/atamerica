package com.example.atamerica.ui.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentDescriptionBinding;
import com.example.atamerica.databinding.FragmentUpcomingPageBinding;

public class DescriptionFragment extends Fragment {

    String desc;
    TextView evtDesc;
    FragmentDescriptionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            desc = getArguments().getString("desc");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        evtDesc = mView.findViewById(R.id.evt_desc);
        evtDesc.setText(HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY));

        return mView;
    }
}
package com.example.atamerica.ui.detail;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.atamerica.models.EventAttributeModel;
import com.example.atamerica.models.views.VwEventAttributeJsonModel;

import java.util.List;

public class AdapterTabViewPager extends FragmentStateAdapter {

    private final String eventId;
    private final String description;

    public AdapterTabViewPager(@NonNull FragmentActivity fragmentActivity, String eventId, String desc) {
        super(fragmentActivity);
        this.eventId = eventId;
        this.description = desc;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("event_id", this.eventId);
        bundle.putString("desc", this.description);

        switch (position) {
            case 0:
                DescriptionFragment descriptionFragment = new DescriptionFragment();
                descriptionFragment.setArguments(bundle);
                return descriptionFragment;
            case 1:
                GuestFragment guestFragment = new GuestFragment();
                guestFragment.setArguments(bundle);
                return guestFragment;
            default:
                DescriptionFragment descriptionFragmentDefault = new DescriptionFragment();
                descriptionFragmentDefault.setArguments(bundle);
                return descriptionFragmentDefault;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

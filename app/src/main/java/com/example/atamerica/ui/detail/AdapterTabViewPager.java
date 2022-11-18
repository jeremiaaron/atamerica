package com.example.atamerica.ui.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdapterTabViewPager extends FragmentStateAdapter {

    private String desc, guest;

    public AdapterTabViewPager(@NonNull FragmentActivity fragmentActivity, String desc, String guest) {
        super(fragmentActivity);
        this.desc = desc;
        this.guest = guest;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("desc", this.desc);
        bundle.putString("guest", this.guest);

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

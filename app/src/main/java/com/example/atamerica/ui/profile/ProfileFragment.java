package com.example.atamerica.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.AuthenticateActivity;
import com.example.atamerica.R;
import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.databinding.FragmentProfileBinding;
import com.example.atamerica.ui.booked.BookedPageFragment;
import com.example.atamerica.ui.detail.DetailPageFragment;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private ImageView       ProfilePhoto;
    private TextView        username, email;
    private Button          buttonBooked, buttonLogout;

    private int             SELECT_PICTURE = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        buttonBooked        = mView.findViewById(R.id.booked_button);
        buttonLogout        = mView.findViewById(R.id.buttonLogout);
        username            = mView.findViewById(R.id.profile_name);
        email               = mView.findViewById(R.id.profile_email);

        buttonBooked.setOnClickListener(view -> moveToBooked());
        buttonLogout.setOnClickListener(view -> LogOut());

        ProfilePhoto = mView.findViewById(R.id.profile_picture);
        ProfilePhoto.setOnClickListener(view -> imageChooser());

        username.setText(AccountManager.User.FullName);
        email.setText(AccountManager.User.Email);

        return mView;
    }

    private void moveToBooked() {
        BookedPageFragment bookedPageFragment = new BookedPageFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit);
        );
        fragmentTransaction.replace(R.id.event_container, bookedPageFragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void LogOut() {
        // Clear shared preference
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("com.example.atamerica", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        // Clear caches
        AccountManager.User = null;
        EventItemCache.EventMoreThanNowList.clear();
        EventItemCache.EventLessThanNowList.clear();
        EventItemCache.HomeBannerList.clear();
        EventItemCache.HomeEventLikeList.clear();
        EventItemCache.HomeEventTopList.clear();
        EventItemCache.UpcomingEventList.clear();
        EventItemCache.ArchivedEventList.clear();
        EventItemCache.EventCacheMap.clear();
        EventItemCache.UserRegisteredEventList.clear();

        Intent intent = new Intent(getActivity(), AuthenticateActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    void imageChooser(){
        Intent i = new Intent();

        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    ProfilePhoto.setImageURI(selectedImageUri);
                }
            }

        }
    }

}
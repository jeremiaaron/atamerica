package com.example.atamerica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atamerica.ui.archive.ArchiveDetailPageFragment;
import com.example.atamerica.ui.detail.DetailPageFragment;
import com.example.atamerica.ui.profile.ProfileFragment;
import com.example.atamerica.ui.register.RegisterPageFragment;

public class ChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = getIntent();
        String fragment = intent.getExtras().getString("destination");
        String eventId = intent.getExtras().getString("event_id");

        switch(fragment){
            case "detailPageFragment":
                DetailPageFragment detailPageFragment = new DetailPageFragment();
                Bundle bundle_detail = new Bundle();
                bundle_detail.putString("event_id", eventId);
                detailPageFragment.setArguments(bundle_detail);
                getSupportFragmentManager().beginTransaction().replace(R.id.event_container, detailPageFragment).commit();
                break;

            case "registerPageFragment":
                RegisterPageFragment registerPageFragment = new RegisterPageFragment();
                Bundle bundle_register = new Bundle();
                bundle_register.putString("event_id", eventId);
                registerPageFragment.setArguments(bundle_register);
                getSupportFragmentManager().beginTransaction().replace(R.id.event_container, registerPageFragment).commit();
                break;

            case "archiveDetailPageFragment":
                ArchiveDetailPageFragment archiveDetailPageFragment = new ArchiveDetailPageFragment();
                Bundle bundle_arc_detail = new Bundle();
                bundle_arc_detail.putString("event_id", eventId);
                archiveDetailPageFragment.setArguments(bundle_arc_detail);
                getSupportFragmentManager().beginTransaction().replace(R.id.event_container, archiveDetailPageFragment).commit();
                break;

            case "profileFragment":
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.event_container, profileFragment).commit();
                break;
        }
    }

    public void backActivity(View view) {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
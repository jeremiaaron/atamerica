package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.atamerica.ui.archive.ArchiveDetailPageFragment;
import com.example.atamerica.ui.detail.DetailPageFragment;
import com.example.atamerica.ui.profile.ProfileFragment;
import com.example.atamerica.ui.register.RegisterPageFragment;

public class ChildActivity extends AppCompatActivity {

    String evtTitle, evtDesc, evtDate, evtTime, evtGuest, evtImgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = getIntent();

        String frag = intent.getExtras().getString("destination");
        evtTitle = intent.getExtras().getString("title");
        evtDesc = intent.getExtras().getString("desc");
        evtDate = intent.getExtras().getString("date");
        evtTime = intent.getExtras().getString("time");
        evtGuest = intent.getExtras().getString("guest");
        evtImgId = intent.getExtras().getString("imgId");

        switch(frag){
            case "detailPageFragment":
                DetailPageFragment detailPageFragment = new DetailPageFragment();
                Bundle bundle_detail = new Bundle();
                bundle_detail.putString("title", evtTitle);
                bundle_detail.putString("desc", evtDesc);
                bundle_detail.putString("imgId", evtImgId);
                bundle_detail.putString("date", evtDate);
                bundle_detail.putString("time", evtTime);
                bundle_detail.putString("guest", evtGuest);
                detailPageFragment.setArguments(bundle_detail);
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.event_container, detailPageFragment).commit();
                break;

            case "registerPageFragment":
                RegisterPageFragment registerPageFragment = new RegisterPageFragment();
                Bundle bundle_register = new Bundle();
                bundle_register.putString("title", evtTitle);
                bundle_register.putString("desc", evtDesc);
                bundle_register.putString("imgId", evtImgId);
                bundle_register.putString("date", evtDate);
                bundle_register.putString("time", evtTime);
                bundle_register.putString("guest", evtGuest);
                registerPageFragment.setArguments(bundle_register);
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.event_container, registerPageFragment).commit();
                break;

            case "archiveDetailPageFragment":
                ArchiveDetailPageFragment archiveDetailPageFragment = new ArchiveDetailPageFragment();
                Bundle bundle_arc_detail = new Bundle();
                bundle_arc_detail.putString("title", evtTitle);
                bundle_arc_detail.putString("desc", evtDesc);
                bundle_arc_detail.putString("imgId", evtImgId);
                bundle_arc_detail.putString("date", evtDate);
                bundle_arc_detail.putString("guest", evtGuest);
                archiveDetailPageFragment.setArguments(bundle_arc_detail);
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.event_container, archiveDetailPageFragment).commit();
                break;

            case "profileFragment":
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.event_container, profileFragment).commit();
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
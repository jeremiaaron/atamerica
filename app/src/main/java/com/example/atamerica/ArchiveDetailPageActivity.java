package com.example.atamerica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ArchiveDetailPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_detail_page);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String desc = i.getStringExtra("desc");
        String imgId = i.getStringExtra("imgId");
        String date = i.getStringExtra("date");
        String time = i.getStringExtra("time");
        String guest = i.getStringExtra("guest");
        assert title != null;

        TextView evtTitle = findViewById(R.id.evtTitle);
        TextView evtDesc = findViewById(R.id.evtDesc);
        ImageView evtImg = findViewById(R.id.evtImg);
        TextView evtDate = findViewById(R.id.dateContent);
        TextView evtTime = findViewById(R.id.timeContent);
        TextView evtGuest = findViewById(R.id.guestContent);
        evtTitle.setText(title);
        evtDesc.setText(desc);
        evtImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), Integer.parseInt(imgId), null));
        evtDate.setText(date);
        evtTime.setText(time);
        evtGuest.setText(guest);
    }

    public void backActivity(View view) {
        this.finish();
    }
}
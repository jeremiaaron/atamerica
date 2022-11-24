package com.example.atamerica.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atamerica.ChildActivity;
import com.example.atamerica.R;
import com.example.atamerica.models.views.VwHomeBannerModel;
import com.example.atamerica.taskhandler.DownloadBitmapTask;
import com.example.atamerica.taskhandler.TaskRunner;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterSlider extends SliderViewAdapter<AdapterSlider.Holder> {

    private final Context context;
    private final List<VwHomeBannerModel> models;

    public AdapterSlider(Context ctx, List<VwHomeBannerModel> models) {
        this.context = ctx;
        this.models = models;
    }

    @Override
    public AdapterSlider.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSlider.Holder viewHolder, int position) {
        Date startDate = new Date(models.get(position).EventStartTime.getTime());
        Date endDate = new Date(models.get(position).EventEndTime.getTime());

        String eventDuration = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(startDate) + " - " + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(endDate);

        viewHolder.homeTitle.setText(models.get(position).EventName);
        viewHolder.homeDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(startDate));
        viewHolder.homeTime.setText(eventDuration);

        new TaskRunner().executeAsyncPool(new DownloadBitmapTask(models.get(position).Path), (data) -> viewHolder.homeImage.setImageBitmap(data));

        viewHolder.homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChildActivity.class);
            intent.putExtra("destination", "detailPageFragment");
            intent.putExtra("event_id", models.get(position).EventId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getCount() {
        return models.size();
    }

    public static class Holder extends SliderViewAdapter.ViewHolder {

        TextView homeTitle, homeDate, homeTime;
        ImageView homeImage;
        Button homeButton;

        public Holder(View itemView) {
            super(itemView);
            homeTitle = itemView.findViewById(R.id.evtTitle);
            homeDate = itemView.findViewById(R.id.evtDate);
            homeTime = itemView.findViewById(R.id.evtTime);
            homeImage = itemView.findViewById(R.id.evtImage);
            homeButton = itemView.findViewById(R.id.evtButton);
        }
    }
}

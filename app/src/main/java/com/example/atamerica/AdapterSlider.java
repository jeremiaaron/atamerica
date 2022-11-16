package com.example.atamerica;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AdapterSlider extends SliderViewAdapter<AdapterSlider.Holder> {

    private final Context context;
    List<String> home_titles, home_dates, home_times, home_descs, home_guests;
    TypedArray home_images;

    public AdapterSlider(Context ctx, List<String> titles, List<String> dates, List<String> times,
                         List<String> descs, List<String> guests, TypedArray images) {
        this.context = ctx;
        this.home_titles = titles;
        this.home_dates = dates;
        this.home_times = times;
        this.home_descs = descs;
        this.home_guests = guests;
        this.home_images = images;
    }

    @Override
    public AdapterSlider.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.slider_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSlider.Holder viewHolder, int position) {
        viewHolder.homeTitle.setText(home_titles.get(position));
        viewHolder.homeDate.setText(home_dates.get(position));
        viewHolder.homeTime.setText(home_times.get(position));
        viewHolder.homeImage.setImageResource(home_images.getResourceId(position, 0));
        viewHolder.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChildActivity.class);
                intent.putExtra("destination", "detailPageFragment");
                intent.putExtra("title", home_titles.get(position));
                intent.putExtra("desc", home_descs.get(position));
                intent.putExtra("imgId", Integer.toString(home_images.getResourceId(position, 0)));
                intent.putExtra("date", home_dates.get(position));
                intent.putExtra("time", home_times.get(position));
                intent.putExtra("guest", home_guests.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getCount() {
        return home_images.length();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {

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

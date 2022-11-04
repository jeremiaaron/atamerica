package com.example.atamerica;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecyclerUpcoming extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> evt_titles;
    TypedArray evt_images;
    Context context;
    LayoutInflater inflater;
    private final OnEventUpcomingClickListener onEventClickListener;

    public AdapterRecyclerUpcoming(Context ctx, List<String> evt_titles, TypedArray evt_images, OnEventUpcomingClickListener onEventClickListener) {
        this.evt_titles = evt_titles;
        this.evt_images = evt_images;
        this.inflater = LayoutInflater.from(ctx);
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.upcoming_recycler_layout, parent, false);
        context = parent.getContext();
        return new UpcomingViewHolder(view, onEventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UpcomingViewHolder viewHolder = (UpcomingViewHolder) holder;

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();

        if (position % 2 == 0) {
            layoutParams.setMarginStart(dpToPixel(12));
            layoutParams.setMarginEnd(dpToPixel(0));
        }
        else {
            layoutParams.setMarginStart(dpToPixel(0));
            layoutParams.setMarginEnd(dpToPixel(12));
        }
        viewHolder.cardView.requestLayout();

        viewHolder.evt_title.setText(evt_titles.get(position));
        viewHolder.evt_image.setImageResource(evt_images.getResourceId(position, 0));

        if((position-1) % 4 == 0 || (position-2) % 4 == 0) {
            viewHolder.trans_gradient.setBackground(context.getResources().getDrawable(R.drawable.transparent_gradient_red));
        }
        else viewHolder.trans_gradient.setBackground(context.getResources().getDrawable(R.drawable.transparent_gradient_blue));
    }

    @Override
    public int getItemCount() {
        return evt_titles.size();
    }

    private int dpToPixel (int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    static class UpcomingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView evt_title;
        ImageView evt_image;
        View trans_gradient;
        OnEventUpcomingClickListener onEventClickListener;

        public UpcomingViewHolder(@NonNull View itemView, OnEventUpcomingClickListener onEventClickListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            evt_title = itemView.findViewById((R.id.evt_title));
            evt_image = itemView.findViewById((R.id.evt_image));
            trans_gradient = itemView.findViewById((R.id.trans_gradient));
            this.onEventClickListener = onEventClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventClickListener.onEventUpcomingClick(getAdapterPosition());
        }
    }

    public interface OnEventUpcomingClickListener {
        void onEventUpcomingClick(int position);
    }
}

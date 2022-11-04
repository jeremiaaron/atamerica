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

public class AdapterRecyclerHomeLike extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> evt_titles;
    private final TypedArray evt_images;
    private Context context;
    private final LayoutInflater inflater;
    private final OnEventHomeClickListener onEventClickListener;

    public AdapterRecyclerHomeLike(Context ctx, List<String> evt_titles, TypedArray evt_images, OnEventHomeClickListener onEventClickListener) {
        this.evt_titles = evt_titles;
        this.evt_images = evt_images;
        this.inflater = LayoutInflater.from(ctx);
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_like_recycler_layout, parent, false);
        context = parent.getContext();
        return new HomeLikeViewHolder(view, onEventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeLikeViewHolder viewHolder = (HomeLikeViewHolder) holder;

        if (position == 0) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();
            layoutParams.setMarginStart(dpToPixel(20));
            layoutParams.setMarginEnd(dpToPixel(10));
            viewHolder.cardView.requestLayout();
        }
        else if (position == evt_titles.size()-1) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();
            layoutParams.setMarginStart(dpToPixel(10));
            layoutParams.setMarginEnd(dpToPixel(20));
            viewHolder.cardView.requestLayout();
        }

        viewHolder.evt_title.setText(evt_titles.get(position));
        viewHolder.evt_image.setImageResource(evt_images.getResourceId(position, 0));

        if(position % 2 != 0) {
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

    static class HomeLikeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView evt_title;
        ImageView evt_image;
        View trans_gradient;
        OnEventHomeClickListener onEventClickListener;

        public HomeLikeViewHolder(@NonNull View itemView, OnEventHomeClickListener onEventClickListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            evt_title = itemView.findViewById(R.id.evt_title);
            evt_image = itemView.findViewById(R.id.evt_image);
            trans_gradient = itemView.findViewById(R.id.trans_gradient);
            this.onEventClickListener = onEventClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventClickListener.onEventLikeClick(getAdapterPosition());
        }
    }

    public interface OnEventHomeClickListener {
        void onEventLikeClick(int position);
    }
}

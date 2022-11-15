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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdapterRecyclerArchive extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> evt_titles;
    TypedArray evt_images;
    Context context;
    LayoutInflater inflater;
    private final OnEventArchiveClickListener onEventClickListener;

    public AdapterRecyclerArchive(Context ctx, List<String> evt_titles, TypedArray evt_images, OnEventArchiveClickListener onEventClickListener) {
        this.evt_titles = evt_titles;
        this.evt_images = evt_images;
        this.inflater = LayoutInflater.from(ctx);
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.archive_recycler_layout, parent, false);
        context = parent.getContext();
        return new ArchiveViewHolder(view, onEventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArchiveViewHolder viewHolder = (ArchiveViewHolder) holder;

        ViewGroup.MarginLayoutParams cardLayoutParams = (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();

        if (position % 2 == 0) {
            cardLayoutParams.setMarginStart(dpToPixel(12));
            cardLayoutParams.setMarginEnd(dpToPixel(0));
        }
        else {
            cardLayoutParams.setMarginStart(dpToPixel(0));
            cardLayoutParams.setMarginEnd(dpToPixel(12));
        }
        viewHolder.cardView.requestLayout();

        ViewGroup.MarginLayoutParams btnLayoutParams = (ViewGroup.MarginLayoutParams) viewHolder.evt_button.getLayoutParams();

        if (position == evt_titles.size()-1 || position == evt_titles.size()-2) {
            btnLayoutParams.setMargins(0, dpToPixel(12), 0, dpToPixel(12));
        }
        viewHolder.evt_button.requestLayout();

        viewHolder.evt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();

                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(
                        R.id.frame_layout, videoFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

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

    static class ArchiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        MaterialButton evt_button;
        TextView evt_title;
        ImageView evt_image;
        View trans_gradient;
        OnEventArchiveClickListener onEventClickListener;

        public ArchiveViewHolder(@NonNull View itemView, OnEventArchiveClickListener onEventClickListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            evt_button = itemView.findViewById(R.id.evt_button);
            evt_title = itemView.findViewById((R.id.evt_title));
            evt_image = itemView.findViewById((R.id.evt_image));
            trans_gradient = itemView.findViewById((R.id.trans_gradient));
            this.onEventClickListener = onEventClickListener;

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventClickListener.onEventArchiveClick(getAdapterPosition());
        }
    }

    public interface OnEventArchiveClickListener {
        void onEventArchiveClick(int position);
    }
}

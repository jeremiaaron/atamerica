package com.example.atamerica.ui.home;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atamerica.R;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.DownloadBitmapTask;
import com.example.atamerica.taskhandler.TaskRunner;

import java.util.List;

public class AdapterRecyclerHomeLike extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private final OnEventHomeClickListener onEventClickListener;
    private final List<VwEventThumbnailModel> models;

    public AdapterRecyclerHomeLike(Context ctx, List<VwEventThumbnailModel> models, OnEventHomeClickListener onEventClickListener) {
        this.models = models;
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
        else if (position == models.size() - 1) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();
            layoutParams.setMarginStart(dpToPixel(10));
            layoutParams.setMarginEnd(dpToPixel(20));
            viewHolder.cardView.requestLayout();
        }

        viewHolder.evt_title.setText(models.get(position).EventName);
        new TaskRunner().executeAsyncPool(new DownloadBitmapTask(models.get(position).Path), (data) -> viewHolder.evt_image.setImageBitmap(data));

        if(position % 2 != 0) viewHolder.trans_gradient.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_gradient_red));
        else viewHolder.trans_gradient.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_gradient_blue));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    private int dpToPixel (int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
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
            onEventClickListener.onEventLikeClick(getBindingAdapterPosition());
        }
    }

    public interface OnEventHomeClickListener {
        void onEventLikeClick(int position);
    }
}

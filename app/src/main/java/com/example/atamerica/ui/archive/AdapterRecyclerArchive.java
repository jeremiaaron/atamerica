package com.example.atamerica.ui.archive;

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
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdapterRecyclerArchive extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context                             context;
    private final LayoutInflater                inflater;
    private final List<VwEventThumbnailModel>   events;
    private final OnEventArchiveClickListener   onEventClickListener;


    public AdapterRecyclerArchive(Context ctx, List<VwEventThumbnailModel> models, OnEventArchiveClickListener onEventClickListener) {
        this.events = models;
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

        if (position == events.size() - 1 || position == events.size() - 2) {
            btnLayoutParams.setMargins(0, dpToPixel(12), 0, dpToPixel(12));
        }
        viewHolder.evt_button.requestLayout();

        viewHolder.evt_title.setText(events.get(position).EventName);
        new TaskRunner().executeAsyncPool(new DownloadBitmapTask(events.get(position).Path), (data) -> viewHolder.evt_image.setImageBitmap(data));

        if((position-1) % 4 == 0 || (position-2) % 4 == 0) viewHolder.trans_gradient.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_gradient_red));
        else viewHolder.trans_gradient.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_gradient_blue));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private int dpToPixel (int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
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

            evt_button.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventClickListener.onEventArchiveClick(getBindingAdapterPosition());
        }
    }

    public interface OnEventArchiveClickListener {
        void onEventArchiveClick(int position);
    }
}

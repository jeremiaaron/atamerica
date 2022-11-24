package com.example.atamerica.ui.booked;

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

public class AdapterRecyclerBooked extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context                             context;
    private final LayoutInflater                inflater;
    private final List<VwEventThumbnailModel>   events;
    private final OnEventBookedClickListener   onEventClickListener;


    public AdapterRecyclerBooked(Context ctx, List<VwEventThumbnailModel> models, OnEventBookedClickListener onEventClickListener) {
        this.events = models;
        this.inflater = LayoutInflater.from(ctx);
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.booked_recycler_layout, parent, false);
        context = parent.getContext();
        return new ArchiveViewHolder(view, onEventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArchiveViewHolder viewHolder = (ArchiveViewHolder) holder;

/*        ViewGroup.MarginLayoutParams cardLayoutParams = (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();

        if (position % 2 == 0) {
            cardLayoutParams.setMarginStart(dpToPixel(12));
            cardLayoutParams.setMarginEnd(dpToPixel(0));
        }
        else {
            cardLayoutParams.setMarginStart(dpToPixel(0));
            cardLayoutParams.setMarginEnd(dpToPixel(12));
        }
        viewHolder.cardView.requestLayout();*/

//        if (position == events.size() - 1 || position == events.size() - 2) {
//            btnLayoutParams.setMargins(0, dpToPixel(12), 0, dpToPixel(12));
//        }
//        viewHolder.evt_button.requestLayout();

        viewHolder.evt_title.setText(events.get(position).EventName);
        new TaskRunner().executeAsyncPool(new DownloadBitmapTask(events.get(position).Path), (data) -> viewHolder.evt_image.setImageBitmap(data));
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
        TextView evt_title, evt_date, evt_time;
        ImageView evt_image;
        OnEventBookedClickListener onEventClickListener;

        public ArchiveViewHolder(@NonNull View itemView, OnEventBookedClickListener onEventClickListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            evt_title = itemView.findViewById(R.id.evt_title);
            evt_image = itemView.findViewById(R.id.evt_image);
            evt_date = itemView.findViewById(R.id.evt_date);
            evt_time = itemView.findViewById(R.id.evt_time);
            this.onEventClickListener = onEventClickListener;

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventClickListener.onEventBookedClick(getBindingAdapterPosition());
        }
    }

    public interface OnEventBookedClickListener {
        void onEventBookedClick(int position);
    }
}

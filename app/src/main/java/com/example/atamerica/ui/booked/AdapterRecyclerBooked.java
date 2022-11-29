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
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.DownloadBitmapTask;
import com.example.atamerica.taskhandler.TaskRunner;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdapterRecyclerBooked extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context                             context;
    private final LayoutInflater                inflater;
    private final List<VwAllEventModel>         events;
    private final OnEventBookedClickListener   onEventClickListener;


    public AdapterRecyclerBooked(Context ctx, List<VwAllEventModel> events, OnEventBookedClickListener onEventClickListener) {
        this.events = new ArrayList<>(events);
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
        Date startDate = new Date(events.get(position).EventStartTime.getTime());
        Date endDate = new Date(events.get(position).EventEndTime.getTime());
        String eventDuration = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(startDate) + " - " + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(endDate);

        String path = events.get(position).DocumentList
                .stream()
                .filter(doc -> Objects.equals(doc.Title, "thumbnail"))
                .collect(Collectors.toList()).get(0).Path;

        ArchiveViewHolder viewHolder = (ArchiveViewHolder) holder;
        viewHolder.evt_title.setText(events.get(position).EventName);
        new TaskRunner().executeAsyncPool(new DownloadBitmapTask(path), (data) -> viewHolder.evt_image.setImageBitmap(data));
        viewHolder.evt_date.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(startDate));
        viewHolder.evt_time.setText(eventDuration);
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

package com.example.atamerica.ui.upcoming;

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
import com.example.atamerica.taskhandler.SetTextViewTask;
import com.example.atamerica.taskhandler.DownloadImageTask;
import com.example.atamerica.models.AppEventModel;
import com.example.atamerica.models.EventDocumentModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdapterRecyclerUpcoming extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AppEventModel>                 models;
    private List<EventDocumentModel>            modelDocuments;
    private Context                             context;
    private final LayoutInflater                inflater;
    private final OnEventUpcomingClickListener  onEventClickListener;

    public AdapterRecyclerUpcoming(Context ctx, List<AppEventModel> models, List<EventDocumentModel> modelDocuments, OnEventUpcomingClickListener onEventClickListener) {
        this.models = models;
        this.modelDocuments = modelDocuments;
        this.inflater = LayoutInflater.from(ctx);
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.upcoming_recycler_layout, parent, false);
        this.context = parent.getContext();
        return new UpcomingViewHolder(view, onEventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UpcomingViewHolder viewHolder = (UpcomingViewHolder) holder;

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

        if (position == models.size() - 1 || position == models.size() - 2) {
            btnLayoutParams.setMargins(0, dpToPixel(12), 0, dpToPixel(12));
        }
        viewHolder.evt_button.requestLayout();

/*        viewHolder.evt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPageFragment registerPageFragment = new RegisterPageFragment();

                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(
                        R.id.frame_layout, registerPageFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

        new SetTextViewTask(viewHolder.evt_title).execute(models.get(position).EventName);
        new DownloadImageTask(viewHolder.evt_image).execute(modelDocuments.get(position).Path);

        if((position-1) % 4 == 0 || (position-2) % 4 == 0) viewHolder.trans_gradient.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_gradient_red));
        else viewHolder.trans_gradient.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_gradient_blue));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    private int dpToPixel (int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    static class UpcomingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        MaterialButton evt_button;
        TextView evt_title;
        ImageView evt_image;
        View trans_gradient;
        OnEventUpcomingClickListener onEventClickListener;

        public UpcomingViewHolder(@NonNull View itemView, OnEventUpcomingClickListener onEventClickListener) {
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
            onEventClickListener.onEventUpcomingClick(getAdapterPosition());
        }
    }

    public interface OnEventUpcomingClickListener {
        void onEventUpcomingClick(int position);
    }
}

package com.example.atamerica.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.atamerica.R;
import com.example.atamerica.cache.EventAttributeCache;
import com.example.atamerica.databinding.FragmentGuestBinding;
import com.example.atamerica.models.views.VwEventAttributeJsonModel;
import com.example.atamerica.taskhandler.QueryVwAllEventTask;
import com.example.atamerica.taskhandler.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public class GuestFragment extends Fragment {

    private String                              eventId;
    private List<VwEventAttributeJsonModel>     attributeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        this.eventId = getArguments().getString("event_id");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.example.atamerica.databinding.FragmentGuestBinding binding = FragmentGuestBinding.inflate(inflater, container, false);
        View mView = binding.getRoot();

        // Get views
        TextView textViewAttributeId = mView.findViewById(R.id.textViewAttributeId);
        TextView textViewAttributeValue = mView.findViewById(R.id.textViewAttributeValue);

        // Check for cache
        attributeList = new ArrayList<>();
        if (EventAttributeCache.EventAttributeMap.containsKey(this.eventId)) {
            attributeList = EventAttributeCache.EventAttributeMap.get(this.eventId);
        }
        else {
            new TaskRunner().executeAsyncPool(new QueryVwAllEventTask(this.eventId), (data) -> {
                if (data != null) {
                    data.MapDocument();
                    data.MapAttribute();
                    attributeList = data.AttributeList;
                }
            });
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (VwEventAttributeJsonModel attribute : attributeList) {
            sb.append(attribute.EventAttribute).append("\n");
            sb2.append(attribute.AttributeValue).append("\n");
        }

        textViewAttributeId.setText(sb.toString());
        textViewAttributeValue.setText(sb2.toString());

        return mView;
    }
}
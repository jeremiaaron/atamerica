package com.example.atamerica.taskhandler;

import android.util.Log;

import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class QueryVwAllEventListTask implements Callable<List<VwAllEventModel>> {

    private final String query;
    private final Object[] params;

    public QueryVwAllEventListTask(String query, Object[] params) {
        this.query = query;
        this.params = params;
    }

    @Override
    public List<VwAllEventModel> call() {
        List<VwAllEventModel> events = new ArrayList<>();

        try {
            events = DataHelper.Query.ReturnAsObjectList(query, VwAllEventModel.class, params);

            if (!HelperClass.isEmpty(events)) {
                for (VwAllEventModel event : events) {
                    event.MapAttribute();
                    event.MapDocument();

                    // Store information to cache
                    EventItemCache.EventCacheMap.put(event.EventId, event);
                }
            }
        }
        catch (Exception e) {
            Log.e("ERROR", "Error query VwEventModel; ");
            e.printStackTrace();
        }

        return events;
    }
}

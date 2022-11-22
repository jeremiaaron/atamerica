package com.example.atamerica.taskhandler;

import android.util.Log;

import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class QueryVwAllEventTask implements Callable<VwAllEventModel> {

    private String eventId;

    public QueryVwAllEventTask(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public VwAllEventModel call() {

        // Check cache
        if (EventItemCache.EventCacheMap.containsKey(this.eventId)) {
            return EventItemCache.EventCacheMap.get(this.eventId);
        }
        else {
            // Check for cache
            try {
                VwAllEventModel event = DataHelper.Query.ReturnAsObject("SELECT * FROM VwAllEvent WHERE EventId = ?; ", VwAllEventModel.class, new Object[] { this.eventId });
                event.MapAttribute();
                event.MapDocument();

                // Store information to cache
                EventItemCache.EventCacheMap.put(event.EventId, event);
                return event;
            }
            catch (Exception e) {
                Log.e("ERROR", "Error query VwEventModel; ");
                e.printStackTrace();
            }

            return null;
        }
    }
}

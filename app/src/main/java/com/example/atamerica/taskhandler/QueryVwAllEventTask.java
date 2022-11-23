package com.example.atamerica.taskhandler;

import android.util.Log;

import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.controllers.RegisterController;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.views.VwAllEventModel;

import java.util.concurrent.Callable;

public class QueryVwAllEventTask implements Callable<VwAllEventModel> {

    private final String eventId;

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

                // Check for registration status
                new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> event.Registered = (data2 != null && data2));

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

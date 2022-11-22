package com.example.atamerica.controllers;

import android.util.Log;

import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.TaskRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ArchiveController {

    public static class GetEvent implements Callable<List<VwAllEventModel>> {

        @Override
        public List<VwAllEventModel> call() {
            // Check for cache
            if (!HelperClass.isEmpty(EventItemCache.EventLessThanNowList)) {
                return EventItemCache.EventLessThanNowList;
            }

            List<VwAllEventModel> events = new ArrayList<>();

            try {
                events = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwAllEvent WHERE EventStartTime <= NOW() ORDER BY EventStartTime ASC, EventId ASC; ", VwAllEventModel.class, null);

                if (!HelperClass.isEmpty(events)) {
                    for (VwAllEventModel event : events) {
                        event.MapAttribute();
                        event.MapDocument();

                        // Check for registration status
                        new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> event.Registered = (data2 != null && data2));

                        // Store information to cache
                        EventItemCache.EventLessThanNowList = events;
                        EventItemCache.EventCacheMap.put(event.EventId, event);
                    }
                }
            }
            catch (Exception e) {
                Log.e("ERROR", "Error query archive events; ");
                e.printStackTrace();
            }

            return events;
        }

    }

    public static class ConvertToThumbnailEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public ConvertToThumbnailEvent(final List<VwAllEventModel> events) {
            this.events = new ArrayList<>(events);
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

            // Check for cache
            if (!HelperClass.isEmpty(EventItemCache.ArchivedEventList)) {
                thumbnailEvents = EventItemCache.ArchivedEventList;
            }
            else {
                try {
                    // Select only three banner
                    for (VwAllEventModel event : events) {
                        // Add into list, using parser
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.ArchivedEventList = thumbnailEvents;
                }
                catch (Exception e) {
                    Log.e("ERROR: ", "Error querying events.");
                    e.printStackTrace();
                }
            }

            return thumbnailEvents;
        }
    }

}

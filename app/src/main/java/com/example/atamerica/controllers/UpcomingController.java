package com.example.atamerica.controllers;

import android.util.Log;

import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.cache.ConfigCache;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.taskhandler.TaskRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class UpcomingController {

    public static class GetEvents implements Callable<List<VwAllEventModel>> {
        @Override
        public List<VwAllEventModel> call() {
            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.EventMoreThanNowList)) {
                List<VwAllEventModel> events;

                try {
                    events = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwAllEvent WHERE EventStartTime >= NOW() ORDER BY EventStartTime ASC, EventId ASC; ", VwAllEventModel.class, null);

                    if (!HelperClass.isEmpty(events)) {
                        for (VwAllEventModel event : events) {
                            event.MapAttribute();
                            event.MapDocument();

                            // Check for registration status
                            new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> event.Registered = (data2 != null && data2));

                            // Store information to cache
                            EventItemCache.EventCacheMap.put(event.EventId, event);
                        }

                        EventItemCache.EventMoreThanNowList = new ArrayList<>(events);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.EventMoreThanNowList;
        }
    }

    public static class ConvertToThumbnailEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public ConvertToThumbnailEvent(final List<VwAllEventModel> events) {
            this.events = events;
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.UpcomingEventList)) {
                List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

                try {
                    for (VwAllEventModel event : events) {
                        // Add into list, using parser
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.UpcomingEventList = new ArrayList<>(thumbnailEvents);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.UpcomingEventList;
        }
    }

    public static class FilterEvents implements Callable<List<VwEventThumbnailModel>> {

        private final List<String>          categories;
        private final int                   sortConfig;
        private final List<VwAllEventModel> events;

        public FilterEvents(final List<VwAllEventModel> events, List<String> categories, int sortConfig) {
            this.events = new ArrayList<>(events);
            this.categories = categories;
            this.sortConfig = sortConfig;
        }

        @Override
        public List<VwEventThumbnailModel> call() throws Exception {
            List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

            // Check for cache
            try {
                // Sort events by newest/latest
                if (sortConfig == 1) { // latest
                    events.sort(Comparator.comparing(event -> event.EventStartTime));
                }
                else {
                    events.sort((event1, event2) -> event2.EventId.compareTo(event1.EventId));
                }

                // Filter events
                for (VwAllEventModel event : events) {
                    if (HelperClass.isEmpty(categories) || categories.contains(event.CategoryName)) {
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }
                }

                EventItemCache.UpcomingEventList = new ArrayList<>(thumbnailEvents);

                // Store options
                ConfigCache.Categories = categories;
                ConfigCache.SortConfig = sortConfig;
            }
            catch (Exception e) {
                Log.e("ERROR: ", "Error querying events.");
                e.printStackTrace();
            }

            return EventItemCache.UpcomingEventList;
        }
    }

}

package com.example.atamerica.controllers;

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

public class ArchiveController {

    public static class GetEvent implements Callable<List<VwAllEventModel>> {

        @Override
        public List<VwAllEventModel> call() {
            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.EventLessThanNowList)) {
                List<VwAllEventModel> events;

                try {
                    events = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwAllEvent WHERE EventStartTime < NOW() ORDER BY EventStartTime ASC, EventId ASC; ", VwAllEventModel.class, null);

                    if (!HelperClass.isEmpty(events)) {
                        for (VwAllEventModel event : events) {
                            event.MapAttribute();
                            event.MapDocument();

                            // Check for registration status
                            new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> event.Registered = (data2 != null && data2));

                            // Store information to cache
                            EventItemCache.EventCacheMap.put(event.EventId, event);
                        }

                        EventItemCache.EventLessThanNowList.clear();
                        EventItemCache.EventLessThanNowList.addAll(events);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.EventLessThanNowList;
        }

    }

    public static class ConvertToThumbnailEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public ConvertToThumbnailEvent(final List<VwAllEventModel> events) {
            this.events = events;
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.ArchivedEventList)) {
                try {
                    // Select only three banner
                    for (VwAllEventModel event : events) {
                        // Add into list, using parser
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.ArchivedEventList.clear();
                    EventItemCache.ArchivedEventList.addAll(thumbnailEvents);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.ArchivedEventList;
        }
    }

    public static class FilterEvents implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public FilterEvents(final List<VwAllEventModel> events) {
            this.events = new ArrayList<>(events);
        }

        @Override
        public List<VwEventThumbnailModel> call() throws Exception {
            List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

            try {
                // Sort events by newest/latest
                if (ConfigCache.ArchivedSortConfig == 1) events.sort(Comparator.comparing(event -> event.EventStartTime)); // latest
                else events.sort((event1, event2) -> event2.EventId.compareTo(event1.EventId));

                // Filter events
                for (VwAllEventModel event : events) {
                    if (HelperClass.isEmpty(ConfigCache.ArchivedCategories) || ConfigCache.ArchivedCategories.contains(event.CategoryName)) {
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }
                }

                EventItemCache.ArchivedEventList.clear();
                EventItemCache.ArchivedEventList.addAll(thumbnailEvents);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return EventItemCache.ArchivedEventList;
        }
    }

}

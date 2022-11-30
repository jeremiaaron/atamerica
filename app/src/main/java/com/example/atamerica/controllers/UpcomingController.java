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
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class UpcomingController {

    public static class GetEvents implements Callable<List<VwAllEventModel>> {

        private final boolean refreshQuery;

        public GetEvents(boolean refreshQuery) {
            this.refreshQuery = refreshQuery;
        }

        @Override
        public List<VwAllEventModel> call() {
            // Check for cache
            if (refreshQuery || HelperClass.isEmpty(EventItemCache.EventMoreThanNowList)) {
                try {
                    List<VwAllEventModel> events = DataHelper.Query.ReturnAsObjectList("SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY EventStartTime) AS RowNumber, A.* FROM VwAllEvent A WHERE EventStartTime >= NOW()) B WHERE RowNumber >= ? AND RowNumber <= ? ORDER BY EventStartTime ASC, EventId ASC; ", VwAllEventModel.class,
                            new Object[] { ConfigCache.UpcomingScrollIndex * 8, (ConfigCache.UpcomingScrollIndex + 1) * 8  });

                    if (!HelperClass.isEmpty(events)) {
                        ConfigCache.UpcomingQueryable = !(events.size() < 8);

                        for (VwAllEventModel event : events) {
                            event.MapAttribute();
                            event.MapDocument();

                            // Check for registration status
                            new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> event.Registered = (data2 != null && data2));

                            // Store information to cache
                            EventItemCache.EventCacheMap.put(event.EventId, event);

                            List<VwAllEventModel> eventsRemove = EventItemCache.EventMoreThanNowList
                                    .stream()
                                    .filter(evt -> Objects.equals(evt.EventId, event.EventId))
                                    .collect(Collectors.toList());

                            EventItemCache.EventMoreThanNowList.removeAll(eventsRemove);
                            EventItemCache.EventMoreThanNowList.add(event);
                        }
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

        private final boolean queryNext;
        private final List<VwAllEventModel> events;

        public ConvertToThumbnailEvent(boolean queryNext, final List<VwAllEventModel> events) {
            this.queryNext = queryNext;
            this.events = events;
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            // Check for cache
            if (queryNext || HelperClass.isEmpty(EventItemCache.UpcomingEventList)) {
                List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

                try {
                    for (VwAllEventModel event : events) {
                        // Add into list, using parser
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.UpcomingEventList.clear();
                    EventItemCache.UpcomingEventList.addAll(thumbnailEvents);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.UpcomingEventList;
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
                if (ConfigCache.UpcomingSortConfig == 1) events.sort(Comparator.comparing(event -> event.EventStartTime)); // latest
                else events.sort((event1, event2) -> event2.EventId.compareTo(event1.EventId));

                // Filter events
                for (VwAllEventModel event : events) {
                    if (HelperClass.isEmpty(ConfigCache.UpcomingCategories) || ConfigCache.UpcomingCategories.contains(event.CategoryName)) {
                        thumbnailEvents.add(VwEventThumbnailModel.Parse(event));
                    }
                }

                EventItemCache.UpcomingEventList.clear();
                EventItemCache.UpcomingEventList.addAll(thumbnailEvents);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return EventItemCache.UpcomingEventList;
        }
    }

}

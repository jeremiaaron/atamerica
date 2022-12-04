package com.example.atamerica.controllers;

import android.text.TextUtils;

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
                    List<VwAllEventModel> events = DataHelper.Query.ReturnAsObjectList("SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY EventEndTime) AS RowNumber, A.* FROM VwAllEvent A WHERE EventEndTime >= NOW()) B WHERE RowNumber >= ? AND RowNumber <= ? ORDER BY EventEndTime ASC, EventId ASC; ", VwAllEventModel.class,
                            new Object[] { ConfigCache.UpcomingScrollIndex * 8, (ConfigCache.UpcomingScrollIndex + 1) * 8  });

                    if (!HelperClass.isEmpty(events)) {
                        ConfigCache.UpcomingQueryable = !(events.size() < 8);

                        for (VwAllEventModel event : events) {
                            event.MapAttribute();
                            event.MapDocument();

                            // Check for registration status
                            new TaskRunner().executeAsyncPool(new RegisterController.CheckRegister(AccountManager.User.Email, event.EventId), (data2) -> {
                                if (data2 != null && data2) {
                                    event.Registered = true;
                                    EventItemCache.RegisteredEventList.add(event);
                                }
                                else {
                                    event.Registered = false;
                                    EventItemCache.RegisteredEventList.remove(event);
                                }
                            });

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

    public static class FilterEvents implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;
        private final String searchText;

        public FilterEvents(final List<VwAllEventModel> events, String searchText) {
            this.events = new ArrayList<>(events);
            this.searchText = searchText;
        }

        @Override
        public List<VwEventThumbnailModel> call() throws Exception {
            List<VwEventThumbnailModel> thumbnailEvents = new ArrayList<>();

            try {
                // Sort events by newest/latest
                if (ConfigCache.UpcomingSortConfig == 1) events.sort(Comparator.comparing(event -> event.EventEndTime));
                else events.sort((event1, event2) -> event2.EventId.compareTo(event1.EventId));

                // Filter events
                for (VwAllEventModel event : events) {
                    if (event.EventName.toLowerCase().contains(TextUtils.isEmpty(searchText) ? "" : searchText.toLowerCase())
                            && (HelperClass.isEmpty(ConfigCache.UpcomingCategories) || ConfigCache.UpcomingCategories.contains(event.CategoryName))) {
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

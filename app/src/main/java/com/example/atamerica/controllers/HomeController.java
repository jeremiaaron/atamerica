package com.example.atamerica.controllers;

import android.util.Log;

import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;
import com.example.atamerica.taskhandler.TaskRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class HomeController {

    public static class GetEvents implements Callable<List<VwAllEventModel>> {

        @Override
        public List<VwAllEventModel> call() {
            // Check for cache
            if (!HelperClass.isEmpty(EventItemCache.EventMoreThanNowList)) {
                return EventItemCache.EventMoreThanNowList;
            }

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

                    // Store information to cache
                    EventItemCache.EventMoreThanNowList.clear();
                    EventItemCache.EventMoreThanNowList = new ArrayList<>(events);
                }
            }
            catch (Exception e) {
                Log.e("ERROR", "Error query home events; ");
                e.printStackTrace();
            }

            return EventItemCache.EventMoreThanNowList;
        }
    }

    public static class FilterHomeBannerEvent implements Callable<List<VwHomeBannerModel>> {

        private final List<VwAllEventModel> events;

        public FilterHomeBannerEvent(final List<VwAllEventModel> events) {
            this.events = events;
        }

        @Override
        public List<VwHomeBannerModel> call() {
            List<VwHomeBannerModel> bannerEvents = new ArrayList<>();

            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.HomeEventBannerList)) {
                try {
                    // Select only three banner at most
                    int minimum = Math.min(events.size(), 3);
                    for (int i = 0; i < minimum; i++) {
                        VwAllEventModel event = events.get(i);

                        // Add into list, using parser
                        bannerEvents.add(VwHomeBannerModel.Parse(event));
                    }

                    EventItemCache.HomeEventBannerList = new ArrayList<>(bannerEvents);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.HomeEventBannerList;
        }
    }

    public static class FilterHomeLikeEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public FilterHomeLikeEvent(final List<VwAllEventModel> events) {
            this.events = events;
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.HomeEventLikeList)) {
                List<VwEventThumbnailModel> likeEvents = new ArrayList<>();

                try {
                    // Filter unused events from all events
                    int minimum = Math.min(events.size(), 10);
                    for (int i = 0; i < minimum; i++) {
                        VwAllEventModel event = events.get(i);
                        // Add into list, using parser
                        likeEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.HomeEventLikeList = new ArrayList<>(likeEvents);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.HomeEventLikeList;
        }
    }

    public static class FilterHomeTopEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public FilterHomeTopEvent(final List<VwAllEventModel> events) {
            // Create copy, since this version require modifying the order of the events
            this.events = new ArrayList<>(events);
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            // Check for cache
            if (HelperClass.isEmpty(EventItemCache.HomeEventTopList)) {
                List<VwEventThumbnailModel> topEvents = new ArrayList<>();

                try {
                    // Sort by max capacity first, since default is by event start date
                    // Descending
                    events.sort((Comparator.comparingInt(vwAllEventModel -> vwAllEventModel.MaxCapacity)));

                    // Filter unused events from all events
                    int minimum = Math.min(events.size(), 10);
                    for (int i = 0; i < minimum; i++) {
                        VwAllEventModel event = events.get(i);
                        // Add into list, using parser
                        topEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.HomeEventTopList = new ArrayList<>(topEvents);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return EventItemCache.HomeEventTopList;
        }
    }

}

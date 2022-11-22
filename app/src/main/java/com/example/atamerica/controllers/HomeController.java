package com.example.atamerica.controllers;

import android.util.Log;

import com.example.atamerica.cache.EventItemCache;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.javaclass.HelperClass;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;

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

            List<VwAllEventModel> events = new ArrayList<>();

            try {
                events = DataHelper.Query.ReturnAsObjectList("SELECT * FROM VwAllEvent WHERE EventStartTime >= NOW() ORDER BY EventStartTime ASC, EventId ASC; ", VwAllEventModel.class, null);

                if (!HelperClass.isEmpty(events)) {
                    for (VwAllEventModel event : events) {
                        event.MapAttribute();
                        event.MapDocument();

                        // Store information to cache
                        EventItemCache.EventMoreThanNowList = events;
                        EventItemCache.EventCacheMap.put(event.EventId, event);
                    }
                }
            }
            catch (Exception e) {
                Log.e("ERROR", "Error query home events; ");
                e.printStackTrace();
            }

            return events;
        }
    }

    public static class FilterHomeBannerEvent implements Callable<List<VwHomeBannerModel>> {

        private final List<VwAllEventModel> events;

        public FilterHomeBannerEvent(final List<VwAllEventModel> events) {
            this.events = new ArrayList<>(events);
        }

        @Override
        public List<VwHomeBannerModel> call() {
            List<VwHomeBannerModel> bannerEvents = new ArrayList<>();

            // Check for cache
            if (!HelperClass.isEmpty(EventItemCache.HomeBannerList)) {
                bannerEvents = EventItemCache.HomeBannerList;
            }
            else {
                try {
                    // Select only three banner
                    for (int i = 0; i < 3; i++) {
                        VwAllEventModel event = events.get(i);

                        // Add into list, using parser
                        bannerEvents.add(VwHomeBannerModel.Parse(event));
                    }

                    EventItemCache.HomeBannerList = bannerEvents;
                }
                catch (Exception e) {
                    Log.e("ERROR: ", "Error querying banner events.");
                    e.printStackTrace();
                }
            }

            return bannerEvents;
        }
    }

    public static class FilterHomeLikeEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public FilterHomeLikeEvent(final List<VwAllEventModel> events) {
            this.events = new ArrayList<>(events);
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            List<VwEventThumbnailModel> likeEvents = new ArrayList<>();

            // Check for cache
            if (!HelperClass.isEmpty(EventItemCache.HomeEventLikeList)) {
                likeEvents = EventItemCache.HomeEventLikeList;
            }
            else {
                try {
                    // Filter unused events from all events
                    for (VwAllEventModel event : events) {
                        // Add into list, using parser
                        likeEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.HomeEventLikeList = likeEvents;
                }
                catch (Exception e) {
                    Log.e("ERROR: ", "Error querying like events.");
                    e.printStackTrace();
                }
            }

            return likeEvents;
        }
    }

    public static class FilterHomeTopEvent implements Callable<List<VwEventThumbnailModel>> {

        private final List<VwAllEventModel> events;

        public FilterHomeTopEvent(final List<VwAllEventModel> events) {
            this.events = new ArrayList<>(events);
        }

        @Override
        public List<VwEventThumbnailModel> call() {
            List<VwEventThumbnailModel> topEvents = new ArrayList<>();

            // Check for cache
            if (!HelperClass.isEmpty(EventItemCache.HomeEventTopList)) {
                topEvents = EventItemCache.HomeEventTopList;
            }
            else {
                try {
                    // Sort by max capacity first, since default is by event start date
                    // Descending
                    events.sort((Comparator.comparingInt(vwAllEventModel -> vwAllEventModel.MaxCapacity)));

                    // Filter unused events from all events
                    for (VwAllEventModel event : events) {
                        // Add into list, using parser
                        topEvents.add(VwEventThumbnailModel.Parse(event));
                    }

                    EventItemCache.HomeEventTopList = topEvents;
                }
                catch (Exception e) {
                    Log.e("ERROR: ", "Error querying top events.");
                    e.printStackTrace();
                }
            }

            return topEvents;
        }
    }

}

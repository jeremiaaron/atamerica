package com.example.atamerica.cache;

import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventItemCache {
    // These cache values must be copied into fragments
    // Modifying caches are only done in controllers/fragments

    // Store VwAllEvent based on given event id
    public static HashMap<String, VwAllEventModel> EventCacheMap = new HashMap<>();

    // Event more than and less than current date caches
    public static List<VwAllEventModel> EventMoreThanNowList = new ArrayList<>();
    public static List<VwAllEventModel> EventLessThanNowList = new ArrayList<>();

    // Home event caches
    public static List<VwHomeBannerModel> HomeEventBannerList = new ArrayList<>();
    public static List<VwEventThumbnailModel> HomeEventLikeList = new ArrayList<>();
    public static List<VwEventThumbnailModel> HomeEventTopList = new ArrayList<>();

    // Upcoming event caches
    public static List<VwEventThumbnailModel> UpcomingEventList = new ArrayList<>();

    // Archived event caches
    public static List<VwEventThumbnailModel> ArchivedEventList = new ArrayList<>();

    // Store events that have been booked by user
    public static List<VwAllEventModel> RegisteredEventList = new ArrayList<>();
    public static List<String> UserRegisteredEventList = new ArrayList<>();
}
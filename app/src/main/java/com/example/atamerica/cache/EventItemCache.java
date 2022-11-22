package com.example.atamerica.cache;

import com.example.atamerica.models.DetailItemModel;
import com.example.atamerica.models.views.VwAllEventModel;
import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventItemCache {
    // Event caches
    public static List<VwAllEventModel> EventMoreThanNowList = new ArrayList<>();
    public static List<VwAllEventModel> EventLessThanNowList = new ArrayList<>();

    // Event list caches
    public static List<VwHomeBannerModel> HomeBannerList = new ArrayList<>();
    public static List<VwEventThumbnailModel> HomeEventLikeList = new ArrayList<>();
    public static List<VwEventThumbnailModel> HomeEventTopList = new ArrayList<>();

    public static List<VwEventThumbnailModel> UpcomingEventList = new ArrayList<>();

    public static List<VwEventThumbnailModel> ArchivedEventList = new ArrayList<>();

    // Store VwAllEvent based on given event id
    public static HashMap<String, VwAllEventModel> EventCacheMap = new HashMap<>();

    // Store events that have been booked by user
    public static List<String> UserRegisteredEventList = new ArrayList<>();
}
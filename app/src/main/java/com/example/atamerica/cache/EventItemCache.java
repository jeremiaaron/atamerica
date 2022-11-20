package com.example.atamerica.cache;

import com.example.atamerica.models.views.VwEventThumbnailModel;
import com.example.atamerica.models.views.VwHomeBannerModel;

import java.util.ArrayList;
import java.util.List;

public class EventItemCache {

    public static List<VwHomeBannerModel> HomeBannerList = new ArrayList<>();
    public static List<VwEventThumbnailModel> HomeEventLikeList = new ArrayList<>();
    public static List<VwEventThumbnailModel> HomeEventTopList = new ArrayList<>();

    public static List<VwEventThumbnailModel> UpcomingEventList = new ArrayList<>();

}
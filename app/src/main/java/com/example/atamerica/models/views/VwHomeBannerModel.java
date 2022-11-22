package com.example.atamerica.models.views;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.Collectors;

public class VwHomeBannerModel {

    public String EventId;
    public String EventName;
    public Timestamp EventStartTime;
    public Timestamp EventEndTime;
    public String Path;

    public static VwHomeBannerModel Parse(VwAllEventModel event) {
        VwHomeBannerModel model = new VwHomeBannerModel();

        model.EventId = event.EventId;
        model.EventName = event.EventName;
        model.EventStartTime = event.EventStartTime;
        model.EventEndTime = event.EventEndTime;

        model.Path = event.DocumentList
                .stream()
                .filter(doc -> Objects.equals(doc.Title, "detail_header"))
                .collect(Collectors.toList()).get(0).Path;

        return model;
    }

}
package com.example.atamerica.models.views;

import java.util.Objects;
import java.util.stream.Collectors;

public class VwEventThumbnailModel {

    public String EventId;
    public String EventName;
    public String Path;

    public static VwEventThumbnailModel Parse(VwAllEventModel event) {
        VwEventThumbnailModel model = new VwEventThumbnailModel();

        model.EventId = event.EventId;
        model.EventName = event.EventName;

        model.Path = event.DocumentList
                .stream()
                .filter(doc -> Objects.equals(doc.Title, "thumbnail"))
                .collect(Collectors.toList()).get(0).Path;

        return model;
    }

}

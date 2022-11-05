package com.example.atamerica.models;

import java.time.LocalDateTime;
import java.util.Map;

public class AppEventModel {

    public String EventId;
    public String EventName;
    public LocalDateTime EventStartTime;
    public LocalDateTime EventEndTime;
    public String EventLocation;
    public int MaxCapacity;
    public String EventStatus;
    public String EventDescription;
    public String EventLink;

    public static AppEventModel getAppEvent(String eventId) {
//        AppEventModel returnValue = null;
//
//        try {
//            Map<String, Object> map = DataHelper.Query.ReturnAsHashMap("SELECT * FROM AppEvent WHERE EventId = ?; ", new Object[] { eventId });
//
//            if (map != null) {
//                returnValue = new AppEventModel();
//
//                returnValue.EventId = (String) map.get("EventId");
//                returnValue.EventName = (String) map.get("EventName");
//                returnValue.EventStartTime = (LocalDateTime) map.get("EventStartTime");
//                returnValue.EventEndTime = (LocalDateTime) map.get("EventEndTime");
//                returnValue.EventLocation = (String) map.get("EventLocation");
//                returnValue.MaxCapacity = (int) map.get("MaxCapacity");
//                returnValue.EventStatus = (String) map.get("EventStatus");
//                returnValue.EventDescription = (String) map.get("EventDescription");
//                returnValue.EventLink = (String) map.get("EventLink");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return returnValue;

        return null;
    }
}

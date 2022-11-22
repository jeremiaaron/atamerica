package com.example.atamerica.models.views;

import android.util.Log;

import com.example.atamerica.cache.EventAttributeCache;
import com.example.atamerica.cache.EventDocumentCache;
import com.example.atamerica.javaclass.JsonMapper;

import java.sql.Timestamp;
import java.util.List;

public class VwAllEventModel {

    public String EventId;
    public String EventName;
    public Timestamp EventStartTime;
    public Timestamp EventEndTime;
    public String EventLocation;

    public int RegisteredCount;
    public int MaxCapacity;
    public boolean Registered;

    public String EventDescription;
    public String EventLink;
    public String CategoryName;

    public String AttributeJson;
    public String DocumentJson;

    public List<VwEventAttributeJsonModel> AttributeList;
    public List<VwEventDocumentJsonModel> DocumentList;

    public void MapAttribute() {
        try {
            // Check for cache
            if (EventAttributeCache.EventAttributeMap.containsKey(this.EventId)) {
                this.AttributeList = EventAttributeCache.EventAttributeMap.get(this.EventId);
            }
            else {
                this.AttributeList = JsonMapper.readList(AttributeJson, VwEventAttributeJsonModel.class);
                EventAttributeCache.EventAttributeMap.put(this.EventId, this.AttributeList);
            }
        }
        catch (Exception e) {
            Log.e("ERROR", "Error mapping event attribute.");
            e.printStackTrace();
        }
    }

    public void MapDocument() {
        try {
            // Check for cache
            if (EventDocumentCache.EventDocumentMap.containsKey(this.EventId)) {
                this.DocumentList = EventDocumentCache.EventDocumentMap.get(this.EventId);
            }
            else {
                this.DocumentList = JsonMapper.readList(DocumentJson, VwEventDocumentJsonModel.class);
                EventDocumentCache.EventDocumentMap.put(this.EventId, this.DocumentList);
            }
        }
        catch (Exception e) {
            Log.e("ERROR", "Error mapping event document.");
            e.printStackTrace();
        }
    }

}
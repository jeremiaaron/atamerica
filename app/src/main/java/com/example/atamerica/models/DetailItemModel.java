package com.example.atamerica.models;

import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DetailItemModel {

    public String EventId;
    public String EventName;
    public Timestamp EventStartTime;
    public Timestamp EventEndTime;
    public String EventLocation;
    public int MaxCapacity;
    public String EventDescription;
    public String EventLink;
    public String CategoryName;
    public String Path;

    public List<EventAttributeModel> Attribute = new ArrayList<>();
    public Bitmap BannerBitmap = null;

    public static DetailItemModel CreateEmptyDetail() {
        DetailItemModel returnModel = new DetailItemModel();

        returnModel.EventId = "";
        returnModel.EventName = "";
        returnModel.EventStartTime = new Timestamp(System.currentTimeMillis());
        returnModel.EventEndTime = new Timestamp(System.currentTimeMillis());
        returnModel.EventLocation = "";
        returnModel.MaxCapacity = 0;
        returnModel.EventDescription = "";
        returnModel.EventLink = "";
        returnModel.CategoryName = "";
        returnModel.Path = "";

        return returnModel;
    }
}

package com.example.atamerica.models;

public class EventAttributeModel {

    public String EventId;
    public String AttributeTitle;
    public String AttributeValue;

    public String RowString() {
        return "<tr><td><font color='#D21243'>" + AttributeTitle + "</font><td><td style='text-align:right;'>" + AttributeValue + "</td>";
    }
}

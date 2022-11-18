package com.example.atamerica.taskhandler;

import android.util.Log;

import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.views.VwEventThumbnailModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class QueryVwEventThumbnailTask implements Callable<List<VwEventThumbnailModel>> {

    private final String query;
    private final Object[] params;

    public QueryVwEventThumbnailTask(String query, Object[] params) {
        this.query = query;
        this.params = params;
    }

    @Override
    public List<VwEventThumbnailModel> call() {
        List<VwEventThumbnailModel> models = new ArrayList<>();

        try {
            models = DataHelper.Query.ReturnAsObjectList(query, VwEventThumbnailModel.class, params);
        }
        catch (Exception e) {
            Log.e("ERROR", "Error querying thumbnail model!");
            e.printStackTrace();
        }

        return models;
    }
}
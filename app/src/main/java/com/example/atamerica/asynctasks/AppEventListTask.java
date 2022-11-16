package com.example.atamerica.asynctasks;

import android.os.AsyncTask;

import com.example.atamerica.java_class.DataHelper;
import com.example.atamerica.models.AppEventModel;

import java.util.ArrayList;
import java.util.List;

public class AppEventListTask extends AsyncTask<Void, Void, List<AppEventModel>> {

    private List<AppEventModel> modelList;
    private String              query;
    private Object[]            params;

    public AppEventListTask(List<AppEventModel> modelList, String query, Object[] params) {
        this.modelList = new ArrayList<>();

        this.modelList = modelList;
        this.query = query;
        this.params = params;
    }

    @Override
    protected List<AppEventModel> doInBackground(Void... voids) {
        List<AppEventModel> models = DataHelper.Query.ReturnAsObjectList(query, AppEventModel.class, params);
        return models;
    }

    @Override
    protected void onPostExecute(List<AppEventModel> appEventModels) {
//        super.onPostExecute(appEventModels);
        modelList = appEventModels;
    }
}

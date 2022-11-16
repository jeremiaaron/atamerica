package com.example.atamerica.asynctasks;

import android.os.AsyncTask;

import com.example.atamerica.java_class.DataHelper;
import com.example.atamerica.models.EventDocumentModel;

import java.util.ArrayList;
import java.util.List;

public class EventDocumentListTask extends AsyncTask<Void, Void, List<EventDocumentModel>> {

    private List<EventDocumentModel> models;
    private String query;
    private Object[] params;

    public EventDocumentListTask(List<EventDocumentModel> models, String query, Object[] params) {
        this.models = new ArrayList<>();

        this.models = models;
        this.query = query;
        this.params = params;
    }

    @Override
    protected List<EventDocumentModel> doInBackground(Void... voids) {
        List<EventDocumentModel> models = DataHelper.Query.ReturnAsObjectList(query, EventDocumentModel.class, null);
        return models;
    }

    @Override
    protected void onPostExecute(List<EventDocumentModel> eventDocumentModels) {
        this.models = eventDocumentModels;
    }
}

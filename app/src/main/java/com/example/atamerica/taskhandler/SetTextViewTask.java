package com.example.atamerica.taskhandler;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.TextView;

@SuppressLint("StaticFieldLeak")
public class SetTextViewTask extends AsyncTask<String, Void, String> {
    TextView textView;

    public SetTextViewTask(TextView textView) { this.textView = textView; }

    @Override
    protected String doInBackground(String... strings) {
        return strings[0];
    }

    protected void onPostExecute(String s) { this.textView.setText(s); }
}

package com.example.atamerica.taskhandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

public class DownloadBitmapTask implements Callable<Bitmap> {

    private final String input;

    public DownloadBitmapTask(String url) {
        this.input = url;
    }

    @Override
    public Bitmap call() {
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new URL(input).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (Exception e) {
            Log.e("ERROR", "Error downloading image!");
            e.printStackTrace();
        }

        return bitmap;
    }
}

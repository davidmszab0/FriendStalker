package com.naegling.assassins.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Johan Nilsson
 */
public class HttpBitMap extends AsyncTask<URL, Object, Bitmap[]> {

    @Override
    protected Bitmap[] doInBackground(URL... params) {

        Bitmap[] bmImg = new Bitmap[params.length];

        try {
            for (int i = 0; i < params.length; i++) {
               HttpURLConnection conn = (HttpURLConnection) params[i].openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg[i] = BitmapFactory.decodeStream(is);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmImg;
    }
}

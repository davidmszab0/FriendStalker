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
 * Created by Johan on 2014-05-02.
 */
public class HttpBitMap extends AsyncTask<Object, Object, Object> {

    @Override
    protected Object doInBackground(Object... params) {

        Bitmap bmImg = null;

        try {
            URL url = new URL((String)params[0]);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmImg = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bmImg;
    }

}

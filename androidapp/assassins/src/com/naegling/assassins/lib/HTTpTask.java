
package com.naegling.assassins.lib;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class HTTpTask extends AsyncTask<Object, Object, Object>{

    static InputStream is = null;

    @Override
    protected Object doInBackground(Object... params) {
    	InputStream is = null;
        String json = "";
        
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost((String)params[0]);
            httpPost.setEntity(new UrlEncodedFormEntity((List<NameValuePair>)params[1]));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
<<<<<<< HEAD
                sb.append(line + "\n");
=======
                sb.append(line + "n");
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
            }
            is.close();

            json = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD

=======
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
        return json;
    }

}

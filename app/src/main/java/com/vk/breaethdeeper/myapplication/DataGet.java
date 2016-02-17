package com.vk.breaethdeeper.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mixmax on 17.02.16.
 */
public class DataGet extends AsyncTask<String, Void, String> {

    private Exception exception;
    private int result = 0;
    private ArrayAdapter arrayAdapter;
    String postsList[];
    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        String string="";

        try{
            URL url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            InputStream inputStream = urlConnection.getInputStream();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode ==  200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                parseResult(response);
                result = 1; // Successful
            }else{
                result = 0; //"Failed to fetch data!";
            }
        }catch (IOException e){
            Log.e(this.getClass().getName(), e.toString());
        } finally {

            try {
                if(br!=null) br.close();
                if(urlConnection!=null) urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    return string;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
/*
        if(result == 1){
            arrayAdapter = new ArrayAdapter(MainActivity.class, android.R.layout.activity_list_item, postsList);
            listView.setAdapter(arrayAdapter);
        }else{
            Log.e(TAG, "Failed to fetch data!");
        }*/
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }

    private void parseResult(String result) {
        try{
            JSONObject response = new JSONObject(result);
           JSONArray posts = response.optJSONArray("");

/*
            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                postsList.add(i,title);
            }*/

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

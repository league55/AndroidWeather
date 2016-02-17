package com.vk.breaethdeeper.myapplication;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Http Connection";
    final String URL = "http://api.openweathermap.org/data/2.5/weather?q=London&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
    Button button = null;
    TextView textView = null;

    private ArrayAdapter arrayAdapter = null;
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        new DataGet().execute(URL);

    }

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
                JSONArray weatherArray = (JSONArray) response.get("weather");
                JSONObject w = weatherArray.getJSONObject(0);

                String id = w.getString("id");
                String main =  w.getString("main");
                String description =  w.getString("description");
                String icon =  w.getString("icon");

                weather = new Weather(id,main,description,icon);


            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(result == 1){

                textView.setText(weather.toString());
            }else{
                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }


}

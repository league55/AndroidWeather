package com.vk.breaethdeeper.myapplication.jsonLoadersParcers;

import android.os.AsyncTask;
import android.util.Log;

import com.vk.breaethdeeper.myapplication.models.Weather;

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

/**
 * Created by mixmax on 19.02.16.
 * gets current weather from web
 */
public class WeatherParcer extends AsyncTask<String, Void, Weather> {
    private int result = 0;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
            /*pDialog = new ProgressDialog(ShowWeather.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/
    }

    @Override
    protected Weather doInBackground(String... urls) {
        Weather weather = null;
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        String string = "";

        try {
            java.net.URL url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            InputStream inputStream = urlConnection.getInputStream();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                weather = parseResult(response);

                result = 1; // Successful
            } else {
                result = 0; //"Failed to fetch data!";
            }
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString());
        } finally {

            try {
                if (br != null) br.close();
                if (urlConnection != null) urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return weather;
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    private Weather parseResult(String result) {
        Weather weather = null;
        try {

            JSONObject response = new JSONObject(result);

            int code = response.getInt("cod");

            if (code != 200) {
                return new Weather(code);
            }


            JSONArray weatherArray = (JSONArray) response.get("weather");
            JSONObject ob = weatherArray.getJSONObject(0);
            JSONObject mainPart = (JSONObject) response.get("main");
            JSONObject wind = (JSONObject) response.get("wind");


                String id = ob.getString("id");
                String main = ob.getString("main");
                String description = ob.getString("description");
                String temp = mainPart.getString("temp");
                String pressure = mainPart.getString("pressure");
                String humidity = mainPart.getString("humidity");
                String windSpeed = wind.getString("speed");
                String cityName = response.getString("name");
                String icon = ob.getString("icon");

                weather = new Weather(id, main, description, temp, pressure, humidity, windSpeed, cityName, icon, code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }

    @Override
    protected void onPostExecute(Weather w) {
        super.onPostExecute(w);

    }
}
package com.vk.breaethdeeper.myapplication.jsonLoadersParcers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vk.breaethdeeper.myapplication.App;
import com.vk.breaethdeeper.myapplication.activities.MainActivity;
import com.vk.breaethdeeper.myapplication.models.Forecast;
import com.vk.breaethdeeper.myapplication.models.Weather;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by mixmax on 25.02.16.
 */
public class WeatherHandler {

    public Forecast forecast;
    private Context context;
    private SharedPreferences sPref;
    public WeatherHandler(Context context) {
        this.context = context;
    }

    public Weather updateWeather(Weather oldWeather, Forecast oldForcast) {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getContext());

        Weather weather = oldWeather;
        forecast = oldForcast;
        WeatherParcer wp = new WeatherParcer();
        ForecastParcer fp = new ForecastParcer();
        if (hasConnection(context)) {
            wp.execute(sPref.getString(MainActivity.URLw, ""));
            fp.execute(sPref.getString(MainActivity.URLf, ""));
            try {
                weather = wp.get();
                weather.setCityName(oldWeather.getCityName());
                forecast = fp.get();
                Log.i("WH", forecast.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (weather.getCode() == 200) {
                Log.i("HANDLER", weather.toString() + "2");
                return weather;
            } else if (weather.getCode() == 404) {
                alertError("city wasn't found");
            } else {
                alertError("something went wrong >.<" + weather.getCode());
            }
        } else {

            alertError("no internet connection");
        }
        Log.i("HANDLER", "return oldWeather");
        return oldWeather;
    }


    private void alertError(String msg) {
        new AlertDialog.Builder(context)
                //  .setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("OK", null).show();
    }

    private boolean hasConnection(Context context) {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }


}

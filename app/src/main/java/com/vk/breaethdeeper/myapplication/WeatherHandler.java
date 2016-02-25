package com.vk.breaethdeeper.myapplication;

import android.app.AlertDialog;
import android.content.Context;

import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by mixmax on 25.02.16.
 */
public class WeatherHandler {

    private Context context;

    public WeatherHandler(Context context) {
        this.context = context;
    }

    public Weather updateWeather(String URL, WeatherParcer wp, Weather oldWeather) {
        Weather weather = new Weather();

        if (hasConnection(context)) {
            wp.execute(URL);
            try {
                weather = wp.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (weather.getCode() == 200) {
                return weather;
            } else if (weather.getCode() == 404) {
                alertError("city wasn't found");
            } else {
                alertError("something went wrong >.<" + weather.getCode());
            }
        } else {
            alertError("no internet connection");
        }
        return oldWeather;
    }


    public void alertError(String msg) {
        new AlertDialog.Builder(context)
                //  .setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("OK", null).show();
    }

    public boolean hasConnection(Context context) {
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

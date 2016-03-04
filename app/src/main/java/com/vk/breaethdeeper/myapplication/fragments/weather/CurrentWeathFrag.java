package com.vk.breaethdeeper.myapplication.fragments.weather;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.models.Weather;

/**
 * Created by mixmax on 29.02.16.
 */
public class CurrentWeathFrag extends Fragment {
    Weather weather;
    SharedPreferences sPref;
    String temp = "";
    String description;
    String humidity;
    String windSpeed;
    Activity activity;
    private String URL;
    private String city = "";
    private TextView cityTV;
    private TextView weatherMainTV;
    private TextView weatherDescrTV;
    private TextView weatherTempTV;
    private TextView windSpeedTV;
    private TextView humidityTV;
    private ProgressDialog pDialog;
    private int page;

    public static CurrentWeathFrag newInstance(Weather w) {
        CurrentWeathFrag currentWeathFrag = new CurrentWeathFrag();
        Bundle args = new Bundle();

        //  args.putString("someTitle", title);
        args.putString("city", w.getCityName());
        args.putString("description", w.getDescription());
        args.putString("temp", w.getTemp());
        args.putString("humidity", w.getHumidity());
        args.putString("windSpeed", w.getWindSpeed());
        currentWeathFrag.setArguments(args);
        return currentWeathFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        city = getArguments().getString("city");
        temp = getArguments().getString("temp");
        description = getArguments().getString("description");
        windSpeed = getArguments().getString("windSpeed");
        humidity = getArguments().getString("humidity");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_weather_frag, container, false);

        cityTV = (TextView) view.findViewById(R.id.tVCity);
        weatherDescrTV = (TextView) view.findViewById(R.id.tVdescription);
        weatherTempTV = (TextView) view.findViewById(R.id.tVtemp);
        windSpeedTV = (TextView) view.findViewById(R.id.tVwindSpeed);
        humidityTV = (TextView) view.findViewById(R.id.tVhuimdity);

        cityTV.setText(city);
        weatherDescrTV.setText(description);
        float tempF = Float.parseFloat(temp);
        tempF = Math.round(tempF);
        weatherTempTV.setText(tempF + "C");
        windSpeedTV.setText(windSpeed + "m/s");
        humidityTV.setText(humidity + "%");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void updateUI(Weather weather) {

        if (!isAdded() || activity == null) return;

        Log.i("SHOW_W", weather.toString());

        cityTV.setText(getString(R.string.your_city) + " " + weather.getCityName());
        weatherDescrTV.setText(weather.getDescription());
        float temp = Float.parseFloat(weather.getTemp());
        temp = Math.round(temp);
        weatherTempTV.setText(temp + "C");
        windSpeedTV.setText(weather.getWindSpeed() + "m/s");
        humidityTV.setText(weather.getHumidity() + "%");


    }
}

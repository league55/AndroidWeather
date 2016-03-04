package com.vk.breaethdeeper.myapplication.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mixmax on 01.03.16.
 */
public class Forecast {
    private int code;
    private List<Weather> weathers;

    public Forecast() {
        this.weathers = new ArrayList<>();
    }


    public Forecast(int code) {
        this.code = code;
    }

    public boolean addWeather(Weather weather) {
        return weathers.add(weather);
    }

    public List<Weather> getForecast() {
        return weathers;
    }

    private List<Weather> getFiveDayForecast() {
        List<Weather> fiveDay = new ArrayList<>();
        Iterator<Weather> it = getForecast().iterator();
        String oldDate = "";
        Weather weather;
        while (it.hasNext()) {
            weather = it.next();
            String newDate = weather.getDate();

            if (newDate.equals(oldDate)) continue;
            fiveDay.add(weather);
            oldDate = newDate;
        }
        return fiveDay;
    }

    public ArrayList<String> getFiveDayWeatherStr() {
        ArrayList<String> weathersMainStr = new ArrayList<>();
        List<Weather> weathers = getFiveDayForecast();
        Iterator<Weather> it = weathers.iterator();
        Weather w;
        StringBuilder sb;
        while (it.hasNext()) {
            sb = new StringBuilder();
            w = it.next();
            sb.append(w.getTemp() + "C ");
            sb.append(w.getDescription());

            weathersMainStr.add(sb.toString());
        }
        return weathersMainStr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Forecast{");
        for (Weather w : weathers) {
            sb.append("\n" + w.toString());
        }

        sb.append('}');

        return sb.toString();
    }
}

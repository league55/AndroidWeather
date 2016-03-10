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

    public ArrayList<Weather> getFiveDayForecast() {
        ArrayList<Weather> fiveDay = new ArrayList<>();
        Iterator<Weather> it = getForecast().iterator();
        String oldDate = getForecast().get(0).getDateShort();
        Weather weather = getForecast().get(0);
        ArrayList<Weather> temp = new ArrayList<>();
        while (it.hasNext()) {

            if (!oldDate.equals(weather.getDateShort())) {
                if (temp.size() == 1) {
                    fiveDay.add(temp.get(0));
                    temp.clear();
                } else if (temp.size() == 2) {
                    fiveDay.add(temp.get(1));
                    temp.clear();
                } else {
                    fiveDay.add(temp.get(temp.size() / 2));
                    temp.clear();
                }

            }
            temp.add(weather);
            oldDate = weather.getDateShort();
            weather = it.next();
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

    public ArrayList<String> getFiveDayWeatherStr(List<Weather> weathers) {
        ArrayList<String> weathersMainStr = new ArrayList<>();
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

    public ArrayList<String> getFiveDaySummury() {
        ArrayList<String> weathersSummury = new ArrayList<>();
        List<Weather> weathers = getFiveDayForecast();
        Iterator<Weather> it = weathers.iterator();
        Weather w;
        StringBuilder sb;
        while (it.hasNext()) {
            sb = new StringBuilder();
            w = it.next();
            sb.append(w.getDate());

            weathersSummury.add(sb.toString());
        }
        return weathersSummury;
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

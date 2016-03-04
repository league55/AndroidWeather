package com.vk.breaethdeeper.myapplication.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mixmax on 17.02.16.
 */
public class Weather {



    public String main;
    private String id;
    private String description;
    private String temp;
    private String pressure;
    private String humidity;
    private String windSpeed;
    private int windDeg;
    private String cityName;
    private String icon;
    private String date;
    private int code;
    private Date dateF;

    private String dateTime;

    public Weather(String id, String main, String description, String temp, String pressure, String humidity, String windSpeed, int windDeg, String cityName, String icon, String date, int code) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.cityName = cityName;
        this.icon = icon;
        setDate(date);
        this.code = code;
    }



    public Weather(int code) {
        this.code = code;
    }

    public Weather() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.dateF = new Date(Long.parseLong(date) * 1000);

        Locale locale = Locale.getDefault();
        SimpleDateFormat df = new SimpleDateFormat("E d MMM HH:mm", locale);
        this.dateTime = df.format(dateF);

        df = new SimpleDateFormat("E d MMM", locale);
        this.date = df.format(dateF);
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "Weather{" +
                "id='" + id + '\'' +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", temp='" + temp + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", cityName='" + cityName + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (!id.equals(weather.id)) return false;
        if (main != null ? !main.equals(weather.main) : weather.main != null) return false;
        if (description != null ? !description.equals(weather.description) : weather.description != null)
            return false;
        if (!temp.equals(weather.temp)) return false;
        if (pressure != null ? !pressure.equals(weather.pressure) : weather.pressure != null)
            return false;
        if (humidity != null ? !humidity.equals(weather.humidity) : weather.humidity != null)
            return false;
        if (windSpeed != null ? !windSpeed.equals(weather.windSpeed) : weather.windSpeed != null)
            return false;
        if (cityName != null ? !cityName.equals(weather.cityName) : weather.cityName != null)
            return false;
        return !(icon != null ? !icon.equals(weather.icon) : weather.icon != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (main != null ? main.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + temp.hashCode();
        result = 31 * result + (pressure != null ? pressure.hashCode() : 0);
        result = 31 * result + (humidity != null ? humidity.hashCode() : 0);
        result = 31 * result + (windSpeed != null ? windSpeed.hashCode() : 0);
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }




}

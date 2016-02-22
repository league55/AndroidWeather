package com.vk.breaethdeeper.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by mixmax on 17.02.16.
 */
public class Weather implements Parcelable {


    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        // распаковываем объект из Parcel
        public Weather createFromParcel(Parcel in) {
            Log.d("Weather", "createFromParcel");
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
    public String main;
    private String id;
    private String description;
    private String temp;
    private String pressure;
    private String humidity;
    private String windSpeed;
    private String cityName;
    private String icon;
    private int code;

    public Weather(String id, String main, String description, String temp, String pressure, String humidity, String windSpeed, String cityName, String icon, int code) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.cityName = cityName;
        this.icon = icon;
        this.code = code;
    }

    // конструктор, считывающий данные из Parcel
    private Weather(Parcel parcel) {
        Log.d("Weather", "Weather(Parcel parcel)");

        this.id = parcel.readString();
        this.main = parcel.readString();
        this.description = parcel.readString();
        this.temp = parcel.readString();
        this.pressure = parcel.readString();
        this.humidity = parcel.readString();
        this.windSpeed = parcel.readString();
        this.cityName = parcel.readString();
        this.icon = parcel.readString();
        this.code = parcel.readInt();
    }


    public Weather(int code) {
        this.code = code;
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

    @Override
    public int describeContents() {
        return 0;
    }

    // упаковываем объект в Parcel
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d("Weather", "writeToParcel");
        parcel.writeString(id);
        parcel.writeString(main);
        parcel.writeString(description);
        parcel.writeString(temp);
        parcel.writeString(pressure);
        parcel.writeString(humidity);
        parcel.writeString(windSpeed);
        parcel.writeString(cityName);
        parcel.writeString(icon);
        parcel.writeInt(code);
    }


}

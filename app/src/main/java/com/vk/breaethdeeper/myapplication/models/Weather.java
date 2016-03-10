package com.vk.breaethdeeper.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mixmax on 17.02.16.
 */
public class Weather implements Parcelable {


    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
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
    private int windDeg;
    private String cityName;
    private String icon;
    private String date;
    private int code;
    private String condition;
    private String dateTime;
    private String dateShort;
    private Date pureDate;



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
        this.date = date;

        setDatesStr(date);

        this.code = code;
        this.condition = setCondition(id);
    }

    public Weather(int code) {
        this.code = code;
    }

    public Weather() {
    }


    protected Weather(Parcel in) {
        main = in.readString();
        id = in.readString();
        description = in.readString();
        temp = in.readString();
        pressure = in.readString();
        humidity = in.readString();
        windSpeed = in.readString();
        windDeg = in.readInt();
        cityName = in.readString();
        icon = in.readString();
        date = in.readString();
        setDatesStr(date);
        code = in.readInt();
        condition = in.readString();

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
        float tempF = Float.parseFloat(temp);
        tempF = Math.round(tempF);
        int t = (int) tempF;
        return t + "";
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

    public String getDateShort() {
        return dateShort;
    }

    public String getDate() {
        return date;
    }

    public Date getPureDate() {
        return pureDate;
    }

    public int getHour() {
        int hour;
        String[] dateParts = this.getDateTime().split(" ");
        String time = dateParts[dateParts.length - 1];
        hour = Integer.parseInt(time.charAt(0) + time.charAt(1) + "");
        return hour;

    }

    public void setDatesStr(String date) {
        this.pureDate = new Date(Long.parseLong(date) * 1000);

        Locale locale = Locale.getDefault();
        SimpleDateFormat df = new SimpleDateFormat("E d MMM HH:mm", locale);
        this.dateTime = df.format(pureDate);

        df = new SimpleDateFormat("E d MMM", locale);
        this.dateShort = df.format(pureDate);
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

    public String getCondition() {
        return condition;
    }

    public String setCondition(String idS) {
        int id = Integer.parseInt(idS);
        if (id > 957) {
            return "STORM";
        } else if (id == 951) {
            return "CALM";
        } else if (id == 900 || id == 902 || id == 781) {
            return "TORNADO";
        } else if (id > 800 && id < 805) {
            return "CLOUDS";
        } else if (id == 800) {
            return "CLEAR";
        } else if (id == 701 || id == 741 || id == 711) {
            return "FOG";
        } else if (id > 599 && id < 622) {
            return "SNOW";
        } else if (id > 299 && id < 532) {
            return "RAIN";
        } else if (id > 199 && id < 233) {
            return "THUNDERSTORM";
        } else return "DEFAULT";

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main);
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(temp);
        dest.writeString(pressure);
        dest.writeString(humidity);
        dest.writeString(windSpeed);
        dest.writeInt(windDeg);
        dest.writeString(cityName);
        dest.writeString(icon);
        dest.writeString(date);
        dest.writeInt(code);
        dest.writeString(condition);

    }
}

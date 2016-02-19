package com.vk.breaethdeeper.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mixmax on 17.02.16.
 */
public class Weather implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
    private String id;
    private String main;
    private String description;
    private String icon;

    public Weather(String id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public Weather(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);

        this.id = data[0];
        this.main = data[1];
        this.description = data[2];
        this.icon = data[3];
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id='" + id + '\'' +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{getId(), getMain(), getDescription(), getIcon()});


    }
}

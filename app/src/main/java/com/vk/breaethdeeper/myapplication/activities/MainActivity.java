package com.vk.breaethdeeper.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.Weather;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    final String SAVED_CITY = "SAVED_CITY";
    SharedPreferences sPref;
    TextView welcome;
    Button confirm;
    EditText cityName;
    CheckBox cbSaveCity;


    private String URL;

    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cbSaveCity = (CheckBox) findViewById(R.id.cbSaveCity);
        welcome = (TextView) findViewById(R.id.tfWelcome);
        cityName = (EditText) findViewById(R.id.city);
        confirm = (Button) findViewById(R.id.confirmCity);
        if (hasSavedWeather()) {
            loadWeather();
        }

        confirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (hasConnection(this)) {

            URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText() + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
            WeatherParcer wp = (WeatherParcer) new WeatherParcer(MainActivity.this).execute(URL);
            try {
                weather = wp.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (cbSaveCity.isChecked()) saveCity();


            if (weather.getCode() == 200) {
                Intent intent = new Intent(this, ShowWeather.class);
                intent.putExtra("weather", weather);
                intent.putExtra("URL", URL);
                startActivity(intent);
            } else if (weather.getCode() == 404) {
                alertError("city wasn't found");
            } else {
                alertError("no internet connection");
            }


        } else {
            alertError("something went wrong >.<");
        }


    }


    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    void saveCity() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_CITY, cityName.getText().toString());
        ed.commit();
    }

    void loadWeather() {
        sPref = getPreferences(MODE_PRIVATE);
        cityName.setText(sPref.getString(SAVED_CITY, ""));

        if (hasConnection(this)) {

            URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText() + "&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
            WeatherParcer wp = (WeatherParcer) new WeatherParcer(MainActivity.this).execute(URL);
            try {
                weather = wp.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (weather.getCode() == 200) {
                Intent intent = new Intent(this, ShowWeather.class);
                intent.putExtra("weather", weather);
                intent.putExtra("URL", URL);
                startActivity(intent);
            } else if (weather.getCode() == 404) {
                alertError("city wasn't found");
            } else {
                alertError("Woops, somthing went wrong >.<");
            }
        }

        alertError("no internet connection");

    }

    boolean hasSavedWeather() {
        sPref = getPreferences(MODE_PRIVATE);
        return sPref.contains("SAVED_CITY") && (!sPref.getString(SAVED_CITY, "").equals(""));
    }

    public void alertError(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("OK", null).show();
    }


}

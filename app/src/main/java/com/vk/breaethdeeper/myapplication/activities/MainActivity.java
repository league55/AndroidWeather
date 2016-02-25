package com.vk.breaethdeeper.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.Weather;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    final String SAVED_CITY = "SAVED_CITY";
    private SharedPreferences sPref;
    private TextView welcome;
    private Button confirm;
    private EditText EditTcityName;
    private CheckBox cbSaveCity;
    private Spinner spinner_lang;
    private String lang = "en";
    private String cityName;


    private String URL;

    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<String> languages_code = new ArrayList<String>();
        for (String s : getResources().getStringArray(R.array.lang_codes)) {
            languages_code.add(s);
        }
        final ArrayList<String> languages = new ArrayList<String>();
        for (String s : getResources().getStringArray(R.array.languages)) {
            languages.add(s);
        }
        sPref = getPreferences(MODE_PRIVATE);
        if (sPref.contains("lang")) lang = sPref.getString("lang", "en");
        if (sPref.contains(SAVED_CITY)) cityName = sPref.getString(SAVED_CITY, "BoraBora");

        cbSaveCity = (CheckBox) findViewById(R.id.cbSaveCity);
        welcome = (TextView) findViewById(R.id.tfWelcome);
        EditTcityName = (EditText) findViewById(R.id.city);
        confirm = (Button) findViewById(R.id.confirmCity);
        spinner_lang = (Spinner) findViewById(R.id.spinner_lang);


        confirm.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);

        spinner_lang.setAdapter(adapter);
        //getResources().getStringArray(R.array.languages)
        spinner_lang.setSelection(adapter.getPosition(languages.get(languages_code.indexOf(sPref.getString("lang", "en")))));
        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner_lang.getSelectedItem().toString()) {

                    case "English":
                        setLang("en");
                        break;
                    case "Русский":
                        setLang("ru");
                        break;
                    case "Espanol":
                        setLang("es");
                        break;
                    case "Українська":
                        setLang("ua");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (hasSavedWeather()) loadWeather();

    }

    @Override
    public void onClick(View v) {
        cityName = EditTcityName.getText().toString();
        if (hasConnection(this)) {

            URL = getURL();


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
                alertError("something went wrong >.<" + weather.getCode());
            }
        } else {
            alertError("no internet connection");
        }


    }

    private String getURL() {
        return "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&lang=" + lang + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
    }

    private String chooseLang() {
        Log.i("MAIN", spinner_lang.getSelectedItem().toString());
        switch (spinner_lang.getSelectedItem().toString()) {

            case "English":
                lang = "en";
                break;
            case "Русский":
                lang = "ru";
                break;
            case "Espanol":
                lang = "es";
                break;
            case "Українська":
                lang = "ua";
                break;

        }
        return lang;
    }


    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    void saveCity() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_CITY, EditTcityName.getText().toString());
        ed.commit();
    }

    void setLang(String lang) {
        this.lang = lang;
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("lang", lang);
        ed.commit();
    }

    void loadWeather() {
        //   sPref = getPreferences(MODE_PRIVATE);
        //   EditTcityName.setText(cityName);

        if (hasConnection(this)) {

            URL = getURL();
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
        // sPref = getPreferences(MODE_PRIVATE);
        return sPref.contains("SAVED_CITY") && (!sPref.getString(SAVED_CITY, "").equals("")) && sPref.contains("lang");
    }

    public void alertError(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("OK", null).show();
    }


}

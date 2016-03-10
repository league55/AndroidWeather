package com.vk.breaethdeeper.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.jsonLoadersParcers.CountriesParcer;
import com.vk.breaethdeeper.myapplication.jsonLoadersParcers.WeatherHandler;
import com.vk.breaethdeeper.myapplication.models.Weather;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String SAVED_CITY = "SAVED_CITY";
    public static final String URLw = "URLw";
    public static final String URLf = "URLf";
    final String SAVED_LANG = "SAVED_LANG";
    final String SAVED_COUNTRY_CODE = "SAVED_COUNTRY_CODE";
    final String SAVED_COUNTRY = "SAVED_COUNTRY";
    final String DO_SAVE = "DO_SAVE";
    private final ArrayList<String> languages_code = new ArrayList<String>();
    private final ArrayList<String> languages = new ArrayList<String>();
    private HashMap<String, String> countriesMap;
    private SharedPreferences sPref;
    private TextView welcome;
    private Button confirm;
    private EditText EditTcityName;
    private CheckBox cbSaveCity;
    private Spinner spinner_lang;
    private Spinner spinner_country;
    private String lang = "en";

    private String cityName;
    private String countryName;
    private String countryCode;
    private ArrayAdapter<String> lang_adapter;
    private ArrayAdapter<String> countries_adapter;
    private String[] URL;
    private WeatherHandler wh;
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        getLanguagesFromRes();
        if (hasConnection(this)) {
            if (!getIntent().getBooleanExtra("isForsed", false) && hasSavedWeather()) {
                Intent intent = new Intent(this, ShowWeather.class);
                getURL();
                startActivity(intent);
            }
        }



        if (sPref.contains(SAVED_LANG)) {
            lang = sPref.getString(SAVED_LANG, "en");
            setLang(lang);
        }
        if (sPref.contains(SAVED_CITY)) cityName = sPref.getString(SAVED_CITY, "BoraBora");
        if (sPref.contains(SAVED_COUNTRY_CODE))
            countryCode = sPref.getString(SAVED_COUNTRY_CODE, "UA");


        countriesMap = new CountriesParcer().loadCountriesJSONFromAsset(this, "countries.json");
        final ArrayList<String> countries = new ArrayList<String>(countriesMap.keySet());
        countries.add(0, getResources().getString(R.string.select_country));

        cbSaveCity = (CheckBox) findViewById(R.id.cbSaveCity);
        welcome = (TextView) findViewById(R.id.tfWelcome);
        EditTcityName = (EditText) findViewById(R.id.city);
        confirm = (Button) findViewById(R.id.confirmCity);
        spinner_lang = (Spinner) findViewById(R.id.spinner_lang);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);

        confirm.setOnClickListener(this);

        if (cityName != null) EditTcityName.setText(cityName);

        lang_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        spinner_lang.setAdapter(lang_adapter);
        spinner_lang.setSelection(lang_adapter.getPosition(languages.get(languages_code.indexOf(sPref.getString(SAVED_LANG, "en")))));
        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinner_lang.getSelectedItemPosition();
                saveLang(languages_code.get(pos));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        countries_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        spinner_country.setAdapter(countries_adapter);
        spinner_country.setSelection(countries_adapter.getPosition(sPref.getString(SAVED_COUNTRY, "")));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryName = spinner_country.getSelectedItem().toString();
                String code = countriesMap.get(countryName);
                saveCountry(code, countryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_lang.getSelectedItem().toString();
            }
        });
    }


    private void getLanguagesFromRes() {
        for (String s : getResources().getStringArray(R.array.lang_codes)) {
            languages_code.add(s);
        }
        for (String s : getResources().getStringArray(R.array.languages)) {
            languages.add(s);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (cityName != null) EditTcityName.setText(cityName);
        if (lang != null)
            spinner_lang.setSelection(lang_adapter.getPosition(languages.get(languages_code.indexOf(sPref.getString(SAVED_LANG, "en")))));
        if (countryName != null)
            spinner_country.setSelection(countries_adapter.getPosition(sPref.getString(SAVED_COUNTRY, "")));
    }

    @Override
    public void onClick(View v) {
        if (!hasConnection(this)) {
            Toast.makeText(this, getResources().getString(R.string.pls_check_connection), Toast.LENGTH_SHORT).show();
        } else {
            cityName = EditTcityName.getText().toString();

            saveCity(cbSaveCity.isChecked());
            setLang(lang);

            getURL();
            Intent intent = new Intent(this, ShowWeather.class);

            startActivity(intent);
        }
    }

    private String[] getURL() {
        String[] URL = new String[2];
        String cityName = sPref.getString(SAVED_CITY, null);
        String lang = sPref.getString(SAVED_LANG, "en");
        String countryCode = sPref.getString(SAVED_COUNTRY_CODE, getResources().getString(R.string.select_country));
        String countryName = sPref.getString(SAVED_COUNTRY, "");
        String cityNameEncoded = cityName;
        try {
            cityNameEncoded = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (countryName.equals(getResources().getString(R.string.select_country))) {
            URL[0] = "http://api.openweathermap.org/data/2.5/weather?q=" + cityNameEncoded + "&lang=" + lang + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
            URL[1] = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityNameEncoded + "&lang=" + lang + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";

        } else {
            URL[0] = "http://api.openweathermap.org/data/2.5/weather?q=" + cityNameEncoded + "," + countryCode + "&lang=" + lang + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
            URL[1] = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityNameEncoded + "," + countryCode + "&lang=" + lang + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";

        }

        saveURLs(URL[0], URL[1]);
        return URL;
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

    void saveCity(boolean doSave) {
        // sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_CITY, EditTcityName.getText().toString().trim());
        //need city name any way, use "doSave" to know if user wants to get city inserted automaticly
        ed.putBoolean(DO_SAVE, doSave);
        ed.commit();
    }

    void saveCountry(String code, String countryName) {
        //  sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_COUNTRY_CODE, code);
        ed.putString(SAVED_COUNTRY, countryName);
        ed.commit();
    }

    void saveLang(String lang) {
        this.lang = lang;
        //   sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LANG, lang);
        ed.commit();

    }

    void saveURLs(String w, String f) {

        //    sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(URLw, w);
        ed.putString(URLf, f);
        ed.commit();

    }

    void setLang(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        for (Locale l : Locale.getAvailableLocales()) {
            System.out.println(l.toString());
        }


    }




    boolean hasSavedWeather() {
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sPref != null)
            return sPref.contains("SAVED_CITY") && (!sPref.getString(SAVED_CITY, "").equals("")) && sPref.contains(SAVED_LANG);
        else return false;
    }

    public void alertError(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("OK", null).show();
    }


}

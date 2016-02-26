package com.vk.breaethdeeper.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.Weather;

import java.util.concurrent.ExecutionException;

public class AppPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    public static class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        SharedPreferences sharedPref;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            Context context = null;

            if (isAdded()) {
                context = getActivity();
                PreferenceManager.setDefaultValues(context, R.xml.preferences, false);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

            }


            initSummary(getPreferenceScreen());
            Preference pref = getPreferenceScreen().findPreference("SAVE_PREF");
            pref.setOnPreferenceClickListener(this);

        }


        private void initSummary(Preference p) {
            if (p instanceof ListPreference) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry() + "1");
            }
            if (p instanceof EditTextPreference) {
                EditTextPreference editTextPref = (EditTextPreference) p;
                if (p.getKey().toString().toLowerCase().contains("city")) {
                    p.setSummary(editTextPref.getKey() + "2");
                    ((EditTextPreference) p).setText(editTextPref.getText() + "3");
                } else {
                    p.setSummary(editTextPref.getText() + "4");
                }
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Log.i("PREF", "Click" + preference.getTitle());
            switch (preference.getKey()) {
                case "SAVE_PREF":
                    foo();
                  /*  Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);*/
                    break;
            }

            return false;
        }

        private void foo() {
            Weather weather = new Weather();
            String URL = getURL();
            WeatherParcer wp = (WeatherParcer) new WeatherParcer().execute(URL);
            try {
                weather = wp.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (weather.getCode() == 200) {
                Intent intent = new Intent(getActivity(), ShowWeather.class);
                intent.putExtra("weather", weather);
                intent.putExtra("URL", URL);
                startActivity(intent);
            } else if (weather.getCode() == 404) {
                // alertError("city wasn't found");
            } else {
                // alertError("something went wrong >.<" + weather.getCode());
            }
        }

        private String getURL() {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            return "http://api.openweathermap.org/data/2.5/weather?q=" + sharedPref.getString("SAVED_CITY", "") + "&lang=" + sharedPref.getString("SAVED_LANG", "en") + "&units=metric&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
        }
    }


}



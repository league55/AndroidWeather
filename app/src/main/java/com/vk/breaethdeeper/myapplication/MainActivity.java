package com.vk.breaethdeeper.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView welcome;
    Button confirm;
    EditText cityName;
    Spinner countrySpinner;
    Spinner citySpinner;


    String city = "";
    TextView textView = null;
    SQLiteDatabase db;
    DBHelper dbHelper;


    private String URL;

    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Typeface type= Typeface.createFromAsset(getAssets(), "fonts/book.TTF");


        welcome = (TextView) findViewById(R.id.tfWelcome);
        cityName = (EditText) findViewById(R.id.city);
        confirm = (Button) findViewById(R.id.confirmCity);


        dbHelper = new DBHelper(this);

        db = dbHelper.getWritableDatabase();

        ArrayList<String> country_name_db = dbHelper.parseCountries(db);

        ArrayAdapter countriesAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, country_name_db);
        ArrayAdapter citiesAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, new String[]{"Select country"});
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        countrySpinner.setAdapter(countriesAdapter);
        citySpinner.setAdapter(citiesAdapter);



        confirm.setOnClickListener(this);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select country")) {
                    //do nothing.

                } else {
                    String country = parent.getItemAtPosition(position).toString();
                    String[] specialCities = dbHelper.getCitiesByCountry(db, country);
                    citySpinner.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, specialCities));
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cityName = parent.getItemAtPosition(position).toString();
                if (cityName.equals("Select city") || cityName.equals("Select country")) {

                } else {
                    Integer cityId = dbHelper.getCityId(db, cityName);

                    URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityId + "&units=metric&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
                    Log.d("DB", cityId + " City ID, " + cityName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        // URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText() + "&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";

        if (!cityName.getText().equals("Select city") || cityName.getText().equals("Select country")) {
            city = cityName.getText().toString();
            //  WeatherParcer wp = (WeatherParcer) new WeatherParcer(this,weather).execute(URL);
            Intent intent = new Intent(this, ShowWeather.class);
            intent.putExtra("URL", URL);
            startActivity(intent);
        }

        db.close();
    }






}

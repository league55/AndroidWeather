package com.vk.breaethdeeper.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ShowWeather extends AppCompatActivity implements View.OnClickListener {


    String city = "";
    TextView cityTV;
    TextView weatherMainTV;
    TextView weatherDescrTV;

    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        weather = getIntent().getParcelableExtra("weather");
        city = getIntent().getStringExtra("city");

        //View rootView = inflater.inflate(R.layout.activity_main, container, false);
        cityTV = (TextView) findViewById(R.id.tVCity);
        weatherMainTV = (TextView) findViewById(R.id.tVmain);
        weatherDescrTV = (TextView) findViewById(R.id.tVdescription);

        String main = weather.getMain();
        String description = weather.getDescription();
        cityTV.setText("Your city is " + city);
        weatherMainTV.setText("it is " + main);
        weatherDescrTV.setText(description);

    }


    @Override
    public void onClick(View v) {

    }
}

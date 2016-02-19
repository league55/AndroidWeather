package com.vk.breaethdeeper.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;

import java.util.concurrent.ExecutionException;

public class ShowWeather extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    String city = "";
    TextView cityTV;
    TextView weatherMainTV;
    TextView weatherDescrTV;
    String mainS;
    String descriptionS;
    private ProgressDialog pDialog;
    private Weather weather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        final String URL = getIntent().getStringExtra("URL");


        WeatherParcer wp = (WeatherParcer) new WeatherParcer(ShowWeather.this).execute(URL);
        pDialog = new ProgressDialog(ShowWeather.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        try {
            weather = wp.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        cityTV = (TextView) findViewById(R.id.tVCity);
        weatherMainTV = (TextView) findViewById(R.id.tVmain);
        weatherDescrTV = (TextView) findViewById(R.id.tVdescription);


        cityTV.setText("Your city is " + weather.getCityName());
        weatherMainTV.setText("temp is " + weather.getTemp());
        weatherDescrTV.setText("it is " + weather.getDescription());
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onClick(View v) {

    }


}

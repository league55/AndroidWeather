package com.vk.breaethdeeper.myapplication.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vk.breaethdeeper.myapplication.Fragments.PrefFragment;
import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.Weather;

import java.util.concurrent.ExecutionException;

public class ShowWeather extends AppCompatActivity implements View.OnClickListener {

    private String city = "";
    private TextView cityTV;
    private TextView weatherMainTV;
    private TextView weatherDescrTV;
    private TextView weatherTempTV;

    private ProgressDialog pDialog;
    private Weather weather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        final String URL = getIntent().getStringExtra("URL");
        Weather weather = getIntent().getParcelableExtra("weather");


        WeatherParcer wp = (WeatherParcer) new WeatherParcer(ShowWeather.this).execute(URL);
        pDialog = new ProgressDialog(ShowWeather.this);
        pDialog.setMessage(R.string.please_wait + "");
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
        weatherTempTV = (TextView) findViewById(R.id.tVtemp);


        cityTV.setText(getString(R.string.your_city) + " " + weather.getCityName());

        weatherDescrTV.setText(weather.getDescription());
        weatherTempTV.setText(weather.getTemp() + "C");
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, R.string.settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            //startActivity(new Intent(this, SettingsActivity.class));
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PrefFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

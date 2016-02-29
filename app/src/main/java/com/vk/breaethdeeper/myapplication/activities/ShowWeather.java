package com.vk.breaethdeeper.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.jsonLoadersParcers.WeatherHandler;
import com.vk.breaethdeeper.myapplication.jsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.models.Weather;

public class ShowWeather extends AppCompatActivity implements View.OnClickListener {
    Weather weather;
    private String URL;
    private String city = "";
    private TextView cityTV;
    private TextView weatherMainTV;
    private TextView weatherDescrTV;
    private TextView weatherTempTV;
    private TextView windSpeedTV;
    private TextView humidityTV;
    private WeatherHandler weatherHandler;
    private WeatherParcer weatherParcer;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        setCustomActionBar();
        weatherHandler = new WeatherHandler(this);
        weatherParcer = new WeatherParcer();

        URL = getIntent().getStringExtra("URL");

        cityTV = (TextView) findViewById(R.id.tVCity);
        weatherMainTV = (TextView) findViewById(R.id.tVmain);
        weatherDescrTV = (TextView) findViewById(R.id.tVdescription);
        weatherTempTV = (TextView) findViewById(R.id.tVtemp);
        windSpeedTV = (TextView) findViewById(R.id.tVwindSpeed);
        humidityTV = (TextView) findViewById(R.id.tVhuimdity);


        weather = weatherHandler.updateWeather(URL, weatherParcer, weather);
        updateUI(weather);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        URL = getIntent().getStringExtra("URL");
        // weather = weatherHandler.updateWeather(URL, weatherParcer, weather);
        //  updateUI(weather);
    }

    private void setCustomActionBar() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.toolbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(getString(R.string.app_name));

        ImageButton updateButton = (ImageButton) mCustomView.findViewById(R.id.updateButton);
        ImageButton homeButton = (ImageButton) mCustomView.findViewById(R.id.homeButton);
        updateButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateButton:
                Toast.makeText(getApplicationContext(), getString(R.string.refreshing),
                        Toast.LENGTH_LONG).show();

                break;
            case R.id.homeButton:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("isForsed", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

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
            Intent i = new Intent(this, AppPreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateUI(Weather weather) {

        Log.i("SHOW_W", weather.toString());

        pDialog = new ProgressDialog(ShowWeather.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setCancelable(false);
        pDialog.show();

        cityTV.setText(getString(R.string.your_city) + " " + weather.getCityName());
        weatherDescrTV.setText(weather.getDescription());
        float temp = Float.parseFloat(weather.getTemp());
        temp = Math.round(temp);
        weatherTempTV.setText(temp + "C");
        windSpeedTV.setText(weather.getWindSpeed() + "m/s");
        humidityTV.setText(weather.getHumidity() + "%");

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


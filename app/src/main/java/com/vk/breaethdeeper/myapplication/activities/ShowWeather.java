package com.vk.breaethdeeper.myapplication.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.breaethdeeper.myapplication.Fragments.PrefFragment;
import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.Weather;
import com.vk.breaethdeeper.myapplication.WeatherHandler;

public class ShowWeather extends AppCompatActivity implements View.OnClickListener {
    Weather weather;
    private String URL;
    private String city = "";
    private TextView cityTV;
    private TextView weatherMainTV;
    private TextView weatherDescrTV;
    private TextView weatherTempTV;
    private WeatherHandler weatherHandler;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        setCustomActionBar();
        weatherHandler = new WeatherHandler(this);
        weather = getIntent().getParcelableExtra("weather");
        URL = getIntent().getStringExtra("URL");
        WeatherParcer wp = (WeatherParcer) new WeatherParcer().execute(URL);



        cityTV = (TextView) findViewById(R.id.tVCity);
        weatherMainTV = (TextView) findViewById(R.id.tVmain);
        weatherDescrTV = (TextView) findViewById(R.id.tVdescription);
        weatherTempTV = (TextView) findViewById(R.id.tVtemp);


        cityTV.setText(getString(R.string.your_city) + " " + weather.getCityName());
        update();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        update();
    }

    private void setCustomActionBar() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.toolbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(getString(R.string.app_name));

        ImageButton updateButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.refreshing),
                        Toast.LENGTH_LONG).show();
                update();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

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

    void update() {
        pDialog = new ProgressDialog(ShowWeather.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setCancelable(false);
        pDialog.show();

        weather = weatherHandler.updateWeather(URL, new WeatherParcer(), weather);
        cityTV.setText(getString(R.string.your_city) + " " + weather.getCityName());

        weatherDescrTV.setText(weather.getDescription());
        weatherTempTV.setText(weather.getTemp() + "C");

        if (pDialog.isShowing())
            pDialog.dismiss();

    }
}


package com.vk.breaethdeeper.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.breaethdeeper.myapplication.App;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.fragments.weather.CurrentWeathFrag;
import com.vk.breaethdeeper.myapplication.fragments.weather.ForecastFrag;
import com.vk.breaethdeeper.myapplication.jsonLoadersParcers.WeatherHandler;
import com.vk.breaethdeeper.myapplication.jsonLoadersParcers.WeatherParcer;
import com.vk.breaethdeeper.myapplication.models.Forecast;
import com.vk.breaethdeeper.myapplication.models.Weather;

public class ShowWeather extends AppCompatActivity implements View.OnClickListener {
    static Forecast forecast;
    static Weather weather;
    FragmentPagerAdapter adapterViewPager;
    private String[] URL;
    private String city = "";
    private WeatherHandler weatherHandler;
    private WeatherParcer weatherParcer;

    private CurrentWeathFrag currentWeathFrag;
    private ForecastFrag forecastFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        setCustomActionBar();
        weatherHandler = new WeatherHandler(this);
        weatherParcer = new WeatherParcer();

        if (weather == null) weather = new Weather();
        //city = getSharedPreferences("preferences", MODE_PRIVATE).getString(MainActivity.SAVED_CITY,  "");
        city = PreferenceManager.getDefaultSharedPreferences(this).getString(MainActivity.SAVED_CITY, "");
        //  weather.setCityName(city);
        weather.setCityName(city);
        weather = weatherHandler.updateWeather(weather, forecast);

        forecast = weatherHandler.forecast;

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void setCustomActionBar() {
        ActionBar mActionBar = getSupportActionBar();
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
                weatherHandler.updateWeather(weather, forecast);
                if (currentWeathFrag != null) currentWeathFrag.updateUI(weather);
                if (forecastFrag != null) forecastFrag.updateUI(forecast);
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

            Intent i = new Intent(this, AppPreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return CurrentWeathFrag.newInstance(ShowWeather.weather);

                case 1:
                    return ForecastFrag.newInstance(ShowWeather.forecast.getFiveDayForecast());

            }
            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            String[] titles = new String[]{App.getContext().getString(R.string.current), App.getContext().getString(R.string.forecast)};
            return titles[position];
        }

    }

}




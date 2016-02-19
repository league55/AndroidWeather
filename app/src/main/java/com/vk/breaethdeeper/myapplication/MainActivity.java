package com.vk.breaethdeeper.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private ProgressDialog pDialog;
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

        String[] country_name_db = parseCountries(db);

        ArrayAdapter countriesAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, country_name_db);
        ArrayAdapter citiesAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, new String[]{"Choose country"});
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        countrySpinner.setAdapter(countriesAdapter);
        citySpinner.setAdapter(citiesAdapter);



        confirm.setOnClickListener(this);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose country")) {
                    //do nothing.

                } else {
                    String[] columns = null;
                    String selection = null;
                    String[] selectionArgs = null;
                    String groupBy = null;
                    String having = null;
                    String orderBy = null;

                    // курсор
                    Cursor c = null;

                    String country = parent.getItemAtPosition(position).toString();
                    Cursor code = db.query("countries", new String[]{"code"}, "name=?", new String[]{country}, null, null, null);
                    int index = code.getColumnIndex("code");
                    code.moveToFirst();
                    String countryCode = code.getString(index);
                    Cursor citiesName = db.query("cities", new String[]{"name"}, "country=?", new String[]{countryCode}, null, null, null);
                    index = citiesName.getColumnIndex("name");
                    citiesName.moveToFirst();
                    String[] specialCities = new String[citiesName.getCount()];
                    do {
                        Log.i("DB", citiesName.getString(index));
                        specialCities[citiesName.getPosition()] = citiesName.getString(index);
                    } while (citiesName.moveToNext());

                    code.close();
                    citiesName.close();

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        URL = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText() + "&&APPID=1b1a14fc9f3424c03af8a8da7a21c62d";
        city = cityName.getText().toString();
        new WeatherParcer().execute(URL);

    }

    private String[] parseCountries(SQLiteDatabase db) {
        Cursor c = db.query("countries", null, null, null, null, null, null);
        String[] country_name_db = new String[(c.getCount() + 1)];
        country_name_db[0] = "Choose country";
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке

            int nameColIndex = c.getColumnIndex("name");
            int i = 1;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("DB", "name = " + c.getString(nameColIndex));
                country_name_db[i] = c.getString(nameColIndex);
                i++;
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("DB", "0 rows");
        c.close();
        return country_name_db;
    }


    public class WeatherParcer extends AsyncTask<String, Void, String> {

        String postsList[];
        private Exception exception;
        private int result = 0;
        private ArrayAdapter arrayAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;
            String string = "";

            try {
                java.net.URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                InputStream inputStream = urlConnection.getInputStream();
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.toString());
            } finally {

                try {
                    if (br != null) br.close();
                    if (urlConnection != null) urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return string;
        }


        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            /* Close Stream */
            if (null != inputStream) {
                inputStream.close();
            }
            return result;
        }

        private void parseResult(String result) {
            try {

                JSONObject response = new JSONObject(result);
                JSONArray weatherArray = (JSONArray) response.get("weather");
                JSONObject w = weatherArray.getJSONObject(0);

                String id = w.getString("id");
                String main = w.getString("main");
                String description = w.getString("description");
                String icon = w.getString("icon");

                weather = new Weather(id, main, description, icon);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (result == 1) {

                Intent intent = new Intent(MainActivity.this, ShowWeather.class);
                intent.putExtra("weather", weather);
                intent.putExtra("city", city);
                startActivity(intent);
            } else {
                Log.e("MainActivity", "Failed to fetch data!");
            }

        }
    }

}

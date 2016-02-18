package com.vk.breaethdeeper.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
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
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView welcome;
    Button confirm;
    EditText cityName;
    Spinner countrySpinner;


    String city = "";
    TextView textView = null;
    SQLiteDatabase db;
    DBHelper dbHelper;
    /* private String[] parseCities(SQLiteDatabase db){
         Cursor c = db.query("cities", null, null, null, null, null, null);
         String[] cities_name_db=new String[(c.getCount()+1)];
         cities_name_db[0]="Choose city";
         // ставим позицию курсора на первую строку выборки
         // если в выборке нет строк, вернется false
         if (c.moveToFirst()) {

             // определяем номера столбцов по имени в выборке

             int nameColIndex = c.getColumnIndex("name");
             int i = 1;

             do {
                 // получаем значения по номерам столбцов и пишем все в лог
                 Log.d("DB", "name = " + c.getString(nameColIndex));
                 cities_name_db[i]=c.getString(nameColIndex);
                 i++;
                 // переход на следующую строку
                 // а если следующей нет (текущая - последняя), то false - выходим из цикла
             } while (c.moveToNext());
         } else
             Log.d("DB", "0 rows");
         c.close();
         return cities_name_db;
     }*/
    Map<Integer, String> citiesName = new ArrayMap<>(209600);
    Map<Integer, String> citiesCountry = new ArrayMap<>(209600);
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
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        countrySpinner.setAdapter(countriesAdapter);

     /* Map<String,String> countries = null;

            countries = loadCountriesJSONFromAsset();


        Set<String> keySet = countries.keySet();
        for(String key: keySet){
            ContentValues values = new ContentValues();
            long retvalue = 0;

            values.put("name", countries.get(key));
            values.put("code", key);
            Log.i("DB", key+" - "+countries.get(key));
           retvalue = db.insertWithOnConflict("countries", null, values, SQLiteDatabase.CONFLICT_NONE);

    }*/


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
                    do {
                        Log.i("DB", citiesName.getString(index));

                    } while (citiesName.moveToNext());

                    code.close();
                    citiesName.close();
                }
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
        new DataGet().execute(URL);

    }

    public ArrayMap<String, String> loadCountriesJSONFromAsset() {
        String json = null;
        ArrayMap<String, String> countriesName = null;
        try {
            InputStream is = this.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            /* Close Stream */
            if (null != is) {
                is.close();
            }


            JSONObject response = new JSONObject(result);
            JSONArray countriesArray = (JSONArray) response.get("countries");
            JSONObject w = countriesArray.getJSONObject(0);
            countriesName = new ArrayMap<>(countriesArray.length());

            for (int i = 0; i < countriesArray.length() - 1; i++) {
                JSONObject country = countriesArray.getJSONObject(i);
                countriesName.put(country.getString("code"), country.getString("name"));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countriesName;
    }

    /*public Map<Integer, String> loadCitiesJSONFromAsset() {
        String json = null;
        Map<Integer, String> citiesName = null;
        try {
            InputStream is = this.getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            *//* Close Stream *//*
            if (null != is) {
                is.close();
            }


            JSONObject response = new JSONObject(result);
            JSONArray citiesArray = (JSONArray) response.get("cities");
           // JSONObject w = citiesArray.getJSONObject(0);
            citiesName = new ArrayMap<>(citiesArray.length());

            for (int i = 0; i<citiesArray.length()-1; i++){
                JSONObject city = citiesArray.getJSONObject(i);
                citiesName.put(city.getInt("_id"),city.getString("name"));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return citiesName;
    }
        */
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

    private Map<Integer, String> parseCities() throws IOException {
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createJsonParser(this.getAssets().open("cities.json"));

        JsonToken current;

        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            Log.i("DB", "Error: root should be object: quiting.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            // move from field name to field value
            current = jp.nextToken();
            if (fieldName.equals("cities")) {
                if (current == JsonToken.START_ARRAY) {
                    // For each of the records in the array
                    while (jp.nextToken() != JsonToken.END_ARRAY) {

                        // read the record into a tree model,
                        // this moves the parsing position to the end of it
                        JsonNode node = jp.readValueAsTree();
                        // And now we have random access to everything in the object
                        Log.i("DB", "id: " + node.get("_id").asText() + node.get("name").asText() + node.get("country").asText());

                        this.citiesName.put(node.get("_id").asInt(), node.get("name").asText());
                        this.citiesCountry.put(node.get("_id").asInt(), node.get("country").asText());

                    }
                    Log.i("DB", "parcing complite");
                } else {
                    Log.e("DB", "Error: records should be an array: skipping.");
                    jp.skipChildren();
                }
            } else {
                Log.e("DB", "Unprocessed property: " + fieldName);
                jp.skipChildren();
            }
        }
        return citiesName;
    }

    public class DataGet extends AsyncTask<String, Void, String> {

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

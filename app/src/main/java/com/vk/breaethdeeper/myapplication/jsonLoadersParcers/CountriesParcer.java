package com.vk.breaethdeeper.myapplication.jsonLoadersParcers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

/**
 * Created by mixmax on 19.02.16.
 * was used to get countries from JSON and upload to SQL (for selecting cities using spinners)
 * deprecated because of being too slow
 */
public class CountriesParcer {


    public void loadCountriesToDB(SQLiteDatabase db, Context c, String assetName) {
        Map<String, String> countries = null;

        countries = loadCountriesJSONFromAsset(c, assetName);


        Set<String> keySet = countries.keySet();
        for (String key : keySet) {
            ContentValues values = new ContentValues();
            long retvalue = 0;

            values.put("name", countries.get(key));
            values.put("country_code", key);
            Log.i("DB", key + " - " + countries.get(key));
            retvalue = db.insertWithOnConflict("countries", null, values, SQLiteDatabase.CONFLICT_NONE);
        }

    }

    private ArrayMap<String, String> loadCountriesJSONFromAsset(Context c, String assetName) {
        String json = null;
        ArrayMap<String, String> countriesName = null;
        try {
            InputStream is = c.getAssets().open(assetName);
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

}

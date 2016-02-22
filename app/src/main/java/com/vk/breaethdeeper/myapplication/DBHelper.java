package com.vk.breaethdeeper.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.CitiesParser;
import com.vk.breaethdeeper.myapplication.JsonLoadersParcers.CountriesParcer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mixmax on 18.02.16.
 *  was used to get countries from JSON and upload to SQL (for selecting cities using spinners)
 * deprecated because of being too slow
 */
public class DBHelper extends SQLiteOpenHelper {

    Context c;
    CountriesParcer countryP;
    CitiesParser cityP;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 2);
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        countryP = new CountriesParcer();
        cityP = new CitiesParser();

        db.execSQL("create table countries (" +
                "name text primary key," +
                "country_code text" +
                ")");

        db.execSQL("create table cities (" +
                "id integer primary key," +
                "name text," +
                "country_code text"
                + ")");

        countryP.loadCountriesToDB(db, c, "countries.json");
        ArrayList<String> countryCodes = parseCountryCodes(db);
        try {
            cityP.loadCitiesToDB(db, c, "cities.json", countryCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String> parseCountries(SQLiteDatabase db) {
        Cursor c = db.query("countries", null, null, null, null, null, null);
        ArrayList<String> country_name_db = new ArrayList<>(c.getCount() + 1);
        country_name_db.add(0, "Select country");
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке

            int nameColIndex = c.getColumnIndex("name");
            int i = 1;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("DB", "name = " + c.getString(nameColIndex));
                country_name_db.add(i++, c.getString(nameColIndex));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("DB", "0 rows");
        c.close();
        return country_name_db;
    }

    public ArrayList<String> parseCountryCodes(SQLiteDatabase db) {
        Cursor c = db.query("countries", new String[]{"country_code"}, null, null, null, null, null);
        ArrayList<String> country_code_db = new ArrayList<>(c.getCount());

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = c.getColumnIndex("country_code");
            int i = 0;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("DB", "country_code = " + c.getString(nameColIndex));
                country_code_db.add(i++, c.getString(nameColIndex));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("DB", "0 rows");
        c.close();
        return country_code_db;
    }


    public String[] getCitiesByCountry(SQLiteDatabase db, String country) {
        Cursor c = null;


        Cursor code = db.query("countries", new String[]{"country_code"}, "name=?", new String[]{country}, null, null, null);
        int index = code.getColumnIndex("country_code");
        code.moveToFirst();
        String countryCode = code.getString(index);
        Cursor citiesName = db.query("cities", new String[]{"name"}, "country_code=?", new String[]{countryCode}, null, null, null);
        index = citiesName.getColumnIndex("name");
        citiesName.moveToFirst();
        String[] specialCities = new String[citiesName.getCount() + 1];
        specialCities[0] = "Select city";
        int i = 1;
        do {
            Log.i("DB", citiesName.getString(index));
            specialCities[i++] = citiesName.getString(index);

        } while (citiesName.moveToNext());

        code.close();
        citiesName.close();
        return specialCities;
    }

    public Integer getCityId(SQLiteDatabase db, String cityName) {
        Cursor c = null;

        Cursor cursor = db.query("cities", new String[]{"id"}, "name=?", new String[]{cityName}, null, null, null);
        int index = cursor.getColumnIndex("id");
        cursor.moveToFirst();
        Integer cityId = cursor.getInt(index);

        cursor.close();
        return cityId;
    }
}

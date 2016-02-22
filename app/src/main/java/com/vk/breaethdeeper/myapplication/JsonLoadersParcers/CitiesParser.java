package com.vk.breaethdeeper.myapplication.JsonLoadersParcers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by mixmax on 19.02.16.
 * was used to get cities from JSON and upload to SQL  (for selecting cities using spinners)
 * deprecated because of being too slow
 */
public class CitiesParser {
    private Map<Integer, String> citiesName = new ArrayMap<>(209600);
    private Map<Integer, String> citiesCountriesCode = new ArrayMap<>(209600);


    public void loadCitiesToDB(SQLiteDatabase db, Context c, String assetName, ArrayList<String> countryCodes) throws IOException {
        Map<Integer, String> cities = null;

        cities = loadCitiesJSONFromAsset(c, assetName);


        Set<Integer> keySet = cities.keySet();

        for (Integer key : keySet) {
            if (countryCodes.contains(citiesCountriesCode.get(key))) {

                ContentValues values = new ContentValues();
                long retvalue = 0;
                values.put("id", key);
                values.put("name", cities.get(key));
                values.put("country_code", citiesCountriesCode.get(key));
                Log.i("DB", key + " - " + cities.get(key));
                retvalue = db.insertWithOnConflict("cities", null, values, SQLiteDatabase.CONFLICT_NONE);
            }

        }
    }


    private String[] parseCities(SQLiteDatabase db) {
        Cursor c = db.query("cities", null, null, null, null, null, null);
        String[] cities_name_db = new String[(c.getCount() + 1)];
        cities_name_db[0] = "Choose city";
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке

            int nameColIndex = c.getColumnIndex("name");
            int i = 1;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("DB", "name = " + c.getString(nameColIndex));
                cities_name_db[i] = c.getString(nameColIndex);
                i++;
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("DB", "0 rows");
        c.close();
        return cities_name_db;
    }

    private Map<Integer, String> loadCitiesJSONFromAsset(Context c, String assetName) throws IOException {
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createJsonParser(c.getAssets().open(assetName));

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
                        this.citiesCountriesCode.put(node.get("_id").asInt(), node.get("country").asText());

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
}

package com.vk.breaethdeeper.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.breaethdeeper.myapplication.App;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.models.Weather;

import java.util.ArrayList;

/**
 * Created by mixmax on 04.03.16.
 */
public class ForecastAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Weather> objects;

    public ForecastAdapter(Context context, ArrayList<Weather> objects) {
        ctx = context;
        this.objects = objects;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static Drawable getDrawable(String name) {
        Context context = App.getContext();
        int resourceId;
        name = "i" + name;
        try {
            resourceId = context.getResources().getIdentifier(name, "drawable", App.getContext().getPackageName());
            if (resourceId == 0)
                resourceId = context.getResources().getIdentifier("i00dn", "drawable", App.getContext().getPackageName());
        } catch (Exception e) {
            System.out.println(e);
            resourceId = context.getResources().getIdentifier("i00dn", "drawable", App.getContext().getPackageName());
        }

        return context.getResources().getDrawable(resourceId);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Weather getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.rowlayout, parent, false);
        }

        Weather w = getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.label_main_info)).setText(w.getTemp() + "C " + w.getDescription());
        ((TextView) view.findViewById(R.id.label_date)).setText(w.getDateTime() + " ");
        ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getDrawable(w.getIcon()));


        return view;
    }


}

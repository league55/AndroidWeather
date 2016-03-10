package com.vk.breaethdeeper.myapplication.fragments.weather;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vk.breaethdeeper.myapplication.App;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.adapter.ForecastAdapter;
import com.vk.breaethdeeper.myapplication.models.Forecast;
import com.vk.breaethdeeper.myapplication.models.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixmax on 04.03.16.
 */
public class ForecastFrag extends Fragment {

    Activity activity;
    ListView list;
    Context context;
    ForecastAdapter forecastAdapter;
    ArrayList<Weather> fiveWeathers;

    public static ForecastFrag newInstance(ArrayList<Weather> list) {
        ForecastFrag forecastFrag = new ForecastFrag();
        Bundle args = new Bundle();
        args.putParcelableArrayList("FiveWeathers", list);
        forecastFrag.setArguments(args);
        return forecastFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = App.getContext();
        fiveWeathers = new ArrayList<Weather>();

        for (Parcelable p : getArguments().getParcelableArrayList("FiveWeathers")) {

            fiveWeathers.add((Weather) p);
        }


        forecastAdapter = new ForecastAdapter(context, fiveWeathers);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.forecast_frag, container, false);
        context = App.getContext();
        this.list = (ListView) view.findViewById(R.id.listView);
        //  ArrayAdapter<String> for_adapter = new ArrayAdapter<String>(context, R.layout.rowlayout, R.id.label_main_info, f);
        list.setAdapter(forecastAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(App.getContext(), MainActivity.class);
                //startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = getActivity();
    }

    public boolean updateUI(Forecast forcast) {
        List<String> main = forcast.getFiveDayWeatherStr();
        list.setAdapter(new ArrayAdapter<String>(context, R.layout.rowlayout, R.id.label_main_info, main));
        return true;
    }

}

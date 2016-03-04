package com.vk.breaethdeeper.myapplication.fragments.weather;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vk.breaethdeeper.myapplication.App;
import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.models.Forecast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixmax on 04.03.16.
 */
public class ForecastFrag extends Fragment {

    Activity activity;
    ListView list;
    Context context;

    ArrayList<String> titlesList;

    public static ForecastFrag newInstance(ArrayList<String> list) {
        ForecastFrag forecastFrag = new ForecastFrag();
        Bundle args = new Bundle();
        args.putStringArrayList("titlesList", list);
        forecastFrag.setArguments(args);
        return forecastFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titlesList = getArguments().getStringArrayList("titlesList");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.forecast_frag, container, false);
        context = App.getContext();
        this.list = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> for_adapter = new ArrayAdapter<String>(context, R.layout.rowlayout, R.id.label, titlesList);
        list.setAdapter(for_adapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = getActivity();
    }

    public boolean updateUI(Forecast forcast) {
        List<String> main = forcast.getFiveDayWeatherStr();
        list.setAdapter(new ArrayAdapter<String>(context, R.layout.rowlayout, R.id.label, main));
        return true;
    }

}

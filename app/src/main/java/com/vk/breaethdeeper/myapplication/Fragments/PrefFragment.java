package com.vk.breaethdeeper.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vk.breaethdeeper.myapplication.R;
import com.vk.breaethdeeper.myapplication.activities.MainActivity;

/**
 * Created by mixmax on 23.02.16.
 */
public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.pref_main);

        Context context = null;
        if (isAdded()) {
            context = getActivity();
            PreferenceManager.setDefaultValues(context, R.xml.pref_main, false);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        }


        initSummary(getPreferenceScreen());
        getPreferenceScreen().setOnPreferenceClickListener(this);
    }


    private void initSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry() + "1");
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getKey().toString().toLowerCase().contains("city")) {
                p.setSummary(editTextPref.getKey() + "2");
                ((EditTextPreference) p).setText(editTextPref.getText() + "3");
            } else {
                p.setSummary(editTextPref.getText() + "4");
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i("PREF", "Click" + preference.getTitle());
        switch (preference.getTitleRes()) {
            case R.string.save:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }

        return false;
    }
}


package com.vk.breaethdeeper.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by mixmax on 04.03.16.
 */
public class App extends Application {


    private static Context mContext;

    public static Context getContext() {

        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}

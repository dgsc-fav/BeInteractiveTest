package com.github.dgsc_fav.beinteractivetest;

import android.app.Application;
import android.util.Log;

/**
 * Created by DG on 23.10.2016.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("App", "onCreate");
    }
}

package com.nilanshi.nigam.personalassistant.util;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by HP on 06-Sep-17.
 */

public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}

package com.kooapps.mininumbergame.application;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kooapps.mininumbergame.handler.GameHandler;

public class GameApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GameHandler.getInstance().initializeGridGame();
        MobileAds.initialize(this, new OnInitializationCompleteListener(){
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
    }
}

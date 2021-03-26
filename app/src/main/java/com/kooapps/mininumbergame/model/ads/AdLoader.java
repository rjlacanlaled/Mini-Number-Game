package com.kooapps.mininumbergame.model.ads;

import android.app.Activity;
import android.content.Context;


public interface AdLoader {

    void loadNewAd(Activity activity);
    void setupAd();
    void showAd(Activity activity);
}

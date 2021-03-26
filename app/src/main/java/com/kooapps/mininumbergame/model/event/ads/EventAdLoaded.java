package com.kooapps.mininumbergame.model.event.ads;

import androidx.annotation.NonNull;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.event.main.Event;

public class EventAdLoaded extends Event {


    @NonNull
    @Override
    public int getId() {
        return R.string.event_ad_loaded;
    }

}

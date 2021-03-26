package com.kooapps.mininumbergame.model.event.ads;

import androidx.annotation.NonNull;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.event.main.Event;

public class EventRewardedAdLoaded extends Event {

    @NonNull
    @Override
    public int getId() {
        return R.string.event_rewarded_ad_loaded;
    }
}

package com.kooapps.mininumbergame.model.event.ads;

import androidx.annotation.NonNull;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.ads.AdLoader;
import com.kooapps.mininumbergame.model.ads.RewardedAdmobLoader;
import com.kooapps.mininumbergame.model.event.main.Event;

public class EventWatchRewardedAd extends Event {

    private AdLoader mAdLoader = new RewardedAdmobLoader();

    public AdLoader getAdLoader() {
        return mAdLoader;
    }

    @NonNull
    @Override
    public int getId() {
        return R.string.event_watch_rewarded_ad;
    }
}

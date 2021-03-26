package com.kooapps.mininumbergame.model.event.setting;

import androidx.annotation.NonNull;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.event.main.Event;

public class EventSettingsChanged extends Event {

    @NonNull
    @Override
    public int getId() {
        return R.string.event_settings_changed;
    }
}

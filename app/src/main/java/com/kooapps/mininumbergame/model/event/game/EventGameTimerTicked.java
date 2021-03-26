package com.kooapps.mininumbergame.model.event.game;

import androidx.annotation.NonNull;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.event.main.Event;

public class EventGameTimerTicked extends Event {


    @NonNull
    @Override
    public int getId() {
        return R.string.event_game_timer_ticked;
    }
}

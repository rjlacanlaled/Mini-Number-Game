package com.kooapps.mininumbergame.model.event.main;

import androidx.annotation.NonNull;

public interface   EventListener<E extends Event> {
    void onEvent(@NonNull E event);
}

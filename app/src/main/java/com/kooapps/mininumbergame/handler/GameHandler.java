package com.kooapps.mininumbergame.handler;

import com.kooapps.mininumbergame.model.helper.GridGameSettingsUtil;
import com.kooapps.mininumbergame.model.helper.GridGameUtil;
import com.kooapps.mininumbergame.model.main.GridGame;
import com.kooapps.mininumbergame.model.main.GridGameSettings;

public class GameHandler {

    // GAME INSTANCE

    private static final GameHandler mGameHandler = new GameHandler();
    private static boolean isInitialized;

    // GAME OBJECTS

    private GridGameSettings mGridGameSettings;
    private GridGame mGridGame;

    // GETTERS

    public static GameHandler getInstance() {
        return mGameHandler;
    }

    public GridGameSettings getGridGameSettings() {
        return mGridGameSettings;
    }

    public GridGame getGridGame() {
        return mGridGame;
    }

    // INITIALIZATION

    public void initializeGridGame() {
        if (isInitialized) {
            return;
        }
        mGridGameSettings = GridGameSettingsUtil.createNewSettings();
        mGridGame = GridGameUtil.createNewGridGame(mGridGameSettings);
        isInitialized = true;
    }
}

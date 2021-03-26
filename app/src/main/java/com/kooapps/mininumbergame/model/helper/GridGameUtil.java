package com.kooapps.mininumbergame.model.helper;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.main.Grid;
import com.kooapps.mininumbergame.model.main.GridGame;
import com.kooapps.mininumbergame.model.main.GridGameSettings;
import com.kooapps.mininumbergame.model.main.NumberGridGame;

public class GridGameUtil {


    public static String getTextForScoreTextView(Context context, int score) {
        return String.format(context.getResources().getString(R.string.score_text), score);
    }

    public static int getGameStatusVisibility(boolean isVisible) {
        return isVisible ? View.VISIBLE : View.INVISIBLE;
    }

    public static String getTextForStartGameButton(Context context, boolean isRunning) {
        return isRunning ? context.getResources().getString(R.string.game_stop_text) : context.getResources().getString(R.string.game_start_button_text);
    }

    public static GridGame createNewGridGame(GridGameSettings gridGameSettings) {
        return new NumberGridGame(
                new Grid(gridGameSettings.getRowCount(), gridGameSettings.getColumnCount()),
                gridGameSettings.getBaseScore(),
                gridGameSettings.getGameMode(),
                gridGameSettings.getTimeLimit(),
                gridGameSettings.isTimed());
    }

    public static float getFloatTime(long milisTime) {
        return milisTime / 1000.0f;
    }

    public static String textForRemainingLives(Context context, int lives) {
        StringBuilder livesString = new StringBuilder();

        if (lives < 1) {
            return context.getString(R.string.no_lives_text);
        } else {
            for (int i = 0; i < lives; i++) {
                livesString.append(context.getString(R.string.life_symbol));
            }
        }
        return livesString.toString();
    }

}

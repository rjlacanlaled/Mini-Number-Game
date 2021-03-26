package com.kooapps.mininumbergame.model.helper;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.main.GridGameSettings;

public class GridGameSettingsUtil {

    public static String textForRow(Context context, int rowCount) {
        return String.format(context.getResources().getString(R.string.settings_row_ui_text), rowCount);
    }

    public static String textForColumn(Context context, int columnCount) {
        return String.format(context.getResources().getString(R.string.settings_column_ui_text), columnCount);
    }

    public static String textForTimeLimit(Context context, long timeLimit) {
        return String.format(context.getResources().getString(R.string.time_limit_settings_text), timeLimit / 1000);
    }

    public static String textForBaseScore(Context context, int baseScore) {
        return String.format(context.getResources().getString(R.string.base_score_settings_text), baseScore);
    }

    public static int getSecondsForTimeLimit(long timeLimit) {
        return (int)timeLimit / 1000;
    }

    public static GridGameSettings createNewSettings() {
        return new GridGameSettings();
    }
}

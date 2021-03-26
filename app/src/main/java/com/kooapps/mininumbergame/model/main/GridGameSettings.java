package com.kooapps.mininumbergame.model.main;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.kooapps.mininumbergame.BR;
import com.kooapps.mininumbergame.model.helper.GridGameMode;
import com.kooapps.mininumbergame.model.event.main.EagerEventDispatcher;
import com.kooapps.mininumbergame.model.event.setting.EventSettingsChanged;

public class GridGameSettings extends BaseObservable {

    //region DEFAULT_VALUES

    private static int DEFAULT_ROW = 3;
    private static int DEFAULT_COLUMN = 3;
    private static boolean DEFAULT_TIME_MODE = true;
    public static int MAX_ROW = 7;
    public static int MAX_COLUMN = 7;
    public static int MIN_ROW = 3;
    public static int MIN_COLUMN = 3;
    public static long DEFAULT_TIME_LIMIT = 20000;
    public static int DEFAULT_BASE_SCORE = 10;
    public static long MAX_TIME_LIMIT = 100000;
    public static long MIN_TIME_LIMIT = 10000;
    public static int MAX_BASE_SCORE = 100;
    public static int MIN_BASE_SCORE = 10;
    public static boolean DEFAULT_GAME_MODE = true;

    //endregion DEFAULT_VALUES

    //region PROPERTIES

    private int mRowCount;
    private int mColumnCount;
    private int mBaseScore;
    private long mTimeLimit;
    private boolean mGridGameMode;
    private boolean mIsTimed;
    private boolean mIsActive;

    //endregion PROPERTIES

    //region CONSTRUCTORS

    public GridGameSettings() {
        mRowCount = DEFAULT_ROW;
        mColumnCount = DEFAULT_COLUMN;
        mIsTimed = DEFAULT_TIME_MODE;
        mTimeLimit = DEFAULT_TIME_LIMIT;
        mBaseScore = DEFAULT_BASE_SCORE;
        mGridGameMode = DEFAULT_GAME_MODE;
    }

    //endregion CONSTRUCTORS

    //region GETTERS

    public int getRowCount() {
        return mRowCount;
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public boolean isTimed() {
        return mIsTimed;
    }

    public long getTimeLimit() {
        return mTimeLimit;
    }

    public int getBaseScore() {
        return mBaseScore;
    }

    public boolean isGridGameMode() {
        return mGridGameMode;
    }

    public GridGameMode getGameMode() {
        if (isGridGameMode()) {
            return GridGameMode.GridGameModeAscending;
        } else {
            return GridGameMode.GridGameModeDescending;
        }
    }

    @Bindable
    public boolean isActive() {
        return mIsActive;
    }

    //endregion GETTERS

    //region SETTERS

    public void setGridGameMode(boolean gridGameMode) {
        if (mGridGameMode != gridGameMode) {
            mGridGameMode = gridGameMode;
            notifySettingsChangedListeners();
        }
    }

    public void setBaseScore(int baseScore) {
        if (mBaseScore != baseScore) {
            mBaseScore = baseScore;
            notifySettingsChangedListeners();
        }
    }

    public void setTimeLimit(long timeLimit) {
        if (mTimeLimit != timeLimit) {
            mTimeLimit = timeLimit;
            notifySettingsChangedListeners();
        }

    }

    public void setIsTimed(boolean isTimed) {
        if (mIsTimed != isTimed) {
            mIsTimed = isTimed;
            notifySettingsChangedListeners();
        }
    }

    public void setColumnCount(int columnCount) {
        if (mColumnCount != columnCount) {
            mColumnCount = columnCount;
            notifySettingsChangedListeners();
        }
    }

    public void setRowCount(int rowCount) {
        if (mRowCount != rowCount) {
            mRowCount = rowCount;
            notifySettingsChangedListeners();
        }
    }

    public void setIsActive(boolean isActive) {
        if (mIsActive != isActive) {
            mIsActive = isActive;
            notifyPropertyChanged(BR.active);
        }
    }

    //endregion SETTERS

    //region HELPERS

    public void updateGridGameSettings(int tempRowCount, int tempColumnCount, long tempTimeLimit, int tempBaseScore,
                                        boolean tempGridGameMode, boolean tempIsTimed) {
        setRowCount(tempRowCount);
        setColumnCount(tempColumnCount);
        setTimeLimit(tempTimeLimit);
        setBaseScore(tempBaseScore);
        setGridGameMode(tempGridGameMode);
        setIsTimed(tempIsTimed);
    }

    //endregion HELPERS

    //region NOTIFICATION

    public void notifySettingsChangedListeners() {
        EagerEventDispatcher.dispatch(new EventSettingsChanged());
    }

    //endregion NOTIFICATION
}

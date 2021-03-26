package com.kooapps.mininumbergame.model.main;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.model.helper.GridGameMode;
import com.kooapps.mininumbergame.model.event.main.EagerEventDispatcher;
import com.kooapps.mininumbergame.model.event.game.EventGameStatusChanged;
import com.kooapps.mininumbergame.model.event.game.EventGameTimerFinished;
import com.kooapps.mininumbergame.model.event.game.EventGameTimerTicked;
import com.kooapps.mininumbergame.model.event.game.EventTappedCell;

import java.util.ArrayList;

public abstract class GridGame extends BaseObservable {

    //region CONSTANTS

    public static int MAX_SCORE = 50000;
    private static int BASE_SCORE_MULTIPLIER = 10;
    private static int DEFAULT_LIFE = 3;
    private static int QUIT_GAME_PENALTY = 2;
    private static int COMPLETE_GAME_REWARD = 1;
    private static int MAX_LIFE = 6;

    //endregion CONSTANTS

    //region PROPERTIES

    private boolean mIsGameOver;
    private boolean mIsTimed;
    private boolean mIsRunning;
    private boolean mIsActive;
    private boolean mGameStatusVisible;
    private boolean mIsAdRunning;

    private int mCellToTap;
    private int mBaseScore;
    private int mCurrentScore;
    private int mSize;
    private int mCurrentBonusMultiplier;
    private int mLife;
    private long mTimeLimit;
    private long mTimeRemaining;

    private Grid mGrid;
    private ArrayList<String> mOriginalCellContents;
    private ArrayList<String> mShuffledCellContents;
    private ArrayList<Integer> mSelectedContents;
    private GridGameMode mGameMode;
    private String mGameStatusText;
    private CountDownTimer mGameTimer;

    //endregion PROPERTIES

    //region ABSTRACT_METHODS

    public abstract void generateCellContents();
    public abstract void randomizeCellContents();

    //endregion ABSTRACT_METHODS

    //region CONSTRUCTORS

    public GridGame(){}

    public GridGame(Grid grid, int baseScore, GridGameMode gameMode, long timeLimitInMiliseconds, boolean isTimed) {
        mGrid = grid;
        mSize = grid.getRowCount() * grid.getColumnCount();
        mBaseScore = baseScore;
        mGameMode = gameMode;
        mTimeLimit = timeLimitInMiliseconds;
        mTimeRemaining = timeLimitInMiliseconds;
        mIsTimed = isTimed;
        mCurrentBonusMultiplier = isTimed ? (int) mTimeRemaining / 1000 : BASE_SCORE_MULTIPLIER;
        mIsRunning = false;
        mLife = DEFAULT_LIFE;
        generateCellContents();
        randomizeCellContents();
    }

    //endregion CONSTRUCTORS

    //region GETTERS

    public Grid getGrid() {
        return mGrid;
    }

    public ArrayList<String> getOriginalCellContents() {
        return mOriginalCellContents;
    }


    public ArrayList<String> getShuffledCellContents() {
        return mShuffledCellContents;
    }


    public int getBaseScore() {
        return mBaseScore;
    }

    public int getSize() {
        return mSize;
    }

    public long getTimeLimit() {
        return mTimeLimit;
    }

    public GridGameMode getGameMode() {
        return mGameMode;
    }


    public int getCellToTap() {
        return mCellToTap;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public boolean isTimed() {
        return mIsTimed;
    }

    @Bindable
    public String getGameStatusText() {
        return mGameStatusText;
    }

    @Bindable
    public boolean getGameStatusVisible() {
        return mGameStatusVisible;
    }


    @Bindable
    public int getCurrentScore() {
        return mCurrentScore;
    }

    public boolean isGameOver() {
        return mIsGameOver;
    }

    public long getTimeRemaining() {
        return mTimeRemaining;
    }

    public int getCurrentBonusMultiplier() {
        return mIsTimed ? mCurrentBonusMultiplier : BASE_SCORE_MULTIPLIER;
    }

    @Bindable
    public boolean isRunning() {
        return mIsRunning;
    }

    @Bindable
    public int getLife() {
        return mLife;
    }

    @Bindable
    public boolean isAdRunning() {
        return mIsAdRunning;
    }

    //endregion GETTERS

    //region SETTERS

    public void setIsRunning(boolean isRunning) {
        if (isRunning != mIsRunning) {
            mIsRunning = isRunning;
            notifyGameStatusChangedListeners();
            notifyPropertyChanged(BR.running);
        }
    }

    public void setGrid(Grid mGrid) {
        this.mGrid = mGrid;
    }

    public void setGameMode(GridGameMode mGameMode) {
        this.mGameMode = mGameMode;
    }

    public void setBaseScore(int mBaseScore) {
        this.mBaseScore = mBaseScore;
    }

    public void setTimeLimit(long mTimeLimit) {
        this.mTimeLimit = mTimeLimit;
    }

    public void setIsActive(boolean isActive) {
        mIsActive = isActive;
    }

    public void setIsTimed(boolean mIsTimed) {
        this.mIsTimed = mIsTimed;
    }

    public void setGameStatusVisible(boolean gameStatusVisible) {
        if (mGameStatusVisible != gameStatusVisible) {
            mGameStatusVisible = gameStatusVisible;
            notifyPropertyChanged(BR.gameStatusVisible);
        }
    }
    public void setGameStatusText(String gameStatusText) {
        mGameStatusText = gameStatusText;
        notifyPropertyChanged(BR.gameStatusText);
    }
    public void setCurrentScore(int currentScore) {
        mCurrentScore = currentScore;
        if (mCurrentScore >= MAX_SCORE) {
            mCurrentScore = MAX_SCORE;
        }
        notifyPropertyChanged(BR.currentScore);
    }
    public void setCellToTap(int cellToTap) {
        mCellToTap = cellToTap;
    }
    public void setShuffledCellContents(ArrayList<String> shuffledCellContents) {
        mShuffledCellContents = shuffledCellContents;
    }
    public void setOriginalCellContents(ArrayList<String> originalCellContents) {
        mOriginalCellContents = originalCellContents;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public void setCurrentBonusMultiplier(int currentBonusMultiplier) {
        mCurrentBonusMultiplier = currentBonusMultiplier;
    }

    public void setIsGameOver(boolean mIsGameOver) {
        this.mIsGameOver = mIsGameOver;
    }

    public void setTimeRemaining(long mTimeRemaining) {
        this.mTimeRemaining = mTimeRemaining;
    }

    public void setLife(int mLife) {
        if (mLife <= MAX_LIFE) {
            this.mLife = mLife;
        }
        notifyPropertyChanged(BR.life);
    }

    public void setIsAdRunning(boolean mIsAdRunning) {
        this.mIsAdRunning = mIsAdRunning;
        notifyPropertyChanged(BR.adRunning);
    }

    //endregion SETTERS

    //region GAME_HELPERS

    public void updateCurrentBonusMultiplier() {
        this.mCurrentBonusMultiplier = mIsTimed ? (int) mTimeRemaining / 1000 : BASE_SCORE_MULTIPLIER;
    }

    public int getCurrentBonusPoints() {
        return mCurrentBonusMultiplier * mBaseScore;
    }

    public void resetGame() {
        setCurrentScore(0);
        setTimeRemaining(mTimeLimit);
        setIsGameOver(false);
        setCellToTap(0);
        clearSelectedContents();
    }

    public void generateNewGrid() {
        this.randomizeCellContents();
        clearSelectedContents();
        addTime(mTimeLimit);
        this.mCellToTap = 0;
    }

    public boolean didPressLastButtonOnTime() {
        if (mIsTimed) {
            if (mTimeRemaining <= 0) {
                return false;
            } else {
                return mCellToTap >= this.getOriginalCellContents().size();
            }
        }
        return false;
    }

    private void addTime(long timeToAdd) {
        this.mTimeRemaining += timeToAdd;
    }

    public void updateCellToTap() {
        this.mCellToTap++;
    }

    public void updateScore() {
        this.setCurrentScore(this.mCurrentScore + this.mBaseScore);
    }

    public void updateScore(int bonusMultiplier) {
        this.setCurrentScore(this.mCurrentScore + (this.mBaseScore * bonusMultiplier));
    }

    public boolean didTapCorrectCell(String cellValue) {
        if (this.getOriginalCellContents().get(mCellToTap).equals(cellValue)) {
            return true;
        }
        this.mIsGameOver = true;
        return false;
    }

    public ArrayList<Integer> getSelectedContents() {
        return mSelectedContents;
    }

    public void compareChosenValue(int tag) {
        if (!isGameOver()) {
            onCompareChosenValue(tag);
        }
        notifyCompareTappedCellListeners();
    }

    public void onCompareChosenValue(int tag) {
        if (didTapCorrectCell(mShuffledCellContents.get(tag))) {
            onTappedCorrectCell();
        } else {
            onGameOver();
        }
        addSelectedContents(tag);
    }

    public void onTappedCorrectCell() {
        updateScore();
        updateCellToTap();
    }

    public void onGameOver() {
        setLife(mLife - 1);
    }

    public void notifyCompareTappedCellListeners() {
        EagerEventDispatcher.dispatch(new EventTappedCell());
    }

    public void notifyGameStatusChangedListeners() {
        EagerEventDispatcher.dispatch(new EventGameStatusChanged());
    }

    public void addSelectedContents(int index) {
        if (mSelectedContents == null) {
            mSelectedContents = new ArrayList<>();
        }

        mSelectedContents.add(index);
    }

    public void clearSelectedContents() {
        if (mSelectedContents != null) {
            mSelectedContents.clear();
        }
    }

    public boolean hasEnoughLife() {
        return mLife > 0;
    }

    public void restoreLife() {
        setLife(mLife + DEFAULT_LIFE);
    }

    public void updateGridGameSettings(GridGameSettings gridGameSettings) {
        setGrid(new Grid(gridGameSettings.getRowCount(), gridGameSettings.getColumnCount()));
        setBaseScore(gridGameSettings.getBaseScore());
        setGameMode(gridGameSettings.getGameMode());
        setTimeLimit(gridGameSettings.getTimeLimit());
        setIsTimed(gridGameSettings.isTimed());
        setTimeRemaining(gridGameSettings.getTimeLimit());
        generateCellContents();
        randomizeCellContents();
    }

    public void deductQuitGamePenalty() {
        if (mLife - QUIT_GAME_PENALTY < 0) {
            setLife(0);
        } else {
            setLife(mLife - QUIT_GAME_PENALTY);
        }
    }

    public void addCompletedReward() {
        setLife(mLife + COMPLETE_GAME_REWARD);
    }

    public void updateLifeGameStatus(Context context) {
        if (hasEnoughLife()) {
            setGameStatusText(context.getString(R.string.ad_reward_success_text));
        } else {
            setGameStatusText(context.getString(R.string.ad_reward_failed_text));
        }
    }

    //endregion GAME_HELPERS

    //region GAME_ENTRY_EXIT_METHODS

    public void startGame() {
        setIsRunning(true);
        startGameTimer();
    }

    public void stopGame() {
        setIsRunning(false);
        stopGameTimer();
    }

    //endregion GAME_ENTRY_EXIT_METHODS

    //region GAME_TIMER

    public void initGameTimer() {
        stopGameTimer(); // if has another timer running
        createNewGameTimer();
        setGameStatusVisible(isTimed());
    }

    public void startGameTimer() {
        if (isTimed()) {
            initGameTimer();
            mGameTimer.start();
        }
    }

    public void stopGameTimer() {
        if (mGameTimer != null) {
            mGameTimer.cancel();
            mGameTimer = null;
        }
    }

    public void pauseGameTimer() {
        if (isRunning() && isTimed()) {
            stopGameTimer();
        }
    }

    public void resumeGameTimer() {
        if (isRunning() && isTimed()) {
            startGameTimer();
        }
    }

    public void createNewGameTimer() {
        mGameTimer = new CountDownTimer(mTimeRemaining, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeRemaining = millisUntilFinished;
                EagerEventDispatcher.dispatch(new EventGameTimerTicked());
            }

            @Override
            public void onFinish() {
                setIsGameOver(true);
                EagerEventDispatcher.dispatch(new EventGameTimerFinished());
            }
        };
    }
    //endregion GAME_TIMER
}

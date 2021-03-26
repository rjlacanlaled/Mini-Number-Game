package com.kooapps.mininumbergame.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.databinding.ActivityMainBinding;
import com.kooapps.mininumbergame.fragment.CorrectionFragment;
import com.kooapps.mininumbergame.fragment.GameFragment;
import com.kooapps.mininumbergame.fragment.SettingsFragment;
import com.kooapps.mininumbergame.fragment.WatchAdDialogFragment;
import com.kooapps.mininumbergame.handler.AdHandler;
import com.kooapps.mininumbergame.handler.GameHandler;
import com.kooapps.mininumbergame.model.ads.RewardedAdmobLoader;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdDismissed;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdFailed;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdLoaded;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdRewardEarned;
import com.kooapps.mininumbergame.model.event.ads.EventWatchRewardedAd;
import com.kooapps.mininumbergame.model.helper.GridGameSettingsUtil;
import com.kooapps.mininumbergame.model.helper.GridGameUtil;
import com.kooapps.mininumbergame.model.event.main.EagerEventDispatcher;
import com.kooapps.mininumbergame.model.event.game.EventGameStatusChanged;
import com.kooapps.mininumbergame.model.event.game.EventGameTimerFinished;
import com.kooapps.mininumbergame.model.event.game.EventGameTimerTicked;
import com.kooapps.mininumbergame.model.event.main.EventListener;
import com.kooapps.mininumbergame.model.event.setting.EventSettingsChanged;
import com.kooapps.mininumbergame.model.event.game.EventTappedCell;
import com.kooapps.mininumbergame.model.helper.NetworkUtil;
import com.kooapps.mininumbergame.model.main.GridGame;
import com.kooapps.mininumbergame.model.main.GridGameSettings;

public class MainActivity extends AppCompatActivity  {

    //region DATA_AND_BINDING

    ActivityMainBinding mActivityMainBinding;
    private Toast mStatusToast;
    private GridGame mGridGame;
    private GridGameSettings mGridGameSettings;
    private RewardedAdmobLoader mRewardedAdmobLoader;

    //endregion DATA_AND_BINDING

    //region FRAGMENTS

    private GameFragment mGameFragment;
    private SettingsFragment mSettingsFragment;
    private CorrectionFragment mCorrectionFragment;

    //endregion FRAGMENTS

    //region EVENTS

    EventListener<EventGameStatusChanged> mEventGameStatusChangedEventListener = event -> {
        if (mGridGame.isRunning()) {
            displayToast(getString(R.string.game_started_text));
        }
    };

    EventListener<EventSettingsChanged> mEventSettingsChangedEventListener = event ->
            displayToast(getString(R.string.settings_saved_successfully_text));

    EventListener<EventTappedCell> mEventTappedCellEventListener = event -> {
        if (mGridGame.isGameOver()) {
            onWrongCellTapped();
        } else {
            onLastCellTapped();
        }
    };

    EventListener<EventGameTimerTicked> mEventGameTimerTickedEventListener = event ->
            updateTimeRemainingText(GridGameUtil.getFloatTime(mGridGame.getTimeRemaining()));

    EventListener<EventGameTimerFinished> mEventGameTimerFinishedEventListener = event -> {
        mGridGame.setGameStatusText(getResources().getString(R.string.game_times_up_text));
        stopGame();
    };

    EventListener<EventWatchRewardedAd> mEventWatchRewardedAdEventListener = event -> onRewardedAdRequested((RewardedAdmobLoader)event.getAdLoader());

    EventListener<EventRewardedAdRewardEarned> mEventRewardedAdRewardEarned = event -> onRewardEarned();

    EventListener<EventRewardedAdDismissed> mEventRewardedAdDismissed = event -> onAdFinished();

    EventListener<EventRewardedAdFailed> mEventRewardedAdFailed = event -> onRewardedAdFailedToLoad();

    EventListener<EventRewardedAdLoaded> mEventRewardedAdLoaded = event -> onRewardedAdLoadSuccess();

    //endregion EVENTS

    //region ACTIVITY_LIFE_CYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGridGame.setIsActive(false);
        mGridGame.pauseGameTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGridGameSettings.isActive() && !mGridGame.isActive()) {
            mGridGame.resumeGameTimer();
        }
        mGridGame.setIsActive(true);
        executePendingAds();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resurfaceGameFragmentWhenDestroyedWhileRunning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeListeners();
    }

    //endregion ACTIVITY_LIFE_CYCLE

    //region INITIALIZATION

    public void init() {
        initData();
        initFragments();
        initListeners();
        initUIBinding();
    }

    public void initData() {
        mGridGame = GameHandler.getInstance().getGridGame();
        mGridGameSettings = GameHandler.getInstance().getGridGameSettings();
        mRewardedAdmobLoader = AdHandler.getInstance().getCurrentRewardedAdmobLoader();
    }

    public void initFragments() {

        mGameFragment = (GameFragment)getSupportFragmentManager().findFragmentByTag(GameFragment.TAG);
        mSettingsFragment = (SettingsFragment)getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
        mCorrectionFragment = (CorrectionFragment)getSupportFragmentManager().findFragmentByTag(CorrectionFragment.TAG);

        if (mGameFragment == null) {
            mGameFragment = new GameFragment();
        }

        if (mSettingsFragment == null) {
            mSettingsFragment = new SettingsFragment();
        }

        if (mCorrectionFragment == null) {
            mCorrectionFragment = new CorrectionFragment();
        }
    }


    public void initListeners() {
        initSettingsListener();
        initGameChangeListener();
        initGameTimerListeners();
        initAdListeners();
    }

    public void initUIBinding() {
        mActivityMainBinding.setGridGame(mGridGame);
        mActivityMainBinding.setGridGameSettings(GameHandler.getInstance().getGridGameSettings());
    }

    public void initAdListeners() {
        EagerEventDispatcher.addListener(R.string.event_watch_rewarded_ad, mEventWatchRewardedAdEventListener);
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_reward_earned, mEventRewardedAdRewardEarned);
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_dismissed, mEventRewardedAdDismissed);
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_failed, mEventRewardedAdFailed);
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_loaded, mEventRewardedAdLoaded);
    }

    public void initSettingsListener() {
        EagerEventDispatcher.addListener(R.string.event_settings_changed, mEventSettingsChangedEventListener);
    }
    public void initGameChangeListener() {
        EagerEventDispatcher.addListener(R.string.event_game_status_changed, mEventGameStatusChangedEventListener);
        EagerEventDispatcher.addListener(R.string.event_compare_tapped_cell, mEventTappedCellEventListener);
    }

    public void initGameTimerListeners() {
        EagerEventDispatcher.addListener(R.string.event_game_timer_ticked, mEventGameTimerTickedEventListener);
        EagerEventDispatcher.addListener(R.string.event_game_timer_finished, mEventGameTimerFinishedEventListener);
    }

    //endregion INITIALIZATION

    //region GRID_GAME_MAIN_METHODS

    public void startGame() {
        replaceFragment(mGameFragment, GameFragment.TAG);
        mGridGame.startGame();
    }

    public void stopGame() {
        mGridGame.stopGame();
        mGameFragment.disableCellButtons();
        replaceFragment(mCorrectionFragment, CorrectionFragment.TAG);
    }

    public void restartGameWithNewGrid() {
        mGridGame.addCompletedReward();
        mGridGame.generateNewGrid();
        mGameFragment.updateUI();
        startGame();
    }

    public void startNewGame() {
        mActivityMainBinding.startGameButton.setEnabled(false);
        destroyFragment(mCorrectionFragment);
        mActivityMainBinding.startGameButton.setEnabled(true);
        mGridGame.resetGame();
        mGridGame.updateGridGameSettings(GameHandler.getInstance().getGridGameSettings());
        startGame();
    }

    //endregion GRID_GAME_MAIN_METHODS

    //region BUTTON_ON_CLICK_METHODS

    public void startGameButtonClicked(View view) {
        if (!mGridGame.isRunning()) {
            if (mGridGame.hasEnoughLife()) {
                startNewGame();
            } else {
                onNotEnoughLife();
            }
        } else {
            mGridGame.setGameStatusText(getResources().getString(R.string.game_over_text));
            mGridGame.deductQuitGamePenalty();
            stopGame();
        }
    }

    public void settingsButtonClicked(View view) {

        if (mSettingsFragment.isVisible()) {
            mGridGame.resumeGameTimer();
            onSettingsHide();
        } else {
            mGridGame.pauseGameTimer();
            onSettingsShow();
        }
    }

    //endregion BUTTON_ON_CLICK_METHODS

    //region BUTTON_ON_CLICK_HELPERS

    public void onWrongCellTapped() {
        stopGame();
        mGridGame.setGameStatusText(getResources().getString(R.string.game_wrong_cell_text));
        mGridGame.setGameStatusVisible(true);

    }

    public void onLastCellTapped() {
        if (mGridGame.didPressLastButtonOnTime()) {
            mGridGame.updateCurrentBonusMultiplier();
            mGridGame.updateScore(mGridGame.getCurrentBonusMultiplier());
            displayBonusMessage(mGridGame.getCurrentBonusPoints());
            restartGameWithNewGrid();
        }
    }

    public void onSettingsShow() {
        mActivityMainBinding.settingsButton.setEnabled(false);
        replaceFragment(mSettingsFragment, SettingsFragment.TAG);
        mActivityMainBinding.settingsButton.setEnabled(true);
    }

    public void onSettingsHide() {
        mActivityMainBinding.settingsButton.setEnabled(false);
        destroyFragment(mSettingsFragment);
        mActivityMainBinding.settingsButton.setEnabled(true);
    }

    //endregion BUTTON_ON_CLICK_HELPERS

    //region FRAGMENT_TRANSACTIONS

    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                            .add(R.id.grid_game_fragment_layout, fragment, tag)
                            .addToBackStack(null)
                            .commit();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        destroyFragment(fragment);
        addFragment(fragment, tag);
    }

    public void destroyFragment(Fragment fragment) {
        if (getSupportFragmentManager().getFragments().contains(fragment)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    public void resurfaceGameFragmentWhenDestroyedWhileRunning() {
        if (getSupportFragmentManager().getFragments().size() <= 0 && mGridGame.isRunning()) {
            replaceFragment(mGameFragment, GameFragment.TAG);
        }
    }

    public void showWatchAdDialog() {
        WatchAdDialogFragment watchAdDialogFragment = new WatchAdDialogFragment();
        watchAdDialogFragment.show(getSupportFragmentManager(), WatchAdDialogFragment.TAG);
    }

    //endregion FRAGMENT_TRANSACTIONS

    //region UI_HELPERS

    public void displayToast(String message) {
        if (mStatusToast != null) {
            mStatusToast.cancel();
        }
        mStatusToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mStatusToast.show();
    }

    public void displayBonusMessage(int bonus) {
        if (mGridGame.isTimed()) {
            displayToast(String.format(getString(R.string.bonus_with_time_bonus_text),
                    bonus, GridGameSettingsUtil.getSecondsForTimeLimit(mGridGame.getTimeLimit())));
        } else {
            displayToast(String.format(getString(R.string.bonus_text), bonus));
        }
    }

    public void updateTimeRemainingText(float timeRemainingInSecondsFloat) {
        mGridGame.setGameStatusText(String.format(getResources().getString(R.string.time_remaining_text), timeRemainingInSecondsFloat));
    }

    //endregion UI_HELPERS

    //region ON_DESTROY_METHODS

    public void removeListeners() {
        EagerEventDispatcher.removeListener(R.string.event_compare_tapped_cell, mEventTappedCellEventListener);
        EagerEventDispatcher.removeListener(R.string.event_game_status_changed, mEventGameStatusChangedEventListener);
        EagerEventDispatcher.removeListener(R.string.event_settings_changed, mEventSettingsChangedEventListener);
        EagerEventDispatcher.removeListener(R.string.event_game_timer_ticked, mEventGameTimerTickedEventListener);
        EagerEventDispatcher.removeListener(R.string.event_game_timer_finished, mEventGameTimerFinishedEventListener);
        EagerEventDispatcher.removeListener(R.string.event_watch_rewarded_ad, mEventWatchRewardedAdEventListener);
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_reward_earned, mEventRewardedAdRewardEarned);
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_dismissed, mEventRewardedAdDismissed);
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_failed, mEventRewardedAdFailed);
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_loaded, mEventRewardedAdLoaded);
    }

    //endregion ON_DESTROY_METHODS

    //region ADS

    public void onRewardEarned() {
        mGridGame.restoreLife();
    }

    public void onNotEnoughLife() {
        showWatchAdDialog();
    }

    public void onAdLoading() {
        mGridGame.setIsAdRunning(true);
        mGridGame.setGameStatusText(getString(R.string.ad_loading_text));
    }

    public void onAdFinished() {
        mGridGame.setIsAdRunning(false);
        mGridGame.updateLifeGameStatus(getApplicationContext());
        AdHandler.getInstance().clearRewardedAdmobLoader();
    }

    public void onRewardedAdFailedToLoad() {
        mGridGame.setIsAdRunning(false);
        mGridGame.setGameStatusText(getString(R.string.failed_to_load_ad_text));
    }

    public void onRewardedAdLoadSuccess() {
        if (mGridGame.isActive()) {
            mRewardedAdmobLoader.setupAd();
            mRewardedAdmobLoader.showAd(this);
        }
    }

    public void onRewardedAdRequested(RewardedAdmobLoader loader) {
        mGridGame.setGameStatusText(getString(R.string.checking_for_connection_text));
        if (NetworkUtil.isConnectedToNetwork(getApplicationContext())) {
            AdHandler.getInstance().setRewardedAdmobLoader(loader);
            mRewardedAdmobLoader = AdHandler.getInstance().getCurrentRewardedAdmobLoader();
            loader.loadNewAd(this);
            loader.setIsRequested(true);
            onAdLoading();
        } else {
            mGridGame.setGameStatusText(getString(R.string.no_internet_connection));
        }
    }

    public void executePendingAds() {
        if (mRewardedAdmobLoader != null) {
            if (mRewardedAdmobLoader.isRequested()) {
                if (mRewardedAdmobLoader.hasLoaded()) {
                    if (!mRewardedAdmobLoader.isShown()) {
                        mRewardedAdmobLoader.setupAd();
                        mRewardedAdmobLoader.showAd(this);
                    }
                } else {
                    mRewardedAdmobLoader.loadNewAd(this);
                }
            }
        }
    }

    //endregion ADS

}
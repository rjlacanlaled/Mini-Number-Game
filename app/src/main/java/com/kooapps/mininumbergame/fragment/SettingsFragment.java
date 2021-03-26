package com.kooapps.mininumbergame.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.databinding.FragmentSettingsBinding;
import com.kooapps.mininumbergame.handler.GameHandler;
import com.kooapps.mininumbergame.model.main.GridGameSettings;
import com.kooapps.mininumbergame.model.helper.GridGameSettingsUtil;

public class SettingsFragment extends DialogFragment {

    public static final String TAG = "com.kooapps.mininumbergame.SETTINGS_FRAGMENT";

    // DATA & BINDING
    FragmentSettingsBinding mFragmentSettingsBinding;
    GridGameSettings mGridGameSettings;


    // Temporary Settings value holder
    private int mTempRowCount;
    private int mTempColumnCount;
    private int mTempBaseScore;
    private long mTempTimeLimit;
    private boolean mTempGameMode;
    private boolean mTempIsTimed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        init();
        return mFragmentSettingsBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateGridGameSettings();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GameHandler.getInstance().getGridGameSettings().setIsActive(false);
    }

    // INITIALIZER & INITIAL SETUP

    public void init() {
        mFragmentSettingsBinding.setGridGameSettings(GameHandler.getInstance().getGridGameSettings());
        mGridGameSettings = GameHandler.getInstance().getGridGameSettings();
        mGridGameSettings.setIsActive(true);
        initTempVariables();
        initListeners();
        setupSeekBars();
    }

    public void initTempVariables() {
        mTempRowCount = mGridGameSettings.getRowCount();
        mTempColumnCount = mGridGameSettings.getColumnCount();
        mTempBaseScore = mGridGameSettings.getBaseScore();
        mTempTimeLimit = mGridGameSettings.getTimeLimit();
        mTempGameMode = mGridGameSettings.isGridGameMode();
        mTempIsTimed = mGridGameSettings.isTimed();
    }

    public void setupSeekBars() {
        mFragmentSettingsBinding.rowSeekbar.setProgress(mTempRowCount - GridGameSettings.MIN_ROW);
        mFragmentSettingsBinding.columnSeekbar.setProgress(mTempColumnCount - GridGameSettings.MIN_COLUMN);
        mFragmentSettingsBinding.timeLimitSeekbar.setProgress(((int)(mTempTimeLimit - GridGameSettings.MIN_TIME_LIMIT) / 1000));
        mFragmentSettingsBinding.baseScoreSeekbar.setProgress(mTempBaseScore - GridGameSettings.MIN_BASE_SCORE);
    }

    // LISTENER INITIALIZER

    public void initListeners() {
        // SWITCH - TIME MODE
        mFragmentSettingsBinding.timedModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               mTempIsTimed = isChecked;
               mFragmentSettingsBinding.timedModeSwitch.setChecked(isChecked);
            }
        });

        // SWITCH - GAME MODE
        mFragmentSettingsBinding.gameModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTempGameMode = isChecked;
                mFragmentSettingsBinding.gameModeSwitch.setChecked(isChecked);
            }
        });

        // SEEKBAR - ROW SETTINGS
        mFragmentSettingsBinding.rowSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int minRow = GridGameSettings.MIN_ROW;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                mFragmentSettingsBinding.rowSettingsTexview.setText(String.format(getString(R.string.row_settings_text), progress + minRow));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // UPDATE ROW COUNT SETTINGS;
                mTempRowCount = seekBar.getProgress() + minRow;
            }
        });

        // SEEKBAR - COLUMN SETTINGS
        mFragmentSettingsBinding.columnSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int minColumn = GridGameSettings.MIN_COLUMN;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                mFragmentSettingsBinding.columnSettingsTextview.setText(String.format(getString(R.string.column_settings_text), progress + minColumn));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTempColumnCount = seekBar.getProgress() + minColumn;
            }
        });

        // SEEKBAR - TIME LIMIT SETTINGS
        mFragmentSettingsBinding.timeLimitSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int  minTimeLimit = (int)GridGameSettings.MIN_TIME_LIMIT / 1000;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                mFragmentSettingsBinding.timeLimitTextview.setText(String.format(getString(R.string.time_limit_settings_text), progress + minTimeLimit));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTempTimeLimit = (seekBar.getProgress() + minTimeLimit) * 1000;
            }
        });

        // SEEKBAR - BASE SCORE SETTINGS
        mFragmentSettingsBinding.baseScoreSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int minBaseScore = GridGameSettings.MIN_BASE_SCORE;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                mFragmentSettingsBinding.baseScoreTextview.setText(String.format(getString(R.string.base_score_settings_text), progress + minBaseScore));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTempBaseScore = seekBar.getProgress() + minBaseScore;
            }
        });
    }

    // UPDATE GAME SETTINGS WHEN FRAGMENT IS NO LONGER VISIBLE
    private void updateGridGameSettings() {
        mGridGameSettings.updateGridGameSettings(mTempRowCount, mTempColumnCount, mTempTimeLimit,
                mTempBaseScore, mTempGameMode, mTempIsTimed);
    }

}
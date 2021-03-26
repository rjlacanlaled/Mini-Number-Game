package com.kooapps.mininumbergame.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.databinding.FragmentWatchAdDialogBinding;
import com.kooapps.mininumbergame.model.event.main.EagerEventDispatcher;
import com.kooapps.mininumbergame.model.event.ads.EventWatchRewardedAd;


public class WatchAdDialogFragment extends DialogFragment {

    public static final String TAG = "com.kooapps.mininumbergame.dialog.fragment.WATCH_AD";

    FragmentWatchAdDialogBinding mFragmentWatchAdDialogBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentWatchAdDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_watch_ad_dialog, container, false);
        initListener();
        return mFragmentWatchAdDialogBinding.getRoot();
    }

    private void initListener() {
        mFragmentWatchAdDialogBinding.watchAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentWatchAdDialogBinding.watchAdButton.setEnabled(false);
                EagerEventDispatcher.dispatch(new EventWatchRewardedAd());
                dismiss();
            }
        });

        mFragmentWatchAdDialogBinding.watchAdDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
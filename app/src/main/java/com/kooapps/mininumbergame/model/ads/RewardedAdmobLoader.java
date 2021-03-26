package com.kooapps.mininumbergame.model.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdFailed;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdLoaded;
import com.kooapps.mininumbergame.model.event.main.EagerEventDispatcher;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdDismissed;
import com.kooapps.mininumbergame.model.event.ads.EventRewardedAdRewardEarned;

public class RewardedAdmobLoader implements AdLoader {

    private static final String UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd mRewardedAd;
    private boolean mIsRequested;
    private boolean mHasLoaded;
    private boolean mIsShown;

    //region GETTERS/SETTERS

    public RewardedAd getRewardedAd() {
        return mRewardedAd;
    }

    public boolean hasLoaded() {
        return mHasLoaded;
    }

    public void setHasLoaded(boolean hasLoaded) {
        mHasLoaded = hasLoaded;
    }

    public boolean isShown() {
        return mIsShown;
    }

    public void setIsShown(boolean mIsShown) {
        this.mIsShown = mIsShown;
    }

    public boolean isRequested() {
        return mIsRequested;
    }

    public void setIsRequested(boolean mIsRequested) {
        this.mIsRequested = mIsRequested;
    }

    //endregion GETTERS/SETTERS

    //region ABSTRACT_METHOD_IMPLEMENTATION

    @Override
    public void loadNewAd(Activity activity) {
        RewardedAd.load(activity.getApplicationContext(), UNIT_ID,
                new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        if (!hasLoaded()) {
                            mRewardedAd = rewardedAd;
                            setHasLoaded(true);
                            EagerEventDispatcher.dispatch(new EventRewardedAdLoaded());
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        EagerEventDispatcher.dispatch(new EventRewardedAdFailed());
                    }
                });
    }

    @Override
    public void setupAd() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                EagerEventDispatcher.dispatch(new EventRewardedAdDismissed());
            }

        });
    }

    @Override
    public void showAd(Activity activity) {
        mRewardedAd.show(activity, rewardItem -> EagerEventDispatcher.dispatch(new EventRewardedAdRewardEarned()));
    }

    //endregion ABSTRACT_METHOD_IMPLEMENTATION
}

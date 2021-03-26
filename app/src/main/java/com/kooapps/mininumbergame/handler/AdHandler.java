package com.kooapps.mininumbergame.handler;

import com.kooapps.mininumbergame.model.ads.RewardedAdmobLoader;

public class AdHandler {
    private static AdHandler mAdHandler = new AdHandler();

    private RewardedAdmobLoader mCurrentRewardedAdmobLoader;

    public static AdHandler getInstance() {
        return mAdHandler;
    }


    public void setRewardedAdmobLoader(RewardedAdmobLoader rewardedAdmobLoader) {
        mCurrentRewardedAdmobLoader = rewardedAdmobLoader;
    }

    public RewardedAdmobLoader getCurrentRewardedAdmobLoader() {
        return mCurrentRewardedAdmobLoader;
    }

    public void clearRewardedAdmobLoader() {
        mCurrentRewardedAdmobLoader = null;
    }
}

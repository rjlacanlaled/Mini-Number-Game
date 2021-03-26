package com.kooapps.mininumbergame.model.helper;

import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class ViewUtil {

    private static float TEXT_SCALE_FACTOR = 0.007f;

    public static int textSizeScale(int addedSize) {
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        return (int)(height * TEXT_SCALE_FACTOR) + addedSize;
    }

    @BindingAdapter("android:textScaleFactor")
    public static void setTextScaleFactor(TextView textView, int addedSize) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeScale(addedSize));
    }

}

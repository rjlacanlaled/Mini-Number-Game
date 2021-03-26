package com.kooapps.mininumbergame.model.main;

import android.os.Parcel;
import android.os.Parcelable;

public class Grid implements Parcelable {

    private int mRowCount;
    private int mColumnCount;

    public Grid(Parcel source) {
        mRowCount = source.readInt();
        mColumnCount = source.readInt();
    }

    public Grid(int rowCount, int columnCount) {
        this.mRowCount = rowCount;
        this.mColumnCount = columnCount;
    }

    public int getRowCount() {
        return mRowCount;
    }
    public int getColumnCount() {
        return mColumnCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRowCount);
        dest.writeInt(mColumnCount);
    }

    public static final Parcelable.Creator<Grid> CREATOR =
            new Parcelable.Creator<Grid>() {
                @Override
                public Grid createFromParcel(Parcel source) {
                    return new Grid(source);
                }

                @Override
                public Grid[] newArray(int size) {
                    return new Grid[size];
                }
            };
}

package com.hive.hive.association.votes.tabs.questions.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Birck.
 */
public class TimeLineModel implements Parcelable {


    private OrderStatus mStatus;

    public TimeLineModel() {
    }

    public TimeLineModel(String mMessage, String mDate, OrderStatus mStatus) {

        this.mStatus = mStatus;
    }




    public OrderStatus getStatus() {
        return mStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
    }

    protected TimeLineModel(Parcel in) {
        int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : OrderStatus.values()[tmpMStatus];
    }

    public static final Creator<TimeLineModel> CREATOR = new Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };
}

package com.jinglz.app.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ContestResult implements Parcelable {

    @SerializedName("_id")
    private String id;

    @SerializedName("amount")
    private int amount;

    @SerializedName("position")
    private int position;

    @SerializedName("createdAt")
    private String createdAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * overridden method with specified dest object of type {@link Parcel} and flags.
     * this method is used to write values to Parcel {@code dest}. it will retrieve id,
     * amount, position and createdAt write into {@code dest}. by writing into parcel it
     * can be used as a single object.
     *
     * @param dest Parcel object to write contest leadership info.
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.amount);
        dest.writeInt(this.position);
        dest.writeString(this.createdAt);
    }

    public ContestResult() {
    }

    /**
     * method with specified in, instantiating values
     * from the given Parcel {@code in} whose data had previously been written by
     * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
     *
     * @param in Parcel from which contest information will be read
     */
    protected ContestResult(Parcel in) {
        this.id = in.readString();
        this.amount = in.readInt();
        this.position = in.readInt();
        this.createdAt = in.readString();
    }

    /**
     * create new instance of ContestResult
     */
    public static final Creator<ContestResult> CREATOR = new Creator<ContestResult>() {
        @Override
        public ContestResult createFromParcel(Parcel source) {
            return new ContestResult(source);
        }

        @Override
        public ContestResult[] newArray(int size) {
            return new ContestResult[size];
        }
    };
}

package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.data.network.models.user.ContestUser;

@AutoValue
public abstract class ContestLeadershipRecord {

    @SerializedName("user")
    public abstract ContestUser user();

    @SerializedName("amount")
    public abstract int amount();

    @SerializedName("position")
    public abstract int position();

    /**
     * method with specified user of {@link ContestUser} type, amount and position, to construct
     * new AutoValue_ContestLeadershipRecord. this method is used to keep track of contest leadership.
     *
     * @param user contains information of user such as id, firstName, lastName and image
     * @param amount contains total amount possessed by contest
     * @param position contains information about contest position
     * @return ContestLeadershipRecord object
     */
    public static ContestLeadershipRecord create(ContestUser user, int amount, int position) {
        return new AutoValue_ContestLeadershipRecord(user, amount, position);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_ContestLeadershipRecord.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_ContestLeadershipRecord.GsonTypeAdapter}
     * @return TypeAdapter object of ContestLeadershipRecord type
     */
    public static TypeAdapter<ContestLeadershipRecord> typeAdapter(Gson gson) {
        return new AutoValue_ContestLeadershipRecord.GsonTypeAdapter(gson);
    }

}
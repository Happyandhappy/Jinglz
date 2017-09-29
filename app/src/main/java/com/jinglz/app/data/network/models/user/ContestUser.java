package com.jinglz.app.data.network.models.user;

import com.google.gson.annotations.SerializedName;

/**
 * This class is used to perform operation on user of
 * specific contest. it is used to retrieve details of user
 * along with its id, firstName, lastName and image.
 * getters and setters are generated according to specific value
 * for manipulations and selections
 */
public class ContestUser {

    @SerializedName("_id")
    private String id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("image")
    private String image;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

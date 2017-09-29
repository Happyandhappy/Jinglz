package com.jinglz.app.data.network.models.facebook;

import com.google.gson.annotations.SerializedName;

/**
 * class used to retrieve facebook user information, such as id, firstName,
 * lastName, email, avatar, gender and birthday.
 * it contains getter and setter for performing operations on retrieved user data.
 */
public class FacebookUserData {

    private Long id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    @SerializedName("picture")
    private FacebookPicture avatar;
    private String gender;
    @SerializedName("birthday")
    private String birthday;

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatar() {
        return avatar.getData().getUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(FacebookPicture avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public FacebookGender getGenderAsEnum() {
        return FacebookGender.getGenderFromValue(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

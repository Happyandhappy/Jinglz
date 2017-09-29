package com.jinglz.app.ui.profile.edit.model;

public class EditProfileModel {

    private String id;

    private String gender;

    private Integer yearOfBirth;

    private String firstName;

    private String lastName;

    private String zipCode;

    private String phone;

    private String image;

    /**
     * Constructs new EditProfileModel
     */
    public EditProfileModel() {
    }

    /**
     * Constructs new EditProfileModel with specified id, gender, yearOfBirth, firstName,
     * lastName, zipCode, phone and image.
     *
     * @param id String contains user id.
     * @param gender String contains gender.
     * @param yearOfBirth it contains year of birth
     * @param firstName String variable contains first name of user
     * @param lastName String variable contains  last name of user
     * @param zipCode String variable contains zip code.
     * @param phone String variable contains phone number
     * @param image String variable contains image path
     */
    public EditProfileModel(String id,
                            String gender,
                            Integer yearOfBirth,
                            String firstName,
                            String lastName,
                            String zipCode, String phone, String image) {
        this.id = id;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
        this.phone = phone;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
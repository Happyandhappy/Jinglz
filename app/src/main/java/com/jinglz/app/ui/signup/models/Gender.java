package com.jinglz.app.ui.signup.models;


public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    /**
     * method to retrieve gender from specified value.
     *
     * @param value String contains user gender
     * @return Gender
     */
    public static Gender getGenderFromValue(String value) {
        for (Gender item : Gender.values()) {
            if (item.gender.equals(value)) {
                return item;
            }
        }
        return null;
    }
}

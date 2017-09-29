package com.jinglz.app.data.network.models.facebook;

public enum FacebookGender {
    MALE("male"),
    FEMALE("female");

    private String gender;

    /**
     * method with specified gender, to set gender of user
     *
     * @param gender String variable that contains gender
     */
    FacebookGender(String gender) {
        this.gender = gender;
    }

    /**
     * method to return gender of user
     *
     * @return String variable containing gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * static method to retrieve gender from {@link FacebookGender#values()}.
     * it uses specified string value to fetch gender. return {@code gender} if {@code value}
     * equals to {@see gender}, null otherwise.
     * @param value
     * @return
     */
    public static FacebookGender getGenderFromValue(String value) {
        if (value == null) {
            return null;
        }
        for (FacebookGender gender : FacebookGender.values()) {
            if (gender.gender.equals(value)) {
                return gender;
            }
        }
        return null;
    }
}

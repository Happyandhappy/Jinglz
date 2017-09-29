package com.jinglz.app.data.network.models;

import com.jinglz.app.ui.signup.models.Gender;

public interface SignUpModel {

    boolean acceptTermsAndConditions();

    String email();

    String phoneNumber();

    Gender gender();

    Integer yearOfBirth();

    String firstName();

    String lastName();

    String referrerCode();

    String image();

    String zipCode();

}

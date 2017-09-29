package com.jinglz.app.data.network.models.google;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.jinglz.app.data.network.models.SignUpModel;
import com.jinglz.app.ui.signup.models.Gender;

@AutoValue
public abstract class GoogleSignUpModel implements SignUpModel {

    /**
     * static method with specified model to construct new AutoValue_GoogleSignUpModel.
     *
     * @param model to fetch user details that requires to construct {@link AutoValue_GoogleSignUpModel}
     * @return GoogleSignUpModel object
     */
    public static GoogleSignUpModel create(com.jinglz.app.ui.signup.models.SignUpModel model) {
        return new AutoValue_GoogleSignUpModel(model.isAcceptTermsAndConditions(),
                                               model.getEmail(),
                                               model.getPhoneNumber(),
                                               model.getGender(),
                                               model.getYearOfBirth(),
                                               model.getFirstName(),
                                               model.getLastName(),
                                               model.getReferrerCode(),
                                               model.getImage(),
                                               model.getZipCode());
    }

    public abstract boolean acceptTermsAndConditions();

    public abstract String email();

    public abstract String phoneNumber();

    @Nullable
    public abstract Gender gender();

    @Nullable
    public abstract Integer yearOfBirth();

    public abstract String firstName();

    public abstract String lastName();

    @Nullable
    public abstract String referrerCode();

    @Nullable
    public abstract String image();

    public abstract String zipCode();
}

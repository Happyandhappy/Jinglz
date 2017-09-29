package com.jinglz.app.ui.signup.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.jinglz.app.data.network.models.SignUpModel;


@AutoValue
public abstract class FacebookSignUpModel implements SignUpModel {

    /**
     * Create new instance of AutoValue_FacebookSignUpModel with specified facebookId and model that contains
     * user details like email, phone number, gender, age, name, image, zip code and referrer code.
     *
     * @param facebookId String Contains facebook id of user who is currently logging in.
     * @param model it  contains a {@link com.jinglz.app.ui.signup.models.SignUpModel} object in which signup data is stored.
     *
     * @return AutoValue_FacebookSignUpModel instance
     */
    public static FacebookSignUpModel create(String facebookId, com.jinglz.app.ui.signup.models.SignUpModel model) {
        return new AutoValue_FacebookSignUpModel(facebookId,
                                                 model.isAcceptTermsAndConditions(),
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

    public abstract String facebookId();

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

package com.jinglz.app.business.auth.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.jinglz.app.ui.start.signin.models.SignInErrorModel;
import com.jinglz.app.ui.start.signin.models.SignInManualModel;

@AutoValue
public abstract class LocalValidateResultModel {

    /**
     * method receives {@code SignInManualModel} having credentials
     * and {@code SignInErrorModel} with error description which returns
     * {@code new AutoValue_LocalValidateResultModel(signInModel, error)}
     *
     * @param signInModel
     * @param error
     * @return will return object of AutoValue_LocalValidateResultModel
     */
    public static LocalValidateResultModel create(SignInManualModel signInModel, SignInErrorModel error) {
        return new AutoValue_LocalValidateResultModel(signInModel, error);
    }

    public abstract SignInManualModel signInModel();

    @Nullable
    public abstract SignInErrorModel error();

}

package com.jinglz.app.business.profile.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jinglz.app.R;
import com.jinglz.app.business.base.validation.ValidationError;
import com.jinglz.app.business.base.validation.ValidationException;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Single;

import static com.jinglz.app.Constants.MIN_YEAR_OF_BIRTH;

@PerSession
public class ProfileLocalValidation {

    private final Context mContext;

    @Inject
    public ProfileLocalValidation(Context context) {
        this.mContext = context;
    }

    /**
     * method with specified model for validating user detail by calling {@link #localValidate}
     *
     * @param model of type EditProfileModel
     * @return Single object of EditProfileModel
     */
    public Single<EditProfileModel> validate(@NonNull EditProfileModel model) {
        return Single.just(model)
                .flatMap(this::localValidate)
                .map(validate -> model);
    }

    /**
     * method with specified model of type {@link EditProfileModel}.
     * checks for mandatory fields by {@link #mandatoryFieldsValidate(EditProfileModel)},
     * checks date of birth for minimum specified date by calling {@link #incorrectFieldsValidate(EditProfileModel)}
     * returns {@code new ValidationException(errors)} if error occurs, true otherwise.
     *
     * @param model  of type EditProfileModel
     * @return Single object of Boolean
     */
    private Single<Boolean> localValidate(@NonNull EditProfileModel model) {
        List<ValidationError> errors;

        errors = mandatoryFieldsValidate(model);
        if (errors == null || errors.isEmpty()) {
            errors = incorrectFieldsValidate(model);
        }
        if (errors != null && !errors.isEmpty()) {
            return Single.error(new ValidationException(errors));
        }

        return Single.just(true);
    }

    /**
     * method receives {@link EditProfileModel} as input and checks mandatory fields.
     * returns list of {@link ValidationError} if fields are empty.
     *
     * @param model of type EditProfileModel
     * @return Single object of ValidationError
     */
    @Nullable
    private List<ValidationError> mandatoryFieldsValidate(@NonNull EditProfileModel model) {
        final List<ValidationError> errors = new ArrayList<>();
        final String message = mContext.getString(R.string.error_required);
        if (TextUtils.isEmpty(model.getFirstName())) {
            errors.add(new ValidationError(ValidationException.Field.FIRST_NAME, message));
        }
        if (TextUtils.isEmpty(model.getLastName())) {
            errors.add(new ValidationError(ValidationException.Field.LAST_NAME, message));
        }
        if (TextUtils.isEmpty(model.getPhone())) {
            errors.add(new ValidationError(ValidationException.Field.PHONE_NUMBER, message));
        }
        if (TextUtils.isEmpty(model.getZipCode())) {
            errors.add(new ValidationError(ValidationException.Field.ZIP_CODE, message));
        }
        return errors;
    }

    /**
     * method receives {@link EditProfileModel} as input and checks date of birth if it is
     * less than {@link com.jinglz.app.Constants#MIN_YEAR_OF_BIRTH}.
     * returns list of {@link ValidationError} if {@code model.getYearOfBirth()} is null or less than
     * the min value.
     *
     * @param model of type EditProfileModel
     * @return Single object of ValidationError
     */
    @Nullable
    private List<ValidationError> incorrectFieldsValidate(@NonNull EditProfileModel model) {
        final List<ValidationError> errors = new ArrayList<>();

        if (model.getYearOfBirth() != null && model.getYearOfBirth() < MIN_YEAR_OF_BIRTH) {
            errors.add(new ValidationError(ValidationException.Field.YEAR_OF_BIRTH,
                                           mContext.getString(R.string.error_invalid_year_of_birth)));
        }

        return errors;
    }
}

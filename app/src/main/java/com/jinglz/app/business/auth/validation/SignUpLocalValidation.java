package com.jinglz.app.business.auth.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import com.jinglz.app.Constants;
import com.jinglz.app.R;
import com.jinglz.app.business.base.validation.ValidationError;
import com.jinglz.app.business.base.validation.ValidationException;
import com.jinglz.app.data.network.models.SignUpModel;
import com.jinglz.app.data.repositories.LocationRepository;
import com.jinglz.app.ui.start.signin.models.SignInManualModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Single;

import static com.jinglz.app.Constants.MIN_YEAR_OF_BIRTH;

public class SignUpLocalValidation {

    private final Context mContext;
    private final LocationRepository mLocationRepository;

    /**
     * Constructs new SignUpLocalValidation with specified context and locationRepository.
     *
     * @param context            to initialize {@code mContext}
     * @param locationRepository to initialize {@code mLocationRepository}
     */
    @Inject
    public SignUpLocalValidation(Context context, LocationRepository locationRepository) {
        this.mContext = context;
        this.mLocationRepository = locationRepository;
    }

    /**
     * method that validates {@link SignUpModel}. validate manually for local data.
     * use {@link #localValidate(SignUpModel)} for validation.
     *
     * @param facebookSignUpModel variable to be validated
     * @return {@code Single.just(facebookSignUpModel).flatMap(this::localValidate).map(validate -> facebookSignUpModel)}
     */
    public Single<SignUpModel> validate(@NonNull SignUpModel facebookSignUpModel) {
        return Single.just(facebookSignUpModel)
                .flatMap(this::localValidate)
                .map(validate -> facebookSignUpModel);
    }

    /**
     * method that validates {@link com.jinglz.app.ui.signup.models.SignUpModel}. validate manually for local data.
     * use {@link #localValidate(com.jinglz.app.ui.signup.models.SignUpModel)} for validation.
     *
     * @param signUnModel variable to be validated
     * @return Single.just(signUnModel).flatMap(this::localValidate).map(validate -> signUnModel)
     */
    public Single<com.jinglz.app.ui.signup.models.SignUpModel> validate(@NonNull
                                                                        com.jinglz.app.ui.signup.models.SignUpModel signUnModel) {
        return Single.just(signUnModel)
                .flatMap(this::localValidate)
                .map(validate -> signUnModel);
    }

    /**
     * method that receives {@link com.jinglz.app.ui.signup.models.SignUpModel} as parameter,
     * perform validations and return {@code Single.just(true)} if valid.
     * tests for mandatory fields by calling {@link #mandatoryFieldsValidate(com.jinglz.app.ui.signup.models.SignUpModel)}
     * and incorrect format of the fields by using {@link #incorrectFieldsValidate(com.jinglz.app.ui.signup.models.SignUpModel)}
     * {@code error} holds generated error in validation process
     *
     * @param model variable to be validated
     * @return {@code Single.just(true)}
     */
    private Single<Boolean> localValidate(@NonNull com.jinglz.app.ui.signup.models.SignUpModel model) {
        List<ValidationError> errors;

        errors = mandatoryFieldsValidate(model);
        if (errors == null || errors.isEmpty()) {
            errors = incorrectFieldsValidate(model);
        }
        if (errors == null || errors.isEmpty()) {
            final ValidationError validationError = incorrectCountry();
            errors = validationError == null ? null : Collections.singletonList(validationError);
        }
        if (errors != null && !errors.isEmpty()) {
            return Single.error(new ValidationException(errors));
        }

        return Single.just(true);
    }

    /**
     * method that receives {@link SignUpModel} as parameter,
     * perform validations and return {@code Single.just(true)} if valid.
     * tests for mandatory fields by calling {@link #mandatoryFieldsValidate(SignUpModel)}
     * and incorrect format of the fields by using {@link #incorrectFieldsValidate(SignUpModel)}
     * {@code error} holds generated error in validation process
     *
     * @param model variable to be validated
     * @return {@code Single.just(true)}
     */
    private Single<Boolean> localValidate(@NonNull SignUpModel model) {
        List<ValidationError> errors;

        errors = mandatoryFieldsValidate(model);
        if (errors == null || errors.isEmpty()) {
            errors = incorrectFieldsValidate(model);
        }
        if (errors == null || errors.isEmpty()) {
            final ValidationError validationError = incorrectCountry();
            errors = validationError == null ? null : Collections.singletonList(validationError);
        }
        if (errors != null && !errors.isEmpty()) {
            return Single.error(new ValidationException(errors));
        }

        return Single.just(true);
    }

    /**
     * method that receives {@link com.jinglz.app.ui.signup.models.SignUpModel} as input parameter.
     * test mandatory fields by using {@link ValidationError}. retrieve fields from {@code model}.
     * add {@code message} in list {@code errors}, if field is empty.
     *
     * @param model variable to be validated
     * @return list of {@code errors}
     */

    @Nullable
    private List<ValidationError> mandatoryFieldsValidate(@NonNull com.jinglz.app.ui.signup.models.SignUpModel model) {
        final List<ValidationError> errors = new ArrayList<>();
        final String message = mContext.getString(R.string.error_required);
        if (TextUtils.isEmpty(model.getFirstName())) {
            errors.add(new ValidationError(ValidationException.Field.FIRST_NAME, message));
        }
        if (TextUtils.isEmpty(model.getLastName())) {
            errors.add(new ValidationError(ValidationException.Field.LAST_NAME, message));
        }
        if (TextUtils.isEmpty(model.getEmail())) {
            errors.add(new ValidationError(ValidationException.Field.EMAIL, message));
        }
        if (TextUtils.isEmpty(model.getPhoneNumber())) {
            errors.add(new ValidationError(ValidationException.Field.PHONE_NUMBER, message));
        }
        if (TextUtils.isEmpty(model.getZipCode())) {
            errors.add(new ValidationError(ValidationException.Field.ZIP_CODE, message));
        }
        if (TextUtils.isEmpty(model.getPassword())) {
            errors.add(new ValidationError(ValidationException.Field.PASSWORD, message));
        }
        if (TextUtils.isEmpty(model.getConfirmPassword())) {
            errors.add(new ValidationError(ValidationException.Field.CONFIRM_PASSWORD, message));
        }
        if (!model.isAcceptTermsAndConditions()) {
            errors.add(new ValidationError(ValidationException.Field.TERMS,
                    mContext.getString(R.string.error_terms_required)));
        }
        return errors;
    }

    /**
     * method that receives {@link SignUpModel} as input parameter.
     * test mandatory fields by using {@link ValidationError}. retrieve fields from {@code model}.
     * add {@code message} in list {@code errors}, if field is empty.
     *
     * @param model variable to be validated
     * @return list of {@code errors}
     */
    @Nullable
    private List<ValidationError> mandatoryFieldsValidate(@NonNull SignUpModel model) {
        final List<ValidationError> errors = new ArrayList<>();
        final String message = mContext.getString(R.string.error_required);
        if (TextUtils.isEmpty(model.firstName())) {
            errors.add(new ValidationError(ValidationException.Field.FIRST_NAME, message));
        }
        if (TextUtils.isEmpty(model.lastName())) {
            errors.add(new ValidationError(ValidationException.Field.LAST_NAME, message));
        }
        if (TextUtils.isEmpty(model.email())) {
            errors.add(new ValidationError(ValidationException.Field.EMAIL, message));
        }
        if (TextUtils.isEmpty(model.phoneNumber())) {
            errors.add(new ValidationError(ValidationException.Field.PHONE_NUMBER, message));
        }
        if (TextUtils.isEmpty(model.zipCode())) {
            errors.add(new ValidationError(ValidationException.Field.ZIP_CODE, message));
        }
        if (!model.acceptTermsAndConditions()) {
            errors.add(new ValidationError(ValidationException.Field.TERMS,
                    mContext.getString(R.string.error_terms_required)));
        }
        return errors;
    }

    /**
     * method that receives {@link com.jinglz.app.ui.signup.models.SignUpModel} as input parameter.
     * test pattern for {@code model.getEmail()},
     * test length validation for {@code model.getPassword()},
     * match for {@code model.getPassword()} and {@code model.getConfirmPassword()},
     * and checks limit  for {@code model.getYearOfBirth()} by comparing {@link Constants#MIN_YEAR_OF_BIRTH}.
     * add {@code message} in list {@code errors}, if invalid format.
     *
     * @param model variable to be validated
     * @return list of {@code errors}
     */
    @Nullable
    private List<ValidationError> incorrectFieldsValidate(@NonNull com.jinglz.app.ui.signup.models.SignUpModel model) {
        final List<ValidationError> errors = new ArrayList<>();

        if (!Patterns.EMAIL_ADDRESS.matcher(model.getEmail()).matches()) {
            errors.add(new ValidationError(ValidationException.Field.EMAIL,
                    mContext.getString(R.string.error_invalid_email)
            ));
        }

        if (model.getPassword().length() < Constants.MIN_PASSWORD_LENGTH) {
            errors.add(new ValidationError(ValidationException.Field.PASSWORD,
                    mContext.getString(R.string.error_password_length,
                            Constants.MIN_PASSWORD_LENGTH)
            ));
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            errors.add(new ValidationError(ValidationException.Field.CONFIRM_PASSWORD,
                    mContext.getString(R.string.error_password_match)
            ));
        }

        if (model.getYearOfBirth() != null && model.getYearOfBirth() < MIN_YEAR_OF_BIRTH) {
            errors.add(new ValidationError(ValidationException.Field.YEAR_OF_BIRTH,
                    mContext.getString(R.string.error_invalid_year_of_birth)));
        }

        return errors;
    }

    /**
     * method that receives {@link SignUpModel} as input parameter.
     * test pattern for {@code model.email()}
     * and checks limit  for {@code model.yearOfBirth()} by comparing {@link Constants#MIN_YEAR_OF_BIRTH}.
     * add {@code message} in list {@code errors}, if invalid format.
     *
     * @param model variable to be validated
     * @return list of {@code errors}
     */
    @Nullable
    private List<ValidationError> incorrectFieldsValidate(@NonNull SignUpModel model) {
        final List<ValidationError> errors = new ArrayList<>();

        if (!Patterns.EMAIL_ADDRESS.matcher(model.email()).matches()) {
            errors.add(new ValidationError(ValidationException.Field.EMAIL,
                    mContext.getString(R.string.error_invalid_email)
            ));
        }

        if (model.yearOfBirth() != null && model.yearOfBirth() < Constants.MIN_YEAR_OF_BIRTH) {
            errors.add(new ValidationError(ValidationException.Field.YEAR_OF_BIRTH,
                    mContext.getString(R.string.error_invalid_year_of_birth)));
        }

        return errors;
    }

    /**
     * method checks the {@code currentCountry} with array {@code countries}.
     *
     * @return {@code SignInErrorModel.create(mContext.getString(R.string.error_enable_location))} if
     *         {@code currentCountry} null, @code SignInErrorModel.create(mContext.getString(R.string.error_incorrect_location))} if
     *         {@code currentCountry} does not match with array {@code countries}, null otherwise.
     */
    @Nullable
    private ValidationError incorrectCountry() {
        final String[] countries = mContext.getResources().getStringArray(R.array.supports_countries);
        final String currentCountry = mLocationRepository.getUserCountry().toBlocking().value();
        if (currentCountry == null) {
            return new ValidationError(ValidationException.Field.NONE,
                    mContext.getString(R.string.error_enable_location));
        } else if (!Arrays.asList(countries).contains(currentCountry)) {
            return new ValidationError(ValidationException.Field.COUNTRY,
                    mContext.getString(R.string.error_incorrect_location));
        }
        return null;
    }

}

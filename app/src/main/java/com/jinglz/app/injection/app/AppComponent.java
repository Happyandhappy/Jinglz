package com.jinglz.app.injection.app;

import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.injection.session.SessionComponent;
import com.jinglz.app.ui.confirmcode.password.ConfirmCodePasswordPresenter;
import com.jinglz.app.ui.confirmcode.phone.ConfirmCodePhonePresenter;
import com.jinglz.app.ui.feed.FeedFragment;
import com.jinglz.app.ui.forgotpassword.ForgotPasswordPresenter;
import com.jinglz.app.ui.howitworks.HowItWorksPresenter;
import com.jinglz.app.ui.legalnotice.LegalNoticePresenter;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.profile.edit.EditProfileActivity;
import com.jinglz.app.ui.resetpassword.ResetPasswordPresenter;
import com.jinglz.app.ui.sharewin.ShareAndWinFragment;
import com.jinglz.app.ui.signup.SignUpActivity;
import com.jinglz.app.ui.signup.SignUpPresenter;
import com.jinglz.app.ui.splash.SplashPresenter;
import com.jinglz.app.ui.start.signin.SignInPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    SessionComponent.Builder sessionComponentBuilder();

    void inject(SplashPresenter presenter);

    void inject(SignUpPresenter presenter);

    void inject(SignInPresenter presenter);

    void inject(SignUpActivity activity);

    void inject(ForgotPasswordPresenter presenter);

    void inject(ConfirmCodePasswordPresenter presenter);

    void inject(ResetPasswordPresenter presenter);

    void inject(FeedFragment fragment);

    void inject(ConfirmCodePhonePresenter presenter);

    void inject(MainActivity activity);

    void inject(EditProfileActivity activity);

    void inject(ShareAndWinFragment fragment);

    void inject(HowItWorksPresenter presenter);

    void inject(LegalNoticePresenter presenter);

    AnalyticsFacade provideAnalyticsFacade();
}

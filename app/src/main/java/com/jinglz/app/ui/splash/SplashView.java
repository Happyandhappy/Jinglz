package com.jinglz.app.ui.splash;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.javiersantos.appupdater.objects.Update;
import com.jinglz.app.ui.base.BaseView;

/**
 * This interface is  create perform actions like
 * openNewVersionDialog when new version of app is available.
 * openSignIn() open {@link com.jinglz.app.ui.start.signin.SignInFragment}
 * openSignUp() open {@link com.jinglz.app.ui.signup.SignUpActivity}
 * openHome() open {@link com.jinglz.app.ui.main.MainActivity}
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface SplashView extends BaseView {

    void openNewVersionDialog(Update update);

    void openOnboarding();

    void openSignIn();

    void openSignUp(String code);

    void openHome();

}

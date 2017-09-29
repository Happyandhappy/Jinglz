package com.jinglz.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.javiersantos.appupdater.objects.Update;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.signup.SignUpActivity;
import com.jinglz.app.ui.start.StartActivity;
import com.jinglz.app.updateChecker.UpdateRunnable;
import com.jinglz.app.utils.BrowserUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import io.branch.referral.Branch;

public class SplashActivity extends BaseActivity implements SplashView {

    @InjectPresenter SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


/*
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();

        Log.e("Time zone","="+tz.getDisplayName());
        Log.e("Time zone","="+TimeZone.getDefault().getID());
*/

        /**
         * Check new version of app is available or not
         * if the installed version is old then @Link {@link #openNewVersionDialog(Update)} will show
         * message in dialog.
         *
         */
        new UpdateRunnable(this, new Handler()).start();
    }

    /**
     * If user is already login @Link {@link #openHome()} is called to redirect
     * user to home page {@link MainActivity}
     * else
     * {@Link #openOnboarding()} is called.
     *
     * when user clicks on sign in button @Link {@link #openSignIn()} will takes user to
     * @Link {@link com.jinglz.app.ui.start.signin.SignInFragment} screen
     * if clicks on sign up @Link {@link #openSignUp(String)} will redirect to @Link {@link SignUpActivity} screen
     * for Intents @Link {@link #onNewIntent(Intent)} is called
     */
    @Override
    public void onStart() {
        super.onStart();
        final Branch branch = Branch.getInstance();
        branch.initSession((referringParams, error) -> {
            if (error == null) {
                mPresenter.referralFlow(referringParams);
            } else {
                mPresenter.baseFlow();
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    /**
     * When new version of app is available on play store
     * it uses {@link android.app.AlertDialog} to prompt user.
     * @param update
     */
    @Override
    public void openNewVersionDialog(Update update) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_please_update_your_application)
                .setOnDismissListener(dialog -> finish())
                .setPositiveButton(R.string.text_update,
                        (dialog, which) -> BrowserUtils.openLink(this, update.getUrlToDownload().toString()))
                .show();
    }

    @Override
    public void openOnboarding() {
        startActivity(StartActivity.getIntent(this));
        finish();
    }

    /**
     * to redirect user to home {@link MainActivity} will be launched.
     */
    @Override
    public void openHome() {
        startActivity(MainActivity.getIntent(this));
        finish();
    }

    /**
     * If user's logged in session is out {@link StartActivity} is launched
     */
    @Override
    public void openSignIn() {
        startActivity(StartActivity.getIntent(this, true));
        finish();
    }

    /**
     * If user dont have account with jinglz
     * @param code String value contains code.
     */
    @Override
    public void openSignUp(String code) {
        startActivity(SignUpActivity.getIntent(this));
        finish();
    }
}

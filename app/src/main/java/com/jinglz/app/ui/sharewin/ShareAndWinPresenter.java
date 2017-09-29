package com.jinglz.app.ui.sharewin;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.share.ShareInteractor;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ShareAndWinPresenter extends BasePresenter<ShareAndWinView> {

    private static final String TAG = "ShareAndWinPresenter";
    @Inject
    ShareInteractor mShareInteractor;

    /**
     * Constructs new ShareAndWinPresenter.
     */
    public ShareAndWinPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadReferralCode();
    }

    /**
     * method to retrieve shared message by calling {@link ShareInteractor#getShareMessage()}
     * the message will be passed to {@code startShareIntent()}.
     */
    public void shareClick() {
        mShareInteractor.getShareMessage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    getViewState().startShareIntent(message);
                }, throwable -> {

                });
    }

    /**
     * It will firstly load referral code from user data by calling {@link ShareInteractor#getReferralCode()}
     * and then set code in to {@code setReferralCode()}.
     */

    private void loadReferralCode() {
        mShareInteractor.getReferralCode()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(code -> {
                    getViewState().setReferralCode(code);
                }, throwable -> {

                });
    }
}

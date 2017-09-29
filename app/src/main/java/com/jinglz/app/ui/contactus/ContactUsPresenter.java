package com.jinglz.app.ui.contactus;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.info.InformationInteractor;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ContactUsPresenter extends BasePresenter<ContactUsView> {

    private static final String TAG = "ContactUsPresenter";

    @Inject AnalyticsFacade mAnalyticsFacade;
    @Inject InformationInteractor mInformationInteractor;

    public ContactUsPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    /**
     * This method is used to send feedback using  {@link InformationInteractor#sendContactUsMessage(String)}
     * on success it will track event by calling {@link AnalyticsFacade#trackEvent(Event)}, throws an
     * exception otherwise.
     *
     * @param text String containing feedback message.
     */
    public void onContactUsClick(String text) {
        getViewState().hideKeyboard();
        getViewState().showProgress();
        final Subscription subscription = mInformationInteractor.sendContactUsMessage(text)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(ignored -> mAnalyticsFacade.trackEvent(Event.SUPPORT_TICKET_SENT))
                .subscribe(contactUsResponse -> {
                    getViewState().hideProgress();
                    if (contactUsResponse.sent()) {
                        getViewState().onSendSuccess();
                    }
                }, throwable -> {
                    getViewState().hideKeyboard();
                    Log.e(TAG, "onContactUsClick: ", throwable);
                });
        addSubscription(subscription);
    }

    /**
     * perform operation when text changed.
     * @param sequence character sequence to fetch length.
     */
    public void onTextChanged(CharSequence sequence) {
        getViewState().enableSendButton(sequence.length() != 0);
    }

}

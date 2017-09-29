package com.jinglz.app.ui.legalnotice;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.info.InformationInteractor;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class LegalNoticePresenter extends BasePresenter<LegalNoticeView> {

    @Inject InformationInteractor mInformationInteractor;

    /**
     * Return an instance of LegalNoticePresenter
     */
    public LegalNoticePresenter() {
        App.get().getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadTerms();
    }

    /**
     * This method is called to load terms and conditions from
     * {@link InformationInteractor#getTermsAndConditions()}.
     */
    private void loadTerms() {
        mInformationInteractor.getTermsAndConditions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getViewState().setText(s);
                }, throwable -> {

                });
    }
}

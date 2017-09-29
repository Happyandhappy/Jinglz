package com.jinglz.app.ui.howitworks;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.jinglz.app.App;
import com.jinglz.app.business.info.InformationInteractor;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

@InjectViewState
public class HowItWorksPresenter extends BasePresenter<HowItWorksView> {

    @Inject
    InformationInteractor mInformationInteractor;

    public HowItWorksPresenter() {
        App.get().getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        setText();
    }

    /**
     * call this method to open legal notice.
     */
    public void onLegalNoticeClick() {
        getViewState().openLegalNotice();
    }

    /**
     * This method is used to set text by fetching from {@link InformationInteractor#getHowItWorks()}
     * set to the view return by {@link MvpPresenter#getViewState()}
     */
    private void setText() {
        getViewState().setText(mInformationInteractor.getHowItWorks());
    }
}

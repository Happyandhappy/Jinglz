package com.jinglz.app.ui.start;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.ui.base.BasePresenter;

@InjectViewState
public class StartPresenter extends BasePresenter<StartView> {

    private int mPageCount;
    private int mCurrentPage;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showBackButton(false);
    }

    public void init(int pageCount, boolean skipOnBoarding) {
        mPageCount = pageCount;
        if (skipOnBoarding) {
            getViewState().skipOnBoarding();
        }
    }

    /**
     * mCurrentPage will be incremented by one
     */
    public void onNextClick() {
        getViewState().setSelectedPage(mCurrentPage + 1);
    }
    /**
     * mCurrentPage will be decremented by one
     */
    public void onBackClick() {
        getViewState().setSelectedPage(mCurrentPage - 1);
    }

    /**
     * mCurrentPage value is changed
     */
    public void onPageChanged(int currentPage) {
        mCurrentPage = currentPage;
        getViewState().showBackButton(mCurrentPage > 0);
        getViewState().showNextButton(mCurrentPage < mPageCount - 1);
        getViewState().highlightIndicator(mCurrentPage == mPageCount - 1);
        getViewState().highlightBackButton(mCurrentPage == mPageCount - 1);
    }
}

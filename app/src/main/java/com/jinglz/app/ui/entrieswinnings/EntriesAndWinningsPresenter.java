package com.jinglz.app.ui.entrieswinnings;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.share.ShareInteractor;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class EntriesAndWinningsPresenter extends BasePresenter<EntriesAndWinningsView> {

    private static final String TAG = "EntriesAndWinningsPrese";

    @Inject ShareInteractor mShareInteractor;

    private final List<ContestResult> mResults;

    /**
     * Constructs new EntriesAndWinningsPresenter with specified results
     * @param results
     */
    public EntriesAndWinningsPresenter(List<ContestResult> results) {
        App.get().getSessionComponent().inject(this);
        mResults = results;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().initCoinsLayout(mResults);
    }

    /**
     * This method will be used to retrieve coin wining message by calling
     * {@link ShareInteractor#getShareWonCoinMessage(int)}. on success it send message to
     * {@link EntriesAndWinningsView}, throws exception otherwise.
     */
    public void onShareClick() {
        mShareInteractor.getShareWonCoinMessage(getTotalReveal())
                .subscribe(message -> {
                    getViewState().openShareChooser(message);
                }, throwable -> {
                    Log.e(TAG, "onShareClick: ", throwable);
                });
    }

    /**
     * used to retrieve total amount from {@see mResults}
     * @return {@code coint}
     */
    private int getTotalReveal() {
        int coint = 0;
        for (ContestResult item : mResults) {
            coint += item.getAmount();
        }
        return coint;
    }
}

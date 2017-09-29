package com.jinglz.app.ui.tutorials;

import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.jinglz.app.data.local.event.FeedTutorialsEvent;
import com.jinglz.app.ui.base.BasePresenter;

/**
 * when clicked on feedTutorials It will redirect to {@link FeedTutorialsEvent}
 *
 */
@InjectViewState
public class TutorialsPresenter extends BasePresenter<TutorialsView> {

    private static final String TAG = "TutorialsPresenter";

    public void onFeedTutorialsClick() {
        RxBus.get().post(new FeedTutorialsEvent());
        getViewState().showFeedTutorials();
    }

    public void onVideoRulesClick() {
        getViewState().showVideoRules();
    }

}

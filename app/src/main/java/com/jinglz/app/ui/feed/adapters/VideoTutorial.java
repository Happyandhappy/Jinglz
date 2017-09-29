package com.jinglz.app.ui.feed.adapters;

import android.view.View;

import com.jinglz.app.ui.feed.models.TutorialModel;

/**
 * This interface is used to retrieve tutorial view according to the type of
 * tutorial type that we can fetch from {@link com.jinglz.app.ui.feed.models.TutorialModel.TutorialType}
 */

public interface VideoTutorial {

    View getTutorialView(@TutorialModel.TutorialType int type);

}

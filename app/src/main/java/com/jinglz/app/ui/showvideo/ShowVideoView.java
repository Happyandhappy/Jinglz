package com.jinglz.app.ui.showvideo;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;
import com.jinglz.app.ui.showvideo.models.ShowVideoModel;

/**
 * An interface that handel video functionality extends BaseView
 * to handle progress dialog and keyboard.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ShowVideoView extends BaseView {

    @StateStrategyType(value = SingleStateStrategy.class)
    void showVideo(ShowVideoModel video);

    void playVideo(boolean play);

    void showFirstLowVolumeAlert(boolean show);

    void showLowVolumeAlert(boolean show);

    void showFirstEyeContactAlert(boolean show);

    void showEyeContactAlert(boolean show);

    void showPermissionError();

    void close();

    void contestSuccess(ShowVideoModel video);
}

package com.jinglz.app.ui.winnings;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;
import com.jinglz.app.ui.base.BaseView;

import java.util.List;

/**
 * an interface to hide progress bar
 * open a video with specified id
 * and to set list of ranking contains data in ContestLeadershipRecord
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface WinningsView extends BaseView {

    void setRanking(List<ContestLeadershipRecord> records);

    void hideProgressBar();

    void openVideo(String videoId, String contestId);

}

package com.jinglz.app.ui.entrieswinnings;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.ui.base.BaseView;

import java.util.List;

/**
 * @openShareChooser to open share chooses
 * @initCoinsLayout to initialize coin layout
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface EntriesAndWinningsView extends BaseView {

    void openShareChooser(String text);

    void initCoinsLayout(List<ContestResult> data);
}

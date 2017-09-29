package com.jinglz.app.data.repositories;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

import com.jinglz.app.R;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.TutorialResponse;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.feed.models.TutorialModel;
import com.jinglz.app.ui.feed.models.TutorialSectionModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

@PerSession
public class TutorialRepository {

    private final Context mContext;
    private final Api mApi;

    private TutorialResponse mTutorialResponse;

    /**
     * Constructs new TutorialRepository with specified context and api.
     * This class is basically used to retrieve tutorials related to video and video history details.
     *
     * @param context for application-specified data
     * @param api for tutorials
     */

    @Inject
    public TutorialRepository(Context context, Api api) {
        mContext = context;
        mApi = api;
    }


    /**
     * This method is used to retrieve available tutorials. it first checks
     * {@code mTutorialResponse} if null or didi not have tutorials, return empty list.
     * otherwise {@link #transformAvailable(TutorialResponse)}.
     *
     * @return list of TutorialSectionModel
     */
    public Single<List<TutorialSectionModel>> getAvailableFeedTutorials() {
        if (mTutorialResponse != null && !mTutorialResponse.hasTutorials()) {
            return Single.just(Collections.emptyList());
        }
        final Single<TutorialResponse> observable =
                mTutorialResponse != null ? Single.just(mTutorialResponse)
                        : (mApi.currentUser()
                                .map(DataResponse::data)
                                .map(UserResponse::tutorial))
                                .doOnSuccess(response -> mTutorialResponse = response);

        return observable.flatMap(this::transformAvailable);
    }

    /**
     * This method is used to retrieve all tutorials by calling {@link #transformAll()}
     * @return list of TutorialSectionModel
     */
    public Single<List<TutorialSectionModel>> getAllFeedTutorials() {
        return transformAll();
    }

    /**
     * This method is used to check for the video rules if they exists.
     *
     * @return boolean value
     */
    public Single<Boolean> videoRulesViewed() {
        if (mTutorialResponse != null) {
            return Single.just(mTutorialResponse.isRules());
        }
        return mApi.currentUser()
                .map(DataResponse::data)
                .map(UserResponse::tutorial)
                .doOnSuccess(response -> mTutorialResponse = response)
                .map(TutorialResponse::isRules);
    }

    /**
     * This method is used to set flag as true if {@link Api#completeTutorialAfterVideo()}.
     * it is used to retrieve complete tutorial after video.
     *
     * @return Completable object
     */
    public Completable completeTutorialAfterVideo() {
        return mTutorialResponse != null && mTutorialResponse.isAfterVideo() ?
                Completable.complete()
                : mApi.completeTutorialAfterVideo()
                        .doOnSuccess(success -> {
                            if (mTutorialResponse == null) {
                                mTutorialResponse = new TutorialResponse();
                            }
                            mTutorialResponse.setAfterVideo(true);
                        })
                        .toCompletable();
    }

    /**
     * This method is used to retrieve complete tutorial details by using {@link Api#completeTutorialDetails()}
     * it will set {@link TutorialResponse#setDetails(boolean)} true on success
     *
     * @return Completable object
     */
    public Completable completeTutorialDetails() {
        return mTutorialResponse != null && mTutorialResponse.isDetails() ?
                Completable.complete()
                : mApi.completeTutorialDetails()
                        .doOnSuccess(success -> {
                            if (mTutorialResponse == null) {
                                mTutorialResponse = new TutorialResponse();
                            }
                            mTutorialResponse.setDetails(true);
                        })
                        .toCompletable();
    }
    /**
     * This method is used to retrieve complete tutorial history by using {@link Api#completeTutorialHistory()}
     * it will set {@link TutorialResponse#setHistory(boolean)} true on success
     *
     * @return Completable object
     */
    public Completable completeTutorialHistory() {
        return mTutorialResponse != null && mTutorialResponse.isHistory() ?
                Completable.complete()
                : mApi.completeTutorialHistory()
                        .doOnSuccess(success -> {
                            if (mTutorialResponse == null) {
                                mTutorialResponse = new TutorialResponse();
                            }
                            mTutorialResponse.setHistory(true);
                        })
                        .toCompletable();
    }

    /**
     * This method is used to retrieve complete video rules by using {@link Api#completeTutorialVideo()}
     * it will set {@link TutorialResponse#setRules(boolean)} true on success
     *
     * @return Completable object
     */
    public Completable completeVideoRules() {
        return mTutorialResponse != null && mTutorialResponse.isRules() ?
                Completable.complete()
                : mApi.completeTutorialVideo()
                        .doOnSuccess(success -> {
                            if (mTutorialResponse == null) {
                                mTutorialResponse = new TutorialResponse();
                            }
                            mTutorialResponse.setRules(true);
                        })
                        .toCompletable();
    }
    /**
     * This method is used to retrieve complete feed tutorials.
     *
     * @return Completable object
     */
    public Completable completeAllFeedTutorials() {
        return Single.zip(completeTutorialDetails().toSingleDefault(""),
                          completeTutorialAfterVideo().toSingleDefault(""),
                          completeTutorialHistory().toSingleDefault(""),
                          (s, s2, s3) -> true)
                .toCompletable();
    }

    /**
     * This method is used to keep track of the balance coins
     *
     * @return TutorialModel instance
     */
    public TutorialModel getBalanceTutorial() {
        final String text = mContext.getString(R.string.tutorial_balance);
        final String textCoinBalance = mContext.getString(R.string.text_coin_balance);
        final String textCoinCosts = mContext.getString(R.string.text_coin_costs);

        final SpannableStringBuilder builder = new SpannableStringBuilder(text);
        final String coinPosition = "%coin%";
        final ImageSpan coinSpan = new ImageSpan(mContext, R.drawable.ic_coin);
        builder.setSpan(coinSpan, text.indexOf(coinPosition), text.indexOf(coinPosition) + coinPosition.length(),
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(1.25f), text.indexOf(textCoinBalance),
                        text.indexOf(textCoinBalance) + textCoinBalance.length(),
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(1.25f), text.indexOf(textCoinCosts),
                        text.indexOf(textCoinCosts) + textCoinCosts.length(),
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_BALANCE, builder);
    }

    /**
     * Provide details regarding timer.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getTimerTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_TIMER,
                                    new SpannableString(mContext.getString(R.string.tutorial_timer)));
    }

    /**
     * Provide details regarding jackPot.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getJackPotTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_JACK_POT,
                                    mContext.getString(R.string.tutorial_jack_pot));
    }

    /**
     * Provide details regarding drawing.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getDrawingTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_DRAWING,
                                    mContext.getString(R.string.tutorial_drawing));
    }

    /**
     * Provide details regarding views.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getViewedTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_VIEWED,
                                    mContext.getString(R.string.tutorial_viewed));
    }

    /**
     * Provide tutorial for re-watching videos.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getReWatchTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_RE_WATCH,
                                    mContext.getString(R.string.tutorial_re_watch));
    }

    /**
     * Provide details regarding timer to be viewed.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getViewedTimerTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_VIEWED_TIMER,
                                    mContext.getString(R.string.tutorial_viewed_timer));
    }
    /**
     * Provide details regarding positions.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getPositionTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_POSITION,
                                    mContext.getString(R.string.tutorial_position));
    }
    /**
     * Provide details regarding coins.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getCoinsTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_COINS,
                                    mContext.getString(R.string.tutorial_coins));
    }
    /**
     * Provide more details about application and its available features.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getLearnMoreTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_LEARN_MORE,
                                    mContext.getString(R.string.tutorial_learn_more));
    }
    /**
     * Provide details regarding time to draw.
     *
     * @return TutorialModel instance
     */
    public TutorialModel getDrawingTimeTutorial() {
        return TutorialModel.create(TutorialModel.TUTORIAL_TYPE_DRAWING_TIME,
                                    mContext.getString(R.string.tutorial_drawing_time));
    }

    /**
     * This method is used to transform the specified response. it create a list of
     * {@link TutorialSectionModel}, names as {@code sections} and return.
     *
     * @param response that needed to transform
     * @return list of TutorialSectionModel
     */
    private Single<List<TutorialSectionModel>> transformAvailable(TutorialResponse response) {
        final List<TutorialSectionModel> sections = new ArrayList<>();

        if (!response.isDetails()) {
            sections.add(TutorialSectionModel.create(VideoSectionModel.VIDEOS_NEW,
                                                     Arrays.asList(getBalanceTutorial(),
                                                                   getTimerTutorial(),
                                                                   getJackPotTutorial(),
                                                                   getDrawingTutorial(),
                                                                   getLearnMoreTutorial(),
                                                                   getDrawingTimeTutorial())));
        }

        if (!response.isAfterVideo()) {
            sections.add(TutorialSectionModel.create(VideoSectionModel.VIDEOS_ENTRY,
                                                     Arrays.asList(getViewedTimerTutorial(),
                                                                   getReWatchTutorial(),
                                                                   getViewedTutorial())));
        }

        if (!response.isHistory()) {
            sections.add(TutorialSectionModel.create(VideoSectionModel.VIDEOS_HISTORY,
                                                     Arrays.asList(getCoinsTutorial(),
                                                                   getPositionTutorial())));
        }

        return Single.just(sections);
    }

    /**
     * This method is used to transform the whole response. it create a list of
     * {@link TutorialSectionModel}, names as {@code sections} and return.
     *
     * @return list of TutorialSectionModel
     */
    private Single<List<TutorialSectionModel>> transformAll() {
        final List<TutorialSectionModel> sections = new ArrayList<>();

        sections.add(TutorialSectionModel.create(VideoSectionModel.VIDEOS_NEW,
                                                 Arrays.asList(getBalanceTutorial(),
                                                               getTimerTutorial(),
                                                               getJackPotTutorial(),
                                                               getDrawingTutorial(),
                                                               getLearnMoreTutorial(),
                                                               getDrawingTimeTutorial())));

        sections.add(TutorialSectionModel.create(VideoSectionModel.VIDEOS_ENTRY,
                                                 Arrays.asList(getViewedTimerTutorial(),
                                                               getReWatchTutorial(),
                                                               getViewedTutorial())));

        sections.add(TutorialSectionModel.create(VideoSectionModel.VIDEOS_HISTORY,
                                                 Arrays.asList(getCoinsTutorial(),
                                                               getPositionTutorial())));

        return Single.just(sections);
    }
}

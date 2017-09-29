package com.jinglz.app.business.tutorial;

import com.jinglz.app.data.repositories.TutorialRepository;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.feed.models.TutorialSectionModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;

import java.util.List;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class TutorialInteractor {

    private final TutorialRepository mTutorialRepository;

    /**
     * Construct new TutorialInteractor with specified tutorialRepository
     * @param tutorialRepository used to keep track of tutorial information including its
     *                           history, detail, rules, history etc.
     */
    @Inject
    public TutorialInteractor(TutorialRepository tutorialRepository) {
        mTutorialRepository = tutorialRepository;
    }

    public Single<List<TutorialSectionModel>> getAvailableFeedTutorials() {
        return mTutorialRepository.getAvailableFeedTutorials()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<TutorialSectionModel>> getAllFeedTutorials() {
        return mTutorialRepository.getAllFeedTutorials()
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> videoRulesViewed() {
        return mTutorialRepository.videoRulesViewed()
                .subscribeOn(Schedulers.io());
    }

    /**
     * This method takes sectionType as an input parameter and perform operation according
     * to that section type. throws new RuntimeException if {@code sectionType} is invalid
     *
     * @param sectionType number that hold section type
     * @return Completable object
     */
    public Completable completeSection(@VideoSectionModel.SectionType int sectionType) {
        switch (sectionType) {
            case VideoSectionModel.VIDEOS_ENTRY:
                return mTutorialRepository.completeTutorialAfterVideo()
                        .subscribeOn(Schedulers.io());
            case VideoSectionModel.VIDEOS_HISTORY:
                return mTutorialRepository.completeTutorialHistory()
                        .subscribeOn(Schedulers.io());
            case VideoSectionModel.VIDEOS_NEW:
                return mTutorialRepository.completeTutorialDetails()
                        .subscribeOn(Schedulers.io());
        }
        throw new RuntimeException("Section type is invalid");
    }

    public Completable completeAllSections() {
        return mTutorialRepository.completeAllFeedTutorials()
                .subscribeOn(Schedulers.io());
    }

    public Completable completeVideoRules() {
        return mTutorialRepository.completeVideoRules()
                .subscribeOn(Schedulers.io());
    }
}

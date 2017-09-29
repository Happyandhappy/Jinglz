package com.jinglz.app.business.showvideo;

import android.support.annotation.Nullable;

import com.google.android.gms.vision.face.Face;
import com.jinglz.app.Constants;
import com.jinglz.app.config.FaceDetectionConfig;
import com.jinglz.app.data.network.models.contest.TakePartInContestRequest;
import com.jinglz.app.data.repositories.ContestRepository;
import com.jinglz.app.data.repositories.DeviceRepository;
import com.jinglz.app.data.repositories.FaceRepository;
import com.jinglz.app.data.repositories.VideoRepository;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.showvideo.models.ShowVideoModel;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class ShowVideoInteractor {

    private static final String TAG = "ShowVideoInteractor";

    private final VideoRepository mVideoRepository;
    private final DeviceRepository mDeviceRepository;
    private final FaceRepository mFaceRepository;
    private final ContestRepository mContestRepository;

    @Inject
    public ShowVideoInteractor(VideoRepository videoRepository, DeviceRepository deviceRepository,FaceRepository faceRepository, ContestRepository contestRepository) {
        mVideoRepository = videoRepository;
        mDeviceRepository = deviceRepository;
        mFaceRepository = faceRepository;
        mContestRepository = contestRepository;
    }

    /**
     * method that takes videoId and contestId as its input parameter. it is used to
     * retrieve video according to the video id by calling {@link VideoRepository#getVideo(String)}.
     * and retrieve contest according to contest id by calling {@link ContestRepository#getContest(String)}
     *
     * @param videoId string variable contains video id
     * @param contestId string variable contains contest id
     * @return Single object of ShowVideoModel
     */
    public Single<ShowVideoModel> getVideo(String videoId, String contestId) {
        return Single.zip(mVideoRepository.getVideo(videoId), mContestRepository.getContest(contestId),
                          ShowVideoModel::create)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> lowVolumeLevelObservable() {
        return mDeviceRepository.volumeLevelObservable()
                .map(volumeLevel -> volumeLevel < Constants.MIN_VOLUME_LEVEL_PERCENT)
                .distinctUntilChanged();
    }

    public Observable<Boolean> badFaceObservable() {
        return mFaceRepository.getFaces()
                .subscribeOn(Schedulers.io())
                .map(this::checkFace)
                .map(good -> !good)
                .distinctUntilChanged();
    }

    /**
     * method with specified video to creating new instance of TakePartInContestRequest by passing id
     * and contestId of {@code video}
     *
     * @param video {@link ShowVideoModel} object to retrieve id and contestId
     * @return Completable object
     */
    public Completable takePartInContest(ShowVideoModel video) {
        return mContestRepository.takePartInContest(TakePartInContestRequest.create(video.id(), video.contestId())).subscribeOn(Schedulers.io());
    }

    private boolean checkFace(@Nullable Face face) {
        if (face == null) {
            return false;
        }
        final float y = face.getEulerY();
        final float r = face.getEulerZ();

        return y > FaceDetectionConfig.MIN_FACE_Y && y < FaceDetectionConfig.MAX_FACE_Y && r > FaceDetectionConfig.MIN_FACE_R && r < FaceDetectionConfig.MAX_FACE_R;
    }
}

package com.jinglz.app.data.repositories;

import android.content.Context;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.jinglz.app.injection.session.PerSession;

import java.io.IOException;

import javax.inject.Inject;

import rx.Emitter;
import rx.Observable;

@PerSession
public class FaceRepository {

    private static final String TAG = "FaceRepository";

    private final Context mContext;

    /**
     * Constructs new FaceRepository with specified context. It is used o keep track the
     * face detection process.
     *
     * @param context to access application-specific resources
     */
    @Inject
    public FaceRepository(Context context) {
        mContext = context;
    }

    /**
     * This method is used for face detection. {@code detector} is an instance of
     * {@link FaceDetector} which is used to process detection.
     * it calls {@link LargestFaceFocusingProcessor} to set processor for update detection.
     * if {@code detector} is not operational, it throws a runtime exception.
     * {@code cameraSource} will be used to detect face.
     *
     * @return instance of Face
     * @throws RuntimeException if {@code detector} is not in operation state.
     */
    public Observable<Face> getFaces() {
        return Observable.<Face>fromEmitter(emitter -> {
            final FaceDetector detector = new FaceDetector.Builder(mContext)
                    .setMode(FaceDetector.ACCURATE_MODE)
                    .setProminentFaceOnly(true)
                    .setTrackingEnabled(true)
                    .build();

            detector.setProcessor(new LargestFaceFocusingProcessor(detector, new Tracker<Face>() {
                @Override
                public void onUpdate(Detector.Detections<Face> detections, Face face) {
                    super.onUpdate(detections, face);
                    emitter.onNext(face);
                }

                @Override
                public void onMissing(Detector.Detections<Face> detections) {
                    super.onMissing(detections);
                    emitter.onNext(null);
                }
            }));

            if (!detector.isOperational()) {
                emitter.onError(new RuntimeException("Face detector dependencies are not yet available."));
            }

            final CameraSource cameraSource = new CameraSource.Builder(mContext, detector)
                    .setRequestedPreviewSize(640, 480)
                    .setFacing(CameraSource.CAMERA_FACING_FRONT)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(30.0f)
                    .build();
            try {
                //noinspection MissingPermission
                cameraSource.start();
            } catch (IOException e) {
                emitter.onError(e);
            }

            emitter.setCancellation(() -> {
                cameraSource.release();
                detector.release();
            });

        }, Emitter.BackpressureMode.DROP)
                .distinctUntilChanged();
    }
}

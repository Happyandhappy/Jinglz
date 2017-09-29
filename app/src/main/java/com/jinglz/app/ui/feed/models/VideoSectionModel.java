package com.jinglz.app.ui.feed.models;

import android.support.annotation.IntDef;

import com.google.auto.value.AutoValue;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@AutoValue
public abstract class VideoSectionModel<T extends VideoItemModel> {

    @Retention(SOURCE)
    @IntDef({VIDEOS_ENTRY, VIDEOS_NEW, VIDEOS_HISTORY})
    public @interface SectionType {

    }

    public static final int VIDEOS_ENTRY = 0;
    public static final int VIDEOS_NEW = 1;
    public static final int VIDEOS_HISTORY = 2;

    /**
     * Method with specified type, title and items used to construct new AutoValue_VideoSectionModel.
     *
     * @param type contains type of the video {@see VIDEOS_ENTRY, VIDEOS_NEW, VIDEOS_HISTORY}
     * @param title string variable contains title of the video.
     * @param items List of collections
     * @param <T>
     * @return AutoValue_VideoSectionModel instance
     */
    public static <T extends VideoItemModel> VideoSectionModel create(@SectionType int type,
                                                                      String title,
                                                                      List<T> items) {
        return new AutoValue_VideoSectionModel(type, title, items);
    }

    @SectionType
    public abstract int type();

    public abstract String title();

    public abstract List<T> items();

}

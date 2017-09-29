package com.jinglz.app.ui.feed.models;

import android.support.annotation.IntDef;
import android.text.Spannable;

import com.google.auto.value.AutoValue;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@AutoValue
public abstract class TutorialModel {

    @Retention(SOURCE)
    @IntDef({TUTORIAL_TYPE_BALANCE, TUTORIAL_TYPE_TIMER,
            TUTORIAL_TYPE_JACK_POT, TUTORIAL_TYPE_DRAWING,
            TUTORIAL_TYPE_VIEWED, TUTORIAL_TYPE_RE_WATCH,
            TUTORIAL_TYPE_VIEWED_TIMER, TUTORIAL_TYPE_POSITION,
            TUTORIAL_TYPE_COINS, TUTORIAL_TYPE_LEARN_MORE,
            TUTORIAL_TYPE_DRAWING_TIME})
    public @interface TutorialType {

    }

    public static final int TUTORIAL_TYPE_BALANCE = 0;
    public static final int TUTORIAL_TYPE_TIMER = 1;
    public static final int TUTORIAL_TYPE_JACK_POT = 2;
    public static final int TUTORIAL_TYPE_DRAWING = 3;
    public static final int TUTORIAL_TYPE_VIEWED = 4;
    public static final int TUTORIAL_TYPE_RE_WATCH = 5;
    public static final int TUTORIAL_TYPE_VIEWED_TIMER = 6;
    public static final int TUTORIAL_TYPE_POSITION = 7;
    public static final int TUTORIAL_TYPE_COINS = 8;
    public static final int TUTORIAL_TYPE_LEARN_MORE = 9;
    public static final int TUTORIAL_TYPE_DRAWING_TIME = 10;

    /**
     * it is used to construct new AutoValue_TutorialModel with specified type and text.
     *
     * @param type
     * @param text
     * @return AutoValue_TutorialModel instance
     */
    public static TutorialModel create(int type, CharSequence text) {
        return new AutoValue_TutorialModel(type, text);
    }

    @TutorialType
    public abstract int type();

    public abstract CharSequence text();

}

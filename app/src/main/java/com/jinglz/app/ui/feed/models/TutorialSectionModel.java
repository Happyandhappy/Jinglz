package com.jinglz.app.ui.feed.models;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class TutorialSectionModel {

    /**
     * it is used to construct new AutoValue_TutorialSectionModel with specified type and tutorials.
     *
     * @param type
     * @param tutorials
     * @return AutoValue_TutorialSectionModel instance
     */
    public static TutorialSectionModel create(@VideoSectionModel.SectionType int type, List<TutorialModel> tutorials) {
        return new AutoValue_TutorialSectionModel(type, tutorials);
    }

    @VideoSectionModel.SectionType
    public abstract int type();

    public abstract List<TutorialModel> tutorials();

}

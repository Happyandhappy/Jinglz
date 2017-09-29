package com.jinglz.app.data.network.models.update;

import com.github.javiersantos.appupdater.objects.Update;

public class UpdateModel {

    private boolean inNeedToUpdate;
    private Update update;

    /**
     * constructs new UpdateModel with specified inNeedToUpdate and update.
     *
     * @param inNeedToUpdate to check if there is need to update
     * @param update that contains information of application such as version, versionCode,
     *               releaseNotes and apk link
     */
    public UpdateModel(boolean inNeedToUpdate, Update update) {
        this.inNeedToUpdate = inNeedToUpdate;
        this.update = update;
    }

    /**
     * This method is used to check if there is need to update
     *
     * @return boolean variable
     */
    public boolean isInNeedToUpdate() {
        return inNeedToUpdate;
    }

    /**
     * This method is used to return information of application such as version, versionCode,
     *               releaseNotes and apk link
     * @return Update object
     */
    public Update getUpdate() {
        return update;
    }
}

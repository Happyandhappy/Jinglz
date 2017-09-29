package com.jinglz.app.data.network.models;

public class TutorialResponse {

    /**
     * returns new TutorialResponse object by constructing
     * new TutorialResponse with specified afterVideo, details, history and rules.
     *
     * @param afterVideo boolean value to check tutorial is after video or before
     * @param details boolean value to check if detail is there.
     * @param history boolean value to check if history is there
     * @param rules boolean value to check if rules are there
     * @return Returns a new instance of TutorialResponse class
     */
    public static TutorialResponse create(boolean afterVideo, boolean details, boolean history, boolean rules) {
        return new TutorialResponse(afterVideo, details, history, rules);
    }

    private boolean afterVideo;
    private boolean details;
    private boolean history;
    private boolean rules;

    public TutorialResponse() {
    }

    /**
     * Constructs new TutorialResponse with specified parameters named as afterVideo,
     * details, history and rules.
     *
     * @param afterVideo boolean value to check tutorial is after video or before
     * @param details boolean value to check if detail is there.
     * @param history boolean value to check if history is there
     * @param rules boolean value to check if rules are there
     */
    public TutorialResponse(boolean afterVideo, boolean details, boolean history, boolean rules) {
        this.afterVideo = afterVideo;
        this.details = details;
        this.history = history;
        this.rules = rules;
    }

    public boolean hasTutorials() {
        return !afterVideo || !details || !history || !rules;
    }

    public boolean isAfterVideo() {
        return afterVideo;
    }

    public void setAfterVideo(boolean afterVideo) {
        this.afterVideo = afterVideo;
    }

    public boolean isDetails() {
        return details;
    }

    public void setDetails(boolean details) {
        this.details = details;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public boolean isRules() {
        return rules;
    }

    public void setRules(boolean rules) {
        this.rules = rules;
    }
}
package com.jinglz.app.business.analytics;

public enum Event {
    // Parameters : Event name, Mixpanel, Firebase
    LOGIN_WITH_PASSWORD("Login with password", false, true),
    LOGOUT("Logout", false, true),
    VIDEO_LIST_REQUEST("Video List Request", false, true),
    VIDEO_LIST_RESPONSE("Video List Response", false, true),
    REFRESH("Pull to refresh", false, true),
    LEAVE_APPLICATION("Leave application", false, true),
    OPEN_APPLICATION("Open application", false, true),
    NOTIFICATION_SENT("Notification sent", false, true),
    UNAUTHORIZED("Unauthorized", false, true),
    VIDEO_PAUSED_TO_BUFFER("Video paused to buffer", false, true),
    VIDEO_STARTED("Video Started", false, true),
    ABANDON_VIDEO("Abandon video", true, true),
    FACE_NOT_DETECTED("Face not detected", false, true),
    FINISH_WATCHING_VIDEO("Finish watching video", true, true),
    GAZE_NOT_DETECTED("Gaze not detected", false, true),
    VIDEO_PAUSE("Video pause", false, true),
    LOGIN_WITH_FACEBOOK("Login with Facebook", false, true),
    LOGIN_WITH_GOOGLE("Login with Google", false, true),
    NOTIFICATION_BOUNCED("Notification Bounced", false, true),
    REGISTER_WITH_FACEBOOK("Register with Facebook", true, true),
    REGISTER_WITH_PASSWORD("Register with Password", true, true),
    START_PLAYING_VIDEO("Start playing video", true, true),
    RESET_PASSWORD("Reset password", false, true),
    SESSION_EXPIRED("Session expired", false, true),
    SUPPORT_TICKET_SENT("Support Ticket Sent", true, true),
    VERIFIED_PHONE_NUMBER("Verified Phone Number", false, true),
    COIN_REDEMPTION("COIN Redemption", true, true);

    private final String value;
    private final boolean logMixpanel;
    private final boolean logFirebase;

    Event(final String value, final boolean mixpanel, final  boolean firebase) {
        this.value = value;
        this.logMixpanel = mixpanel;
        this.logFirebase = firebase;
    }

    public String getValue() {
        return value;
    }

    public boolean isLogMixpanel() {
        return logMixpanel;
    }

    public boolean isLogFirebase() {
        return logFirebase;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}

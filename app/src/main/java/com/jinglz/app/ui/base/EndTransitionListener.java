package com.jinglz.app.ui.base;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.TransitionValues;

/**
 * abstract class that implementing Transition.TransitionListener, A transition listener
 * receives notifications from a transition. Notifications indicate transition lifecycle events.
 */
public abstract class EndTransitionListener implements Transition.TransitionListener {

    /**
     * Notify about the start of the transition.
     *
     * @param transition The started transition.
     */
    @Override
    public void onTransitionStart(Transition transition) {

    }

    /**
     * Notify about the cancellation of the transition.
     *
     * @param transition The transition which was paused.
     */
    @Override
    public void onTransitionCancel(Transition transition) {

    }

    /**
     * Notify when a transition is paused.
     *
     * @param transition The transition which was paused.
     */
    @Override
    public void onTransitionPause(Transition transition) {

    }

    /**
     * Notify when a transition is resumed.
     *
     * @param transition The transition which was paused.
     */
    @Override
    public void onTransitionResume(Transition transition) {

    }
}

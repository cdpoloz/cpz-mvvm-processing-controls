package com.cpz.processing.controls.controls;

import com.cpz.processing.controls.core.input.KeyboardEvent;

/**
 * Optional keyboard routing contract for public control facades.
 *
 * @author CPZ
 */
public interface KeyboardRoutableControl extends Control {
    /**
     * Routes a normalized keyboard event into this control.
     *
     * @param event keyboard event to route
     */
    void handleKeyboardEvent(KeyboardEvent event);

    /**
     * Returns whether this control should receive the keyboard event.
     *
     * @param event keyboard event to inspect
     * @return {@code true} when the event should stop lower input layers
     */
    boolean canConsumeKeyboardEvent(KeyboardEvent event);
}

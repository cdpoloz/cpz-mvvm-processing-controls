package com.cpz.processing.controls.controls;

import com.cpz.processing.controls.core.input.PointerEvent;

/**
 * Optional pointer routing contract for public control facades.
 *
 * <p>Coordinates are expressed in the control parent's coordinate space. For
 * top-level controls this is sketch space. For controls inside a container this
 * is the container's local space.</p>
 *
 * @author CPZ
 */
public interface PointerRoutableControl extends Control {
    /**
     * Routes a normalized pointer event into this control.
     *
     * @param event pointer event in the parent coordinate space
     */
    void handlePointerEvent(PointerEvent event);

    /**
     * Returns whether this control owns the pointer event geometry.
     *
     * @param event pointer event in the parent coordinate space
     * @return {@code true} when the event should stop lower input layers
     */
    boolean canConsumePointerEvent(PointerEvent event);
}

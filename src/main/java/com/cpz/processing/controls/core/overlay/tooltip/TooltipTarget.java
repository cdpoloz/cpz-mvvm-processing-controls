package com.cpz.processing.controls.core.overlay.tooltip;

/**
 * Generic target that can expose a tooltip.
 *
 * <p>This abstraction intentionally does not depend on the public
 * {@code Control} facade. A target can be a library control, a manually drawn
 * rectangle, a PImage region, or any other sketch-space element.</p>
 *
 * @author CPZ
 */
public interface TooltipTarget {
    /**
     * Returns current target bounds in sketch coordinates.
     *
     * @return target bounds
     */
    TooltipBounds getTooltipBounds();

    /**
     * Returns the tooltip assigned to this target.
     *
     * @return tooltip, or {@code null} when absent
     */
    Tooltip getTooltip();

    /**
     * Returns whether this target participates in tooltip hit testing.
     *
     * @return {@code true} by default
     */
    default boolean isTooltipTargetVisible() {
        return true;
    }

    /**
     * Returns whether this target can show its tooltip.
     *
     * @return {@code true} by default
     */
    default boolean isTooltipTargetEnabled() {
        return true;
    }
}

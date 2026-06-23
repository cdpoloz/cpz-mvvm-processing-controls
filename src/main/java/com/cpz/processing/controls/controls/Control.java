package com.cpz.processing.controls.controls;

/**
 * Minimal public contract shared by the closed control facades.
 *
 * <p>This interface belongs to the public facade layer. It exposes only the
 * transversal surface that is stable across all controls: identity, drawing,
 * visibility, enabled state, and positioning.</p>
 *
 * <p>Input, styling, values, text, selection, overlay lifecycle, and other
 * control-specific concerns intentionally remain on the concrete facade types.
 * {@code Control} does not expose {@code View}, {@code ViewModel}, input
 * adapters, or other MVVM internals.</p>
 *
 * @author CPZ
 */
public interface Control {
    /**
     * Returns the stable code used to identify this control in composed maps.
     *
     * @return control code
     */
    String getCode();

    /**
     * Draws the control using its internal view pipeline.
     */
    void draw();

    /**
     * Returns whether the control accepts interaction and renders as enabled.
     *
     * @return {@code true} when enabled
     */
    boolean isEnabled();

    /**
     * Updates the enabled state exposed by the public facade.
     *
     * @param enabled new enabled state
     */
    void setEnabled(boolean enabled);

    /**
     * Returns whether the control participates in rendering.
     *
     * @return {@code true} when visible
     */
    boolean isVisible();

    /**
     * Updates the visible state exposed by the public facade.
     *
     * @param visible new visible state
     */
    void setVisible(boolean visible);

    /**
     * Moves the control without exposing its internal view object.
     *
     * @param x new x position
     * @param y new y position
     */
    void setPosition(float x, float y);
}

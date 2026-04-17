package com.cpz.processing.controls.controls;

/**
 * Minimal public contract shared by the closed control facades.
 *
 * <p>This interface belongs to the facade layer of the framework. It exposes
 * only the small transversal surface that is stable across the public controls
 * and does not expose MVVM internals.
 *
 * <p>This type is distinct from {@code ControlView}, which belongs to the
 * internal MVVM view layer.
 */
public interface Control {
    String getCode();

    void draw();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isVisible();

    void setVisible(boolean visible);

    void setPosition(float x, float y);
}

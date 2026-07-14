package com.cpz.processing.controls.controls.panel.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;

import java.util.Objects;

/**
 * JSON config for a panel root control.
 *
 * <p>The config stores structural values plus an optional
 * {@link PanelStyleConfig}. Child composition is intentionally not represented
 * here; panel children are still composed through the runtime API.</p>
 *
 * @author CPZ
 */
public final class PanelConfig {
    private final String code;
    private final ControlBounds bounds;
    private final boolean enabled;
    private final boolean visible;
    private final PanelStyleConfig style;

    public PanelConfig(String code, ControlBounds bounds, boolean enabled, boolean visible) {
        this(code, bounds, enabled, visible, null);
    }

    public PanelConfig(String code, ControlBounds bounds, boolean enabled, boolean visible, PanelStyleConfig style) {
        this.code = Objects.requireNonNull(code, "code");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
    }

    public String getCode() {
        return this.code;
    }

    public ControlBounds getBounds() {
        return this.bounds;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public PanelStyleConfig getStyle() {
        return this.style;
    }
}

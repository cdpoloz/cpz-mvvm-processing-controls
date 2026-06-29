package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import processing.core.PFont;

/**
 * Reusable tooltip data and styling component.
 *
 * <p>The instance is independent from controls. It can be attached to any
 * {@link TooltipTarget}.</p>
 *
 * @author CPZ
 */
public final class Tooltip {
    private String text;
    private boolean enabled = true;
    private final TooltipStyleConfig styleConfig;

    /**
     * Creates an empty tooltip.
     */
    public Tooltip() {
        this("");
    }

    /**
     * Creates a tooltip with text.
     *
     * @param text tooltip text
     */
    public Tooltip(String text) {
        this(text, new TooltipStyleConfig());
    }

    /**
     * Creates a tooltip with text and style.
     *
     * @param text tooltip text
     * @param styleConfig style config
     */
    public Tooltip(String text, TooltipStyleConfig styleConfig) {
        this.text = text == null ? "" : text;
        this.styleConfig = styleConfig == null ? new TooltipStyleConfig() : styleConfig;
    }

    public String getText() {
        return this.text;
    }

    public Tooltip setText(String text) {
        this.text = text == null ? "" : text;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Tooltip setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public TooltipStyleConfig getStyleConfig() {
        return this.styleConfig;
    }

    public Tooltip setFont(PFont font) {
        this.styleConfig.font = font;
        return this;
    }

    public Tooltip setTextSize(float size) {
        this.styleConfig.textSize = size;
        return this;
    }

    public Tooltip setBackgroundColor(int argb) {
        this.styleConfig.backgroundOverride = argb;
        return this;
    }

    public Tooltip setTextColor(int argb) {
        this.styleConfig.textOverride = argb;
        return this;
    }

    public Tooltip setBorderColor(int argb) {
        this.styleConfig.borderOverride = argb;
        return this;
    }

    public Tooltip setPadding(float padding) {
        this.styleConfig.textPadding = Math.max(0.0F, padding);
        return this;
    }

    public Tooltip setOffset(float offset) {
        this.styleConfig.offset = Math.max(0.0F, offset);
        return this;
    }

    public Tooltip setCornerRadius(float cornerRadius) {
        this.styleConfig.cornerRadius = Math.max(0.0F, cornerRadius);
        return this;
    }
}

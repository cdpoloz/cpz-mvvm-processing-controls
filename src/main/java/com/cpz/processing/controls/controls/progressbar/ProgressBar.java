package com.cpz.processing.controls.controls.progressbar;

import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.util.ControlCode;
import java.util.Objects;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Public non-interactive horizontal progress bar facade.
 *
 * @author CPZ
 */
public final class ProgressBar implements ParentSizeAwareControl, TooltipAttachable {
    public static final int DEFAULT_TRACK_COLOR = 0xFF30343A;
    public static final int DEFAULT_FILL_COLOR = 0xFF2F80ED;
    public static final int DEFAULT_STROKE_COLOR = 0xFF1F2328;

    private final PApplet sketch;
    private final String code;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private Float parentWidth;
    private Float parentHeight;
    private float x;
    private float y;
    private float width;
    private float height;
    private float min;
    private float max = 1.0F;
    private float value;
    private int trackColor = DEFAULT_TRACK_COLOR;
    private int fillColor = DEFAULT_FILL_COLOR;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private float strokeWeight = 1.0F;
    private boolean enabled = true;
    private boolean visible = true;

    public ProgressBar(PApplet sketch, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("progressbar"), x, y, width, height);
    }

    public ProgressBar(PApplet sketch, String code, float x, float y, float width, float height) {
        this(sketch, code, ControlBounds.absolute(x, y, width, height));
    }

    public ProgressBar(PApplet sketch, ControlBounds bounds) {
        this(sketch, ControlCode.auto("progressbar"), bounds);
    }

    public ProgressBar(PApplet sketch, String code, ControlBounds bounds) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.code = Objects.requireNonNull(code, "code");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.tooltipSupport = new TooltipSupport(this::currentTooltipBounds, this::isVisible);
        this.applyResolvedBounds();
    }

    public void draw() {
        if (!this.visible) {
            return;
        }

        this.applyResolvedBounds();
        float resolvedWidth = Math.max(0.0F, this.width);
        float resolvedHeight = Math.max(0.0F, this.height);
        float fillWidth = resolvedWidth * this.getProgress();

        this.sketch.pushStyle();
        try {
            this.sketch.noStroke();
            this.sketch.fill(this.trackColor);
            this.sketch.rect(this.x, this.y, resolvedWidth, resolvedHeight);

            if (fillWidth > 0.0F) {
                this.sketch.fill(this.fillColor);
                this.sketch.rect(this.x, this.y, fillWidth, resolvedHeight);
            }

            if (this.strokeWeight > 0.0F) {
                this.sketch.noFill();
                this.sketch.stroke(this.strokeColor);
                this.sketch.strokeWeight(this.strokeWeight);
                this.sketch.rect(this.x, this.y, resolvedWidth, resolvedHeight);
            } else {
                this.sketch.noStroke();
            }
        } finally {
            this.sketch.popStyle();
        }
    }

    public String getCode() {
        return this.code;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = this.clampToRange(value);
    }

    public float getMin() {
        return this.min;
    }

    public void setMin(float min) {
        this.setRange(min, this.max);
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float max) {
        this.setRange(this.min, max);
    }

    public void setRange(float min, float max) {
        if (min <= max) {
            this.min = min;
            this.max = max;
        } else {
            this.min = max;
            this.max = min;
        }
        this.value = this.clampToRange(this.value);
    }

    public float getProgress() {
        if (this.min == this.max) {
            return this.value >= this.max ? 1.0F : 0.0F;
        }
        return (this.value - this.min) / (this.max - this.min);
    }

    public int getTrackColor() {
        return this.trackColor;
    }

    public void setTrackColor(int color) {
        this.trackColor = color;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(int color) {
        this.fillColor = color;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public void setStrokeWeight(float weight) {
        this.strokeWeight = Math.max(0.0F, weight);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPosition(float x, float y) {
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedBounds();
    }

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedBounds();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedBounds();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedBounds();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedBounds();
    }

    public TooltipBounds getTooltipBounds() {
        this.applyResolvedBounds();
        return this.tooltipSupport.getTooltipBounds();
    }

    public Tooltip getTooltip() {
        return this.tooltipSupport.getTooltip();
    }

    public boolean isTooltipTargetVisible() {
        return this.tooltipSupport.isTooltipTargetVisible();
    }

    public boolean isTooltipTargetEnabled() {
        return this.tooltipSupport.isTooltipTargetEnabled();
    }

    public ProgressBar setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public ProgressBar setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public ProgressBar setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public ProgressBar setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public ProgressBar setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public ProgressBar setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public ProgressBar setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public ProgressBar setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public ProgressBar setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public ProgressBar setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public ProgressBar setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public ProgressBar clearTooltip() {
        this.tooltipSupport.clearTooltip();
        return this;
    }

    private float clampToRange(float rawValue) {
        return Math.max(this.min, Math.min(this.max, rawValue));
    }

    private TooltipBounds currentTooltipBounds() {
        return new TooltipBounds(this.x, this.y, this.width, this.height);
    }

    private void applyResolvedBounds() {
        ResolvedBounds resolvedBounds = this.bounds.resolve(this.parentWidth(), this.parentHeight());
        this.x = resolvedBounds.x();
        this.y = resolvedBounds.y();
        this.width = Math.max(0.0F, resolvedBounds.width());
        this.height = Math.max(0.0F, resolvedBounds.height());
    }

    private float parentWidth() {
        return this.parentWidth != null ? this.parentWidth : this.sketch.width;
    }

    private float parentHeight() {
        return this.parentHeight != null ? this.parentHeight : this.sketch.height;
    }
}

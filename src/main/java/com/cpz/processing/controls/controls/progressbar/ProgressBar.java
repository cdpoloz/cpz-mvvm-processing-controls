package com.cpz.processing.controls.controls.progressbar;

import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.progressbar.style.ProgressBarStyle;
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
 * Public non-interactive progress bar facade.
 *
 * @author CPZ
 */
public final class ProgressBar implements ParentSizeAwareControl, TooltipAttachable {
    public static final int DEFAULT_TRACK_COLOR = ProgressBarStyle.DEFAULT_TRACK_COLOR;
    public static final int DEFAULT_FILL_COLOR = ProgressBarStyle.DEFAULT_FILL_COLOR;
    public static final int DEFAULT_STROKE_COLOR = ProgressBarStyle.DEFAULT_STROKE_COLOR;
    public static final ProgressBarFillDirection DEFAULT_FILL_DIRECTION = ProgressBarStyle.DEFAULT_FILL_DIRECTION;

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
    private ProgressBarStyle style = new ProgressBarStyle();
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

        this.sketch.pushStyle();
        try {
            this.sketch.noStroke();
            this.sketch.fill(this.style.getTrackColor());
            this.sketch.rect(this.x, this.y, resolvedWidth, resolvedHeight);

            this.drawFill(resolvedWidth, resolvedHeight);

            if (this.style.getStrokeWeight() > 0.0F) {
                this.sketch.noFill();
                this.sketch.stroke(this.style.getStrokeColor());
                this.sketch.strokeWeight(this.style.getStrokeWeight());
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
        return this.style.getTrackColor();
    }

    public void setTrackColor(int color) {
        this.style.setTrackColor(color);
    }

    public int getFillColor() {
        return this.style.getFillColor();
    }

    public void setFillColor(int color) {
        this.style.setFillColor(color);
    }

    public int getStrokeColor() {
        return this.style.getStrokeColor();
    }

    public void setStrokeColor(int color) {
        this.style.setStrokeColor(color);
    }

    public float getStrokeWeight() {
        return this.style.getStrokeWeight();
    }

    public void setStrokeWeight(float weight) {
        this.style.setStrokeWeight(weight);
    }

    public ProgressBarFillDirection getFillDirection() {
        return this.style.getFillDirection();
    }

    public void setFillDirection(ProgressBarFillDirection direction) {
        this.style.setFillDirection(direction);
    }

    public ProgressBarStyle getStyle() {
        return this.style;
    }

    public void setStyle(ProgressBarStyle style) {
        this.style = style == null ? new ProgressBarStyle() : style;
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

    private void drawFill(float resolvedWidth, float resolvedHeight) {
        float progress = this.getProgress();
        if (progress <= 0.0F) {
            return;
        }

        float fillX = this.x;
        float fillY = this.y;
        float fillWidth = resolvedWidth;
        float fillHeight = resolvedHeight;

        switch (this.style.getFillDirection()) {
            case RIGHT_TO_LEFT:
                fillWidth = resolvedWidth * progress;
                fillX = this.x + resolvedWidth - fillWidth;
                break;
            case BOTTOM_TO_TOP:
                fillHeight = resolvedHeight * progress;
                fillY = this.y + resolvedHeight - fillHeight;
                break;
            case TOP_TO_BOTTOM:
                fillHeight = resolvedHeight * progress;
                break;
            case LEFT_TO_RIGHT:
            default:
                fillWidth = resolvedWidth * progress;
                break;
        }

        if (fillWidth > 0.0F && fillHeight > 0.0F) {
            this.sketch.fill(this.style.getFillColor());
            this.sketch.rect(fillX, fillY, fillWidth, fillHeight);
        }
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

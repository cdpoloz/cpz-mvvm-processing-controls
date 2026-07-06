package com.cpz.processing.controls.controls.indicator;

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
import processing.core.PShape;

/**
 * Public non-interactive LED-style indicator facade.
 *
 * @author CPZ
 */
public final class Indicator implements ParentSizeAwareControl, TooltipAttachable {
    public static final int DEFAULT_ON_COLOR = 0xFF2ECC71;
    public static final int DEFAULT_OFF_COLOR = 0xFF30343A;
    public static final int DEFAULT_BORDER_COLOR = 0xFF1F2328;

    private final PApplet sketch;
    private final String code;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private Float parentWidth;
    private Float parentHeight;
    private final boolean svgMode;
    private final PShape svgShape;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean on;
    private int onColor = DEFAULT_ON_COLOR;
    private int offColor = DEFAULT_OFF_COLOR;
    private int strokeColor = DEFAULT_BORDER_COLOR;
    private float strokeWeight = 1.0F;
    private boolean enabled = true;
    private boolean visible = true;

    public Indicator(PApplet sketch, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("indicator"), x, y, width, height, null);
    }

    public Indicator(PApplet sketch, String code, float x, float y, float width, float height) {
        this(sketch, code, x, y, width, height, null);
    }

    public Indicator(PApplet sketch, float x, float y, float width, float height, String svgPath) {
        this(sketch, ControlCode.auto("indicator"), x, y, width, height, svgPath);
    }

    public Indicator(PApplet sketch, String code, float x, float y, float width, float height, String svgPath) {
        this(sketch, code, ControlBounds.absolute(x, y, width, height), svgPath);
    }

    public Indicator(PApplet sketch, ControlBounds bounds) {
        this(sketch, ControlCode.auto("indicator"), bounds, null);
    }

    public Indicator(PApplet sketch, ControlBounds bounds, String svgPath) {
        this(sketch, ControlCode.auto("indicator"), bounds, svgPath);
    }

    public Indicator(PApplet sketch, String code, ControlBounds bounds) {
        this(sketch, code, bounds, null);
    }

    public Indicator(PApplet sketch, String code, ControlBounds bounds, String svgPath) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.code = Objects.requireNonNull(code, "code");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.svgMode = svgPath != null;
        this.svgShape = this.svgMode ? loadShape(sketch, svgPath) : null;
        if (this.svgShape != null) {
            this.svgShape.disableStyle();
        }
        this.tooltipSupport = new TooltipSupport(this::currentTooltipBounds, this::isVisible);
        this.applyResolvedBounds();
    }

    public void draw() {
        if (!this.visible) {
            return;
        }

        this.applyResolvedBounds();
        float diameter = Math.max(0.0F, Math.min(this.width, this.height));
        float centerX = this.x + this.width * 0.5F;
        float centerY = this.y + this.height * 0.5F;

        this.sketch.pushStyle();
        try {
            if (this.strokeWeight <= 0.0F) {
                this.sketch.noStroke();
            } else {
                this.sketch.stroke(this.strokeColor);
                this.sketch.strokeWeight(this.strokeWeight);
            }
            this.sketch.fill(this.on ? this.onColor : this.offColor);
            if (this.svgMode) {
                if (this.svgShape != null) {
                    this.sketch.shapeMode(PApplet.CENTER);
                    this.sketch.shape(this.svgShape, centerX, centerY, this.width, this.height);
                }
            } else {
                this.sketch.ellipseMode(PApplet.CENTER);
                this.sketch.circle(centerX, centerY, diameter);
            }
        } finally {
            this.sketch.popStyle();
        }
    }

    public String getCode() {
        return this.code;
    }

    public boolean isOn() {
        return this.on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getOnColor() {
        return this.onColor;
    }

    public void setOnColor(int color) {
        this.onColor = color;
    }

    public int getOffColor() {
        return this.offColor;
    }

    public void setOffColor(int color) {
        this.offColor = color;
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

    public Indicator setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public Indicator setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public Indicator setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public Indicator setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public Indicator setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public Indicator setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public Indicator setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public Indicator setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public Indicator setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public Indicator setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public Indicator setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public Indicator clearTooltip() {
        this.tooltipSupport.clearTooltip();
        return this;
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

    private static PShape loadShape(PApplet sketch, String path) {
        if (sketch == null || path == null || path.isEmpty()) {
            return null;
        }
        PShape shape = sketch.loadShape(path);
        if (shape == null && path.startsWith("data/")) {
            shape = sketch.loadShape(path.substring("data/".length()));
        }
        return shape;
    }
}

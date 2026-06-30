package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import java.util.Objects;
import java.util.function.Supplier;
import processing.core.PFont;

/**
 * Manual rectangular tooltip target for arbitrary Processing content.
 *
 * @author CPZ
 */
public final class TooltipArea implements TooltipTarget {
    private TooltipBounds bounds;
    private Supplier<TooltipBounds> boundsSupplier;
    private Tooltip tooltip;
    private boolean visible = true;
    private boolean enabled = true;

    public TooltipArea(float x, float y, float width, float height) {
        this.bounds = new TooltipBounds(x, y, width, height);
    }

    public TooltipArea(Supplier<TooltipBounds> boundsSupplier) {
        this.boundsSupplier = Objects.requireNonNull(boundsSupplier, "boundsSupplier");
    }

    public TooltipArea setBounds(float x, float y, float width, float height) {
        this.bounds = new TooltipBounds(x, y, width, height);
        this.boundsSupplier = null;
        return this;
    }

    public TooltipArea setBoundsSupplier(Supplier<TooltipBounds> boundsSupplier) {
        this.boundsSupplier = Objects.requireNonNull(boundsSupplier, "boundsSupplier");
        return this;
    }

    public TooltipArea setTooltip(String text) {
        return this.setTooltip(new Tooltip(text));
    }

    public TooltipArea setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public TooltipArea setTooltipText(String text) {
        this.ensureTooltip().setText(text);
        return this;
    }

    public TooltipArea setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.ensureTooltip().setStyle(styleConfig);
        return this;
    }

    public TooltipArea setTooltipFont(PFont font) {
        this.ensureTooltip().setFont(font);
        return this;
    }

    public TooltipArea setTooltipTextSize(float size) {
        this.ensureTooltip().setTextSize(size);
        return this;
    }

    public TooltipArea setTooltipBackgroundColor(int argb) {
        this.ensureTooltip().setBackgroundColor(argb);
        return this;
    }

    public TooltipArea setTooltipTextColor(int argb) {
        this.ensureTooltip().setTextColor(argb);
        return this;
    }

    public TooltipArea setTooltipBorderColor(int argb) {
        this.ensureTooltip().setBorderColor(argb);
        return this;
    }

    public TooltipArea setTooltipPadding(float padding) {
        this.ensureTooltip().setPadding(padding);
        return this;
    }

    public TooltipArea setTooltipOffset(float offset) {
        this.ensureTooltip().setOffset(offset);
        return this;
    }

    public TooltipArea clearTooltip() {
        this.tooltip = null;
        return this;
    }

    public TooltipArea setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public TooltipArea setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public TooltipBounds getTooltipBounds() {
        TooltipBounds current = this.boundsSupplier == null ? this.bounds : this.boundsSupplier.get();
        return current == null ? new TooltipBounds(0.0F, 0.0F, 0.0F, 0.0F) : current;
    }

    public Tooltip getTooltip() {
        return this.tooltip;
    }

    public boolean isTooltipTargetVisible() {
        return this.visible;
    }

    public boolean isTooltipTargetEnabled() {
        return this.enabled;
    }

    private Tooltip ensureTooltip() {
        if (this.tooltip == null) {
            this.tooltip = new Tooltip();
        }
        return this.tooltip;
    }
}

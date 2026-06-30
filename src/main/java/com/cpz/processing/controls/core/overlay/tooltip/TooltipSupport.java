package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import processing.core.PFont;

/**
 * Tooltip target helper used by closed control facades.
 *
 * @author CPZ
 */
public final class TooltipSupport implements TooltipTarget {
    private final Supplier<TooltipBounds> boundsSupplier;
    private final BooleanSupplier visibleSupplier;
    private final BooleanSupplier enabledSupplier;
    private Tooltip tooltip;

    public TooltipSupport(
            Supplier<TooltipBounds> boundsSupplier,
            BooleanSupplier visibleSupplier,
            BooleanSupplier enabledSupplier
    ) {
        this.boundsSupplier = Objects.requireNonNull(boundsSupplier, "boundsSupplier");
        this.visibleSupplier = visibleSupplier == null ? () -> true : visibleSupplier;
        this.enabledSupplier = enabledSupplier == null ? () -> true : enabledSupplier;
    }

    public TooltipSupport setTooltip(String text) {
        return this.setTooltip(new Tooltip(text));
    }

    public TooltipSupport setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public TooltipSupport setTooltipText(String text) {
        this.ensureTooltip().setText(text);
        return this;
    }

    public TooltipSupport setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.ensureTooltip().setStyle(styleConfig);
        return this;
    }

    public TooltipSupport setTooltipFont(PFont font) {
        this.ensureTooltip().setFont(font);
        return this;
    }

    public TooltipSupport setTooltipTextSize(float size) {
        this.ensureTooltip().setTextSize(size);
        return this;
    }

    public TooltipSupport setTooltipBackgroundColor(int argb) {
        this.ensureTooltip().setBackgroundColor(argb);
        return this;
    }

    public TooltipSupport setTooltipTextColor(int argb) {
        this.ensureTooltip().setTextColor(argb);
        return this;
    }

    public TooltipSupport setTooltipBorderColor(int argb) {
        this.ensureTooltip().setBorderColor(argb);
        return this;
    }

    public TooltipSupport setTooltipPadding(float padding) {
        this.ensureTooltip().setPadding(padding);
        return this;
    }

    public TooltipSupport setTooltipOffset(float offset) {
        this.ensureTooltip().setOffset(offset);
        return this;
    }

    public TooltipSupport clearTooltip() {
        this.tooltip = null;
        return this;
    }

    public TooltipBounds getTooltipBounds() {
        TooltipBounds bounds = this.boundsSupplier.get();
        return bounds == null ? new TooltipBounds(0.0F, 0.0F, 0.0F, 0.0F) : bounds;
    }

    public Tooltip getTooltip() {
        return this.tooltip;
    }

    public boolean isTooltipTargetVisible() {
        return this.visibleSupplier.getAsBoolean();
    }

    public boolean isTooltipTargetEnabled() {
        return this.enabledSupplier.getAsBoolean();
    }

    private Tooltip ensureTooltip() {
        if (this.tooltip == null) {
            this.tooltip = new Tooltip();
        }
        return this.tooltip;
    }
}

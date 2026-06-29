package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.style.LabelStyle;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.Objects;

/**
 * Convenience facade for the label control.
 *
 * @author CPZ
 */
public final class Label implements Control, TooltipTarget {
    private final LabelModel model;
    private final LabelViewModel viewModel;
    private final LabelView view;
    private final TooltipSupport tooltipSupport;

    public Label(PApplet sketch, String text, float x, float y) {
        this(sketch, ControlCode.auto("label"), text, x, y, 0.0f, 0.0f);
    }

    public Label(PApplet sketch, String text, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("label"), text, x, y, width, height);
    }

    public Label(PApplet sketch, String code, String text, float x, float y) {
        this(sketch, code, text, x, y, 0.0f, 0.0f);
    }

    public Label(PApplet sketch, String code, String text, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new LabelModel(code);
        this.viewModel = new LabelViewModel(this.model);
        this.viewModel.setText(text);
        this.view = new LabelView(sketch, this.viewModel, x, y, width, height);
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible, this::isEnabled);
    }

    public void draw() {
        this.view.draw();
    }

    public String getCode() {
        return this.model.getCode();
    }

    public String getText() {
        return this.viewModel.getText();
    }

    public void setText(String text) {
        this.viewModel.setText(text);
    }

    public boolean isEnabled() {
        return this.viewModel.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.viewModel.setEnabled(enabled);
    }

    public boolean isVisible() {
        return this.viewModel.isVisible();
    }

    public void setVisible(boolean visible) {
        this.viewModel.setVisible(visible);
    }

    public void setStyle(LabelStyle style) {
        this.view.setStyle(style);
    }

    public LabelStyle getStyle() {
        return this.view.getStyle();
    }

    public LabelStyleConfig getStyleConfig() {
        return this.view.getStyle().getLabelStyleConfig();
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }

    public Label setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public Label setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public Label setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public Label setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public Label setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public Label setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public Label setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public Label setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public Label setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public Label setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public Label clearTooltip() {
        this.tooltipSupport.clearTooltip();
        return this;
    }

    public TooltipBounds getTooltipBounds() {
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
}

package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.LabelStyle;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.Objects;

/**
 * Convenience facade for the label control.
 *
 * @author CPZ
 */
public final class Label implements ParentSizeAwareControl, TooltipAttachable {
    private final PApplet sketch;
    private final LabelModel model;
    private final LabelViewModel viewModel;
    private final LabelView view;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private ControlMeasure textSize;
    private boolean textSizeStyleIsolated;
    private Float parentWidth;
    private Float parentHeight;

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
        this(sketch, code, text, ControlBounds.absolute(x, y, width, height));
    }

    public Label(PApplet sketch, String text, ControlBounds bounds) {
        this(sketch, ControlCode.auto("label"), text, bounds);
    }

    public Label(PApplet sketch, String code, String text, ControlBounds bounds) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.model = new LabelModel(code);
        this.viewModel = new LabelViewModel(this.model);
        this.viewModel.setText(text);
        this.view = new LabelView(
                sketch,
                this.viewModel,
                resolvedBounds.x(),
                resolvedBounds.y(),
                resolvedBounds.width(),
                resolvedBounds.height()
        );
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
    }

    public void draw() {
        this.applyResolvedGeometryAndTextSize();
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
        this.textSizeStyleIsolated = false;
        this.applyResolvedTextSize();
    }

    public LabelStyle getStyle() {
        return this.view.getStyle();
    }

    public LabelStyleConfig getStyleConfig() {
        this.applyResolvedTextSize();
        return this.view.getStyle().getLabelStyleConfig();
    }

    public void setTextColor(int color) {
        this.requireStyleConfig().textColor = color;
    }

    public int getTextColor() {
        LabelStyleConfig styleConfig = this.requireStyleConfig();
        if (styleConfig.textColor != null) {
            return styleConfig.textColor;
        }
        ThemeSnapshot snapshot = this.view.getStyle().getThemeSnapshot();
        return snapshot.tokens.onSurface;
    }

    public void setPosition(float x, float y) {
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedGeometryAndTextSize();
    }

    public void setTextSize(float textSize) {
        this.setTextSize(ControlMeasure.absolute(textSize));
    }

    public void setTextSize(ControlMeasure textSize) {
        this.textSize = Objects.requireNonNull(textSize, "textSize");
        this.applyResolvedTextSize();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedGeometryAndTextSize();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedGeometryAndTextSize();
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

    public Label setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
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
        this.applyResolvedGeometryAndTextSize();
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

    private ResolvedBounds resolveBounds() {
        return this.bounds.resolve(this.parentWidth(), this.parentHeight());
    }

    private float parentWidth() {
        return this.parentWidth != null ? this.parentWidth : this.sketch.width;
    }

    private float parentHeight() {
        return this.parentHeight != null ? this.parentHeight : this.sketch.height;
    }

    private void applyResolvedGeometryAndTextSize() {
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.view.setPosition(resolvedBounds.x(), resolvedBounds.y());
        this.view.setSize(resolvedBounds.width(), resolvedBounds.height());
        this.applyResolvedTextSize();
    }

    private void applyResolvedTextSize() {
        if (this.textSize == null) {
            return;
        }
        this.ensureTextSizeStyleIsolated();
        LabelStyleConfig styleConfig = this.view.getStyle().getLabelStyleConfig();
        if (styleConfig != null) {
            styleConfig.textSize = this.textSize.resolve(this.parentHeight());
        }
    }

    private void ensureTextSizeStyleIsolated() {
        if (this.textSizeStyleIsolated) {
            return;
        }
        LabelStyle style = this.view.getStyle();
        if (style instanceof DefaultLabelStyle) {
            this.view.setStyle(((DefaultLabelStyle) style).copy());
        }
        this.textSizeStyleIsolated = true;
    }

    private LabelStyleConfig requireStyleConfig() {
        LabelStyleConfig styleConfig = this.getStyleConfig();
        if (styleConfig == null) {
            throw new IllegalStateException("Label style does not expose LabelStyleConfig");
        }
        return styleConfig;
    }
}

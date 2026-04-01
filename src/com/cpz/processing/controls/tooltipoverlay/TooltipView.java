package com.cpz.processing.controls.tooltipoverlay;

import com.cpz.processing.controls.common.ControlView;
import processing.core.PApplet;

public final class TooltipView implements ControlView {

    private static final float VERTICAL_OFFSET = 10f;

    private final PApplet sketch;
    private final TooltipViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private DefaultTooltipStyle style;

    public TooltipView(PApplet sketch, TooltipViewModel viewModel) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.style = new DefaultTooltipStyle(new TooltipStyleConfig());
    }

    @Override
    public void draw() {
        if (!viewModel.isVisible()) {
            return;
        }
        measureFromText();
        style.render(sketch, new TooltipViewState(
                x,
                y,
                width,
                height,
                viewModel.getText(),
                viewModel.isEnabled(),
                style.getTextPadding(),
                style.resolveRenderStyle().cornerRadius()
        ));
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setAnchorBounds(float anchorCenterX, float anchorTopY) {
        measureFromText();
        this.x = anchorCenterX;
        this.y = anchorTopY - (height * 0.5f) - VERTICAL_OFFSET;
    }

    public void setStyle(DefaultTooltipStyle style) {
        if (style != null) {
            this.style = style;
        }
    }

    private void measureFromText() {
        TooltipRenderStyle renderStyle = style.resolveRenderStyle();
        sketch.pushStyle();
        if (renderStyle.font() != null) {
            sketch.textFont(renderStyle.font(), renderStyle.textSize());
        } else {
            sketch.textSize(renderStyle.textSize());
        }
        width = Math.max(40f, sketch.textWidth(viewModel.getText()) + (renderStyle.textPadding() * 2f));
        height = renderStyle.minHeight();
        sketch.popStyle();
    }
}

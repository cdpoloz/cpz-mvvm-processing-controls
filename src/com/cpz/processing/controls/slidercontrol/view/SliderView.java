package com.cpz.processing.controls.slidercontrol.view;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.slidercontrol.SliderOrientation;
import com.cpz.processing.controls.slidercontrol.style.SliderDefaultStyles;
import com.cpz.processing.controls.slidercontrol.style.SliderStyle;
import com.cpz.processing.controls.slidercontrol.viewmodel.SliderViewModel;
import processing.core.PApplet;

public final class SliderView implements ControlView, PointerInteractable {

    private final PApplet sketch;
    private final SliderViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private SliderOrientation orientation;
    private SliderStyle style;

    public SliderView(PApplet sketch,
                      SliderViewModel viewModel,
                      float x,
                      float y,
                      float width,
                      float height,
                      SliderOrientation orientation) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orientation = orientation == null ? SliderOrientation.HORIZONTAL : orientation;
        this.style = SliderDefaultStyles.standard();
        notifyLayoutChanged();
    }

    @Override
    public void draw() {
        if (!viewModel.isVisible()) {
            return;
        }
        SliderGeometry geometry = buildGeometry();
        style.render(sketch, buildViewState(), geometry);
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        notifyLayoutChanged();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        notifyLayoutChanged();
    }

    public void setOrientation(SliderOrientation orientation) {
        if (orientation == null) {
            return;
        }
        this.orientation = orientation;
        notifyLayoutChanged();
    }

    public void setStyle(SliderStyle style) {
        if (style != null) {
            this.style = style;
        }
    }

    public SliderOrientation getOrientation() {
        return orientation;
    }

    @Override
    public boolean contains(float px, float py) {
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
        return px >= x - halfWidth
                && px <= x + halfWidth
                && py >= y - halfHeight
                && py <= y + halfHeight;
    }

    private SliderViewState buildViewState() {
        return new SliderViewState(
                viewModel.getNormalizedValue(),
                viewModel.isHovered(),
                viewModel.isPressed(),
                viewModel.isDragging(),
                viewModel.isShowText(),
                viewModel.getFormattedValue(),
                viewModel.isEnabled()
        );
    }

    private SliderGeometry buildGeometry() {
        if (orientation == SliderOrientation.VERTICAL) {
            return new SliderGeometry(x, y, width, height, orientation, 0f, height);
        }
        return new SliderGeometry(x, y, width, height, orientation, x - (width * 0.5f), x + (width * 0.5f));
    }

    public float toNormalizedValue(float px, float py) {
        SliderGeometry geometry = buildGeometry();
        if (orientation == SliderOrientation.VERTICAL) {
            float position = (y + (height * 0.5f)) - py;
            return geometry.positionToNormalized(position);
        }
        return geometry.positionToNormalized(px);
    }

    private void notifyLayoutChanged() {
    }
}

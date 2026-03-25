package com.cpz.processing.controls.checkboxcontrol.view;

import com.cpz.processing.controls.checkboxcontrol.style.CheckboxDefaultStyles;
import com.cpz.processing.controls.checkboxcontrol.style.CheckboxStyle;
import com.cpz.processing.controls.checkboxcontrol.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.hit.RectHitTest;
import com.cpz.processing.controls.hit.interfaces.HitTest;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class CheckboxView implements ControlView, PointerInteractable {

    private final PApplet sketch;
    private final CheckboxViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private CheckboxStyle style;
    private HitTest hitTest;

    public CheckboxView(PApplet sketch, CheckboxViewModel viewModel, float x, float y, float size) {
        this(sketch, viewModel, x, y, size, size);
    }

    public CheckboxView(PApplet sketch, CheckboxViewModel viewModel, float x, float y, float width, float height) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.style = CheckboxDefaultStyles.standard();
        this.hitTest = new RectHitTest();
        this.hitTest.onLayout(x, y, width, height);
    }

    @Override
    public void draw() {
        if (!viewModel.isVisible()) {
            return;
        }
        style.render(sketch, buildViewState());
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        notifyLayoutChanged();
    }

    public void setSize(float size) {
        this.width = size;
        this.height = size;
        notifyLayoutChanged();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        notifyLayoutChanged();
    }

    public void setStyle(CheckboxStyle style) {
        if (style != null) {
            this.style = style;
        }
    }

    public void setHitTest(HitTest hitTest) {
        if (hitTest == null) {
            return;
        }
        this.hitTest = hitTest;
        this.hitTest.onLayout(x, y, width, height);
    }

    @Override
    public boolean contains(float px, float py) {
        return hitTest.contains(px, py);
    }

    private CheckboxViewState buildViewState() {
        return new CheckboxViewState(
                x,
                y,
                width,
                height,
                viewModel.isChecked(),
                viewModel.isHovered(),
                viewModel.isPressed(),
                viewModel.isEnabled()
        );
    }

    private void notifyLayoutChanged() {
        hitTest.onLayout(x, y, width, height);
    }
}

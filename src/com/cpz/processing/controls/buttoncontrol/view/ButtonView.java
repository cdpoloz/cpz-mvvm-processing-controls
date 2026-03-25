package com.cpz.processing.controls.buttoncontrol.view;

import com.cpz.processing.controls.buttoncontrol.style.ButtonDefaultStyles;
import com.cpz.processing.controls.buttoncontrol.style.ButtonStyle;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.hit.RectHitTest;
import com.cpz.processing.controls.hit.interfaces.HitTest;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class ButtonView implements ControlView, PointerInteractable {

    private final PApplet sketch;
    private final ButtonViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private ButtonStyle style;
    private HitTest hitTest;

    public ButtonView(PApplet sketch, ButtonViewModel vm, float x, float y, float width, float height) {
        this.sketch = sketch;
        this.viewModel = vm;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.style = ButtonDefaultStyles.primary();
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

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        notifyLayoutChanged();
    }

    public void setStyle(ButtonStyle style) {
        if (style != null) this.style = style;
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private ButtonViewState buildViewState() {
        return new ButtonViewState(
                x,
                y,
                width,
                height,
                viewModel.getText(),
                viewModel.isEnabled(),
                viewModel.isHovered(),
                viewModel.isPressed()
        );
    }

    private void notifyLayoutChanged() {
        hitTest.onLayout(x, y, width, height);
    }
}

package com.cpz.processing.controls.buttoncontrol.view;

import com.cpz.processing.controls.buttoncontrol.style.ButtonStyle;
import com.cpz.processing.controls.buttoncontrol.style.DefaultButtonStyle;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.common.ControlView;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class ButtonView implements ControlView {

    private final PApplet sketch;
    private final ButtonViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private ButtonStyle style;

    public ButtonView(PApplet sketch, ButtonViewModel vm, float x, float y, float width, float height) {
        this.sketch = sketch;
        this.viewModel = vm;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.style = new DefaultButtonStyle();
    }

    @Override
    public void draw() {
        ButtonViewState state = viewModel.buildViewState(x, y, width, height);
        style.render(sketch, state);
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setStyle(ButtonStyle style) {
        if (style != null) this.style = style;
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
}

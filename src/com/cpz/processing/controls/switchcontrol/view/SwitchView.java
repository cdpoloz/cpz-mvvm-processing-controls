package com.cpz.processing.controls.switchcontrol.view;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.hit.CircleHitTest;
import com.cpz.processing.controls.hit.interfaces.HitTest;
import com.cpz.processing.controls.switchcontrol.style.SwitchDefaultStyles;
import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class SwitchView implements ControlView, PointerInteractable {

    // <editor-fold defaultstate="collapsed" desc="*** variables ***">
    private final PApplet sketch;
    private final SwitchViewModel viewModel;
    private float x, y, width, height;
    private SwitchStyle style;
    private HitTest hitTest;
    // </editor-fold>

    public SwitchView(PApplet sketch, SwitchViewModel viewModel, float x, float y, float size) {
        this(sketch, viewModel, x, y, size, size);
    }

    public SwitchView(PApplet sketch, SwitchViewModel viewModel, float x, float y, float width, float height) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // minimal default style
        this.style = SwitchDefaultStyles.circular();
        // minimal default hit test
        float diameter = Math.min(width, height);
        this.hitTest = new CircleHitTest(x, y, diameter * 0.5f);
    }

    /**
     * Draws the control using the active style.
     */
    public void draw() {
        if (!viewModel.isVisible()) return;
        style.render(sketch, buildViewState());
    }

    /**
     * Builds the current visual snapshot.
     */
    private SwitchViewState buildViewState() {
        return new SwitchViewState(
                x,
                y,
                width,
                height,
                viewModel.getState(),
                viewModel.getTotalStates(),
                viewModel.isHovered(),
                viewModel.isEnabled()
        );
    }

    @Override
    public boolean contains(float px, float py) {
        return hitTest.contains(px, py);
    }

    public void setHitTest(HitTest hitTest) {
        this.hitTest = hitTest;
        this.hitTest.onLayout(x, y, width, height);
    }

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

    private void notifyLayoutChanged() {
        hitTest.onLayout(x, y, width, height);
    }

    public void setStyle(SwitchStyle style) {
        this.style = style;
    }

}

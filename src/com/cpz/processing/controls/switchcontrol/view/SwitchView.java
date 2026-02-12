package com.cpz.processing.controls.switchcontrol.view;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.hit.CircleHitTest;
import com.cpz.processing.controls.hit.interfaces.HitTest;
import com.cpz.processing.controls.switchcontrol.style.SwitchDefaultStyles;
import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class SwitchView implements ControlView {

    // <editor-fold defaultstate="collapsed" desc="*** variables ***">
    private final PApplet sketch;
    private final SwitchViewModel viewModel;
    @Getter
    private float x, y, width, height;
    private boolean hovering;
    private SwitchStyle style;
    private HitTest hitTest;
    // </editor-fold>

    public SwitchView(@NotNull PApplet sketch, @NotNull SwitchViewModel viewModel, float x, float y, float size) {
        this(sketch, viewModel, x, y, size, size);
    }

    public SwitchView(@NotNull PApplet sketch, @NotNull SwitchViewModel viewModel, float x, float y, float width, float height) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // estilo por defecto mínimo
        this.style = SwitchDefaultStyles.circular();
        // HitTest por defecto mínimo
        float diameter = Math.min(width, height);
        this.hitTest = new CircleHitTest(x, y, diameter * 0.5f);
    }

    /**
     * Dibuja el control delegando en el estilo activo.
     */
    public void draw() {
        if (!viewModel.isDisplay()) return;
        style.draw(sketch, buildViewState());
    }

    /**
     * Construye el snapshot visual actual.
     */
    @Contract(" -> new")
    private @NotNull SwitchViewState buildViewState() {
        return new SwitchViewState(x, y, width, height, viewModel.getState(), viewModel.getTotalStates(), hovering, viewModel.isEnabled());
    }

    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public boolean contains(float px, float py) {
        return hitTest.contains(px, py);
    }

    public boolean isHovering() {
        return hovering;
    }

    public void setHitTest(@NotNull HitTest hitTest) {
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

    public void setStyle(@NotNull SwitchStyle style) {
        this.style = style;
    }

}

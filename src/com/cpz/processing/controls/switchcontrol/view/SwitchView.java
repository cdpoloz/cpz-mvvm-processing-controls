package com.cpz.processing.controls.switchcontrol.view;

import com.cpz.processing.controls.hit.CircleHitTest;
import com.cpz.processing.controls.hit.HitTest;
import com.cpz.processing.controls.switchcontrol.style.DefaultSwitchStyle;
import com.cpz.processing.controls.switchcontrol.style.SwitchStyle;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class SwitchView {

    // <editor-fold defaultstate="collapsed" desc="*** variables ***">
    private final PApplet sketch;
    private final SwitchViewModel viewModel;
    @Getter
    private float x, y, size;
    private boolean hovering;
    private SwitchStyle style;
    private HitTest hitTest;
    // </editor-fold>

    public SwitchView(@NotNull PApplet sketch, @NotNull SwitchViewModel viewModel, float x, float y, float size) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.size = size;
        // Estilo por defecto mínimo
        this.style = new DefaultSwitchStyle();
        // HitTest por defecto mínimo
        this.hitTest = new CircleHitTest(x, y, size * 0.5f);
    }

    /**
     * Dibuja el control delegando en el estilo activo.
     */
    public void draw() {
        if (!viewModel.isDisplay()) {
            return;
        }
        style.draw(sketch, buildViewState());
    }

    /**
     * Construye el snapshot visual actual.
     */
    @Contract(" -> new")
    private @NotNull SwitchViewState buildViewState() {
        return new SwitchViewState(x, y, size, viewModel.getState(), viewModel.getTotalStates(), hovering, viewModel.isEnabled());
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
        this.hitTest.onLayout(x, y, size);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        notifyLayoutChanged();
    }

    public void setSize(float size) {
        this.size = size;
        notifyLayoutChanged();
    }

    private void notifyLayoutChanged() {
        hitTest.onLayout(x, y, size);
    }

    public void setStyle(@NotNull SwitchStyle style) {
        this.style = style;
    }

}

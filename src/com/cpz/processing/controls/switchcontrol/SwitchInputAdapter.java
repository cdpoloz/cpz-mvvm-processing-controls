package com.cpz.processing.controls.switchcontrol;

import com.cpz.processing.controls.switchcontrol.view.SwitchView;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewModel;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class SwitchInputAdapter {

    private boolean mousePressedPrev = false;
    private boolean pressStartedInside = false;

    /**
     * Debe llamarse una vez por frame.
     */
    public void update(PApplet p, SwitchView view, @NotNull SwitchViewModel vm) {
        // Si no se muestra, no hay interaccion
        if (!vm.isDisplay() || !vm.isEnabled()) {
            view.setHovering(false);
            pressStartedInside = false;
            mousePressedPrev = p.mousePressed;
            return;
        }
        boolean hovering = view.contains(p.mouseX, p.mouseY);
        view.setHovering(hovering);
        boolean pressedNow = p.mousePressed;
        // press
        if (pressedNow && !mousePressedPrev) {
            pressStartedInside = hovering;
        }
        // release
        if (!pressedNow && mousePressedPrev) {
            if (pressStartedInside && hovering) {
                vm.onActivate();
            }
            pressStartedInside = false;
        }
        mousePressedPrev = pressedNow;
    }

}

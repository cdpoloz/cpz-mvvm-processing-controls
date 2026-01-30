package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import com.cpz.processing.controls.util.Colors;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class DefaultSwitchStyle implements SwitchStyle {

    private final int[] colors;

    public DefaultSwitchStyle() {
        this.colors = new int[]{Colors.gray(0), Colors.gray(255)};
    }

    public DefaultSwitchStyle(int... argbColors) {
        if (argbColors == null || argbColors.length == 0) {
            throw new IllegalArgumentException("Debe definirse al menos un color");
        }
        this.colors = argbColors;
    }

    @Override
    public void draw(PApplet p, @NotNull SwitchViewState s) {
        int idx = clampIndex(s.stateIndex(), colors.length - 1);
        int fillColor = colors[idx];
        // Estado disabled → alpha reducido
        if (!s.enabled()) fillColor = p.color(p.red(fillColor), p.green(fillColor), p.blue(fillColor), 80);
        p.pushStyle();
        p.stroke(255);
        p.strokeWeight(s.hovering() ? 5f : 2f);
        p.fill(fillColor);
        p.circle(s.x(), s.y(), s.size());
        p.popStyle();
    }

    private int clampIndex(int index, int max) {
        return Math.min(index, max);
    }

}

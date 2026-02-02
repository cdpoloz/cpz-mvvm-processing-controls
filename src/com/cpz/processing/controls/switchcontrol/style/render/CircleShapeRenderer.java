package com.cpz.processing.controls.switchcontrol.style.render;

import com.cpz.processing.controls.switchcontrol.style.SwitchStyleConfig;
import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import com.cpz.processing.controls.util.Colors;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class CircleShapeRenderer implements ShapeRenderer {

    @Override
    public void draw(PApplet p, @NotNull SwitchViewState s, @NotNull SwitchStyleConfig cfg) {
        int idx = Math.min(s.stateIndex(), cfg.colorEstados.length - 1);
        int fill = cfg.colorEstados[idx];
        if (!s.enabled()) fill = Colors.alpha(cfg.alfaDeshabilitado, fill);
        p.pushStyle();
        p.stroke(cfg.colorStroke);
        p.strokeWeight(s.hovering() ? cfg.strokeWidthHover : cfg.strokeWidth);
        p.fill(fill);
        float diameter = Math.min(s.width(), s.height());
        p.circle(s.x(), s.y(), diameter);
        p.popStyle();
    }

}

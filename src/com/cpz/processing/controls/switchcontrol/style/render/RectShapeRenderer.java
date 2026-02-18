package com.cpz.processing.controls.switchcontrol.style.render;

import com.cpz.processing.controls.switchcontrol.style.SwitchStyleConfig;
import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class RectShapeRenderer implements ShapeRenderer {

    @Override
    public void draw(PApplet p, SwitchViewState s, SwitchStyleConfig cfg) {
        int idx = Math.min(s.stateIndex(), cfg.colorEstados.length - 1);
        int fill = cfg.colorEstados[idx];
        if (!s.enabled()) fill = Colors.alpha(cfg.alfaDeshabilitado, fill);
        float halfW = s.width() * 0.5f;
        float halfH = s.height() * 0.5f;
        float x = s.x() - halfW;
        float y = s.y() - halfH;
        p.pushStyle();
        p.stroke(cfg.colorStroke);
        p.strokeWeight(s.hovering() ? cfg.strokeWidthHover : cfg.strokeWidth);
        p.fill(fill);
        p.rect(x, y, s.width(), s.height(), cfg.esquinaRadio);
        p.popStyle();
    }
}

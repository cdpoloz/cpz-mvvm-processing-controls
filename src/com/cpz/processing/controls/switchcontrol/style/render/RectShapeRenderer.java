package com.cpz.processing.controls.switchcontrol.style.render;

import com.cpz.processing.controls.switchcontrol.style.SwitchStyleConfig;
import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class RectShapeRenderer implements ShapeRenderer {

    @Override
    public void render(PApplet p, SwitchViewState s, SwitchStyleConfig cfg) {
        int idx = Math.min(s.stateIndex(), cfg.stateColors.length - 1);
        int fill = InteractiveStyleHelper.applyDisabledAlpha(cfg.stateColors[idx], s.enabled(), cfg.disabledAlpha);
        float halfW = s.width() * 0.5f;
        float halfH = s.height() * 0.5f;
        float x = s.x() - halfW;
        float y = s.y() - halfH;
        p.pushStyle();
        p.stroke(InteractiveStyleHelper.resolveStrokeColor(cfg.strokeColor, s.enabled(), cfg.disabledAlpha));
        p.strokeWeight(InteractiveStyleHelper.resolveStrokeWeight(cfg.strokeWidth, cfg.strokeWidthHover, s.hovered()));
        p.fill(fill);
        p.rect(x, y, s.width(), s.height(), cfg.cornerRadius);
        p.popStyle();
    }
}

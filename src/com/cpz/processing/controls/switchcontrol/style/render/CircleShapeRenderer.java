package com.cpz.processing.controls.switchcontrol.style.render;

import com.cpz.processing.controls.switchcontrol.style.SwitchRenderStyle;
import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class CircleShapeRenderer implements ShapeRenderer {

    @Override
    public void render(PApplet p, float x, float y, float width, float height, SwitchRenderStyle style) {
        p.pushStyle();
        p.stroke(style.strokeColor());
        p.strokeWeight(style.strokeWeight());
        p.fill(style.fillColor());
        float diameter = Math.min(width, height);
        p.circle(x, y, diameter);
        p.popStyle();
    }

}

package com.cpz.processing.controls.switchcontrol.style.render;

import com.cpz.processing.controls.switchcontrol.style.SwitchRenderStyle;
import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class RectShapeRenderer implements ShapeRenderer {

    private final float cornerRadius;

    public RectShapeRenderer() {
        this(0f);
    }

    public RectShapeRenderer(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    public void render(PApplet p, float centerX, float centerY, float width, float height, SwitchRenderStyle style) {
        float halfW = width * 0.5f;
        float halfH = height * 0.5f;
        float x = centerX - halfW;
        float y = centerY - halfH;
        p.pushStyle();
        p.stroke(style.strokeColor());
        p.strokeWeight(style.strokeWeight());
        p.fill(style.fillColor());
        p.rect(x, y, width, height, cornerRadius);
        p.popStyle();
    }
}

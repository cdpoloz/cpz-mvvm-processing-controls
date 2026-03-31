package com.cpz.processing.controls.checkboxcontrol.style.render;

import com.cpz.processing.controls.checkboxcontrol.style.CheckboxRenderStyle;
import com.cpz.processing.controls.checkboxcontrol.style.interfaces.CheckboxRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

public final class SvgCheckboxRenderer implements CheckboxRenderer {

    private final PShape shape;

    public SvgCheckboxRenderer(PApplet app, String path) {
        this.shape = loadShape(app, path);
        if (shape != null) {
            shape.disableStyle();
        }
    }

    @Override
    public void render(PApplet app, float x, float y, float width, float height, CheckboxRenderStyle style) {
        float left = x - (width * 0.5f);
        float top = y - (height * 0.5f);

        app.pushStyle();
        if (shape != null) {
            app.shapeMode(PConstants.CENTER);
            app.fill(style.boxFillColor());
            app.stroke(style.borderColor());
            app.strokeWeight(style.borderWidth());
            app.shape(shape, x, y, width, height);
        }

        if (style.showCheck()) {
            float inset = Math.min(width, height) * style.checkInset();
            float x1 = left + inset;
            float y1 = top + (height * 0.55f);
            float x2 = left + (width * 0.45f);
            float y2 = top + height - inset;
            float x3 = left + width - inset;
            float y3 = top + inset;
            app.stroke(style.checkColor());
            app.strokeWeight(style.checkStrokeWeight());
            app.noFill();
            app.line(x1, y1, x2, y2);
            app.line(x2, y2, x3, y3);
        }
        app.popStyle();
    }

    private static PShape loadShape(PApplet app, String path) {
        if (app == null || path == null || path.isEmpty()) {
            return null;
        }
        PShape loadedShape = app.loadShape(path);
        if (loadedShape == null && path.startsWith("data/")) {
            loadedShape = app.loadShape(path.substring("data/".length()));
        }
        return loadedShape;
    }
}

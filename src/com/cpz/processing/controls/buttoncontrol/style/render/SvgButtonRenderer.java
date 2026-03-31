package com.cpz.processing.controls.buttoncontrol.style.render;

import com.cpz.processing.controls.buttoncontrol.style.ButtonRenderStyle;
import com.cpz.processing.controls.buttoncontrol.style.interfaces.ButtonRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

public final class SvgButtonRenderer implements ButtonRenderer {

    private final PShape shape;

    public SvgButtonRenderer(PApplet app, String path) {
        this.shape = loadShape(app, path);
        if (shape != null) {
            shape.disableStyle();
        }
    }

    @Override
    public void render(PApplet app, float x, float y, float width, float height, ButtonRenderStyle style) {
        String text = style.text();
        if (text == null) {
            text = "";
        }

        app.pushStyle();
        if (shape != null) {
            app.shapeMode(PConstants.CENTER);
            app.fill(style.fillColor());
            app.stroke(style.strokeColor());
            app.strokeWeight(style.strokeWeight());
            app.shape(shape, x, y, width, height);
        }
        app.fill(style.textColor());
        app.textAlign(PConstants.CENTER, PConstants.CENTER);
        app.text(text, x, y);
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

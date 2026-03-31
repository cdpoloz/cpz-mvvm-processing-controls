package com.cpz.processing.controls.switchcontrol.style.render;

import com.cpz.processing.controls.switchcontrol.style.SwitchRenderStyle;
import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

public final class SvgShapeRenderer implements ShapeRenderer {

    private final PShape shape;

    public SvgShapeRenderer(PApplet app, String path) {
        this.shape = loadShape(app, path);
        if (shape != null) {
            shape.disableStyle();
        }
    }

    @Override
    public void render(PApplet app, float x, float y, float width, float height, SwitchRenderStyle style) {
        if (shape == null) {
            return;
        }
        app.pushStyle();
        app.shapeMode(PConstants.CENTER);
        app.fill(style.fillColor());
        app.stroke(style.strokeColor());
        app.strokeWeight(style.strokeWeight());
        app.shape(shape, x, y, width, height);
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

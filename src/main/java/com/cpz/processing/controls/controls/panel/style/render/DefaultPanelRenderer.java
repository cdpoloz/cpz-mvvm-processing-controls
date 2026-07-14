package com.cpz.processing.controls.controls.panel.style.render;

import com.cpz.processing.controls.controls.panel.style.PanelRenderStyle;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Default renderer for panel container chrome.
 *
 * <p>The renderer draws only the optional background and stroke rectangle. It
 * does not draw children, apply layout, clip content, or resolve theme tokens.
 * The caller supplies an already resolved {@link PanelRenderStyle}.</p>
 *
 * @author CPZ
 */
public final class DefaultPanelRenderer {
    /**
     * Renders panel chrome using Processing style isolation.
     *
     * @param sketch Processing sketch
     * @param x panel x position in sketch coordinates
     * @param y panel y position in sketch coordinates
     * @param width panel width
     * @param height panel height
     * @param style resolved render style
     */
    public void render(PApplet sketch, float x, float y, float width, float height, PanelRenderStyle style) {
        if (!style.backgroundVisible() && (!style.strokeVisible() || style.strokeWeight() <= 0.0F)) {
            return;
        }

        sketch.pushStyle();
        try {
            sketch.rectMode(PConstants.CORNER);
            if (style.backgroundVisible()) {
                sketch.fill(style.backgroundColor());
            } else {
                sketch.noFill();
            }

            if (style.strokeVisible() && style.strokeWeight() > 0.0F) {
                sketch.stroke(style.strokeColor());
                sketch.strokeWeight(style.strokeWeight());
            } else {
                sketch.noStroke();
            }

            sketch.rect(x, y, width, height, style.cornerRadius());
        } finally {
            sketch.popStyle();
        }
    }
}

package com.cpz.processing.controls.switchcontrol.style.interfaces;

import com.cpz.processing.controls.switchcontrol.style.SwitchRenderStyle;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public interface ShapeRenderer {
    void render(PApplet p, float x, float y, float width, float height, SwitchRenderStyle style);
}

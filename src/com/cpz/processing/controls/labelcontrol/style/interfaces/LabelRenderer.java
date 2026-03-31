package com.cpz.processing.controls.labelcontrol.style.interfaces;

import com.cpz.processing.controls.labelcontrol.style.LabelRenderStyle;
import processing.core.PApplet;

public interface LabelRenderer {
    void render(PApplet sketch, float x, float y, float width, float height, LabelRenderStyle style);
}

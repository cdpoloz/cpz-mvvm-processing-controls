package com.cpz.processing.controls.buttoncontrol.style.interfaces;

import com.cpz.processing.controls.buttoncontrol.style.ButtonRenderStyle;
import processing.core.PApplet;

public interface ButtonRenderer {
    void render(PApplet p, float x, float y, float width, float height, ButtonRenderStyle style);
}

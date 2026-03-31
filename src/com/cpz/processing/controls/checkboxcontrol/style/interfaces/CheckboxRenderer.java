package com.cpz.processing.controls.checkboxcontrol.style.interfaces;

import com.cpz.processing.controls.checkboxcontrol.style.CheckboxRenderStyle;
import processing.core.PApplet;

public interface CheckboxRenderer {
    void render(PApplet p, float x, float y, float width, float height, CheckboxRenderStyle style);
}

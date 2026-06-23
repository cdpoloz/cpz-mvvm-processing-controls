package com.cpz.processing.controls.controls.checkbox.style.render;

import com.cpz.processing.controls.controls.checkbox.style.CheckboxRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for checkbox renderer.
 *
 * Responsibilities:
 * - Draw already resolved frame data.
 * - Keep rendering concerns separate from state decisions.
 *
 * Behavior:
 * - Uses already resolved state and does not decide behavior.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 *
 * @author CPZ
 */
public interface CheckboxRenderer {
   void render(PApplet sketch, float x, float y, float width, float height, CheckboxRenderStyle renderStyle);
}

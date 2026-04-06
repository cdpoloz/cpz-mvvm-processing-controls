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
 */
public interface CheckboxRenderer {
   void render(PApplet var1, float var2, float var3, float var4, float var5, CheckboxRenderStyle var6);
}

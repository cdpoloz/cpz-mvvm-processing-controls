package com.cpz.processing.controls.controls.button.style.render;

import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for button renderer.
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
public interface ButtonRenderer {
   void render(PApplet var1, float var2, float var3, float var4, float var5, ButtonRenderStyle var6);
}

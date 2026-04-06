package com.cpz.processing.controls.controls.label.style.render;

import com.cpz.processing.controls.controls.label.style.LabelRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for label renderer.
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
public interface LabelRenderer {
   /**
    * Draws a label.
    *
    * @param var1 Processing sketch
    * @param var2 x position
    * @param var3 y position
    * @param var4 reserved width, when applicable
    * @param var5 reserved height, when applicable
    * @param var6 resolved render values
    */
   void render(PApplet var1, float var2, float var3, float var4, float var5, LabelRenderStyle var6);
}

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
 *
 * @author CPZ
 */
public interface LabelRenderer {
   /**
    * Draws a label.
    *
    * @param sketch Processing sketch
    * @param x x position
    * @param y y position
    * @param width reserved width, when applicable
    * @param height reserved height, when applicable
    * @param renderStyle resolved render values
    */
   void render(PApplet sketch, float x, float y, float width, float height, LabelRenderStyle renderStyle);
}

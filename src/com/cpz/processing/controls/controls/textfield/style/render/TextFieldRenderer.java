package com.cpz.processing.controls.controls.textfield.style.render;

import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import com.cpz.processing.controls.controls.textfield.style.TextFieldRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for text field renderer.
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
public interface TextFieldRenderer {
   /**
    * Draws the text field frame.
    *
    * @param var1 Processing sketch
    * @param var2 immutable text field state
    * @param var3 resolved render style
    */
   void render(PApplet var1, TextFieldViewState var2, TextFieldRenderStyle var3);
}

package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

/**
 * Style component for text field style.
 *
 * Responsibilities:
 * - Resolve visual values from immutable state and theme data.
 * - Keep interaction rules outside the rendering layer.
 *
 * Behavior:
 * - Does not process raw input or mutate the backing model.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 */
public interface TextFieldStyle {
   /**
    * Renders the text field frame.
    *
    * @param var1 Processing sketch
    * @param var2 immutable text field state
    * @param var3 cached theme snapshot
    */
   void render(PApplet var1, TextFieldViewState var2, ThemeSnapshot var3);

   /**
    * Resolves visual values for a text field frame.
    *
    * @param var1 immutable text field state
    * @param var2 cached theme snapshot
    * @return resolved render style
    */
   TextFieldRenderStyle resolveRenderStyle(TextFieldViewState var1, ThemeSnapshot var2);

   /**
    * Returns the theme snapshot used by this style.
    *
    * @return cached theme snapshot
    */
   ThemeSnapshot getThemeSnapshot();
}

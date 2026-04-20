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
 *
 * @author CPZ
 */
public interface TextFieldStyle {
   /**
    * Renders the text field frame.
    *
    * @param sketch Processing sketch
    * @param state immutable text field state
    * @param snapshot cached theme snapshot
    */
   void render(PApplet sketch, TextFieldViewState state, ThemeSnapshot snapshot);

   /**
    * Resolves visual values for a text field frame.
    *
    * @param state immutable text field state
    * @param snapshot cached theme snapshot
    * @return resolved render style
    */
   TextFieldRenderStyle resolveRenderStyle(TextFieldViewState state, ThemeSnapshot snapshot);

   /**
    * Returns the theme snapshot used by this style.
    *
    * @return cached theme snapshot
    */
   ThemeSnapshot getThemeSnapshot();
}

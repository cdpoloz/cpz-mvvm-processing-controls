package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

/**
 * Style component for button style.
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
public interface ButtonStyle {
   void render(PApplet sketch, ButtonViewState state, ThemeSnapshot snapshot);

   ThemeSnapshot getThemeSnapshot();
}

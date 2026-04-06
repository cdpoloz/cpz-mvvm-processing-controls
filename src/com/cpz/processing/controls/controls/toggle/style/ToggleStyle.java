package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.state.ToggleViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

/**
 * Style component for toggle style.
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
public interface ToggleStyle {
   void render(PApplet var1, ToggleViewState var2, ThemeSnapshot var3);

   ThemeSnapshot getThemeSnapshot();
}

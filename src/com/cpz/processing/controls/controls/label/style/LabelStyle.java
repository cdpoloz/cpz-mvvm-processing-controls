package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.state.LabelViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

/**
 * Style component for label style.
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
public interface LabelStyle {
   /**
    * Renders the label with the provided state and theme snapshot.
    *
    * @param var1 Processing sketch
    * @param var2 immutable label state
    * @param var3 cached theme snapshot
    */
   void render(PApplet var1, LabelViewState var2, ThemeSnapshot var3);

   /**
    * Resolves typography for measurement and rendering.
    *
    * @return typography used by the label
    */
   LabelTypography resolveTypography();

   /**
    * Returns the theme snapshot used by this style.
    *
    * @return cached theme snapshot
    */
   ThemeSnapshot getThemeSnapshot();
}

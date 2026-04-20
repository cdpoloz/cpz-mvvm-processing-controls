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
 *
 * @author CPZ
 */
public interface LabelStyle {
   /**
    * Renders the label with the provided state and theme snapshot.
    *
    * @param sketch Processing sketch
    * @param state immutable label state
    * @param snapshot cached theme snapshot
    */
   void render(PApplet sketch, LabelViewState state, ThemeSnapshot snapshot);

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

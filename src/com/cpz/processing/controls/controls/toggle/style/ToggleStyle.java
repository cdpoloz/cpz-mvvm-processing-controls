package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.state.ToggleViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

public interface ToggleStyle {
   void render(PApplet var1, ToggleViewState var2, ThemeSnapshot var3);

   ThemeSnapshot getThemeSnapshot();
}

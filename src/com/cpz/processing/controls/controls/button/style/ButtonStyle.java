package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

public interface ButtonStyle {
   void render(PApplet var1, ButtonViewState var2, ThemeSnapshot var3);

   ThemeSnapshot getThemeSnapshot();
}

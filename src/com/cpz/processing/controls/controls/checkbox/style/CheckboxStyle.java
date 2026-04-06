package com.cpz.processing.controls.controls.checkbox.style;

import com.cpz.processing.controls.controls.checkbox.state.CheckboxViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

public interface CheckboxStyle {
   void render(PApplet var1, CheckboxViewState var2, ThemeSnapshot var3);

   ThemeSnapshot getThemeSnapshot();
}

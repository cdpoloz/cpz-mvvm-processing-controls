package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

public interface TextFieldStyle {
   void render(PApplet var1, TextFieldViewState var2, ThemeSnapshot var3);

   TextFieldRenderStyle resolveRenderStyle(TextFieldViewState var1, ThemeSnapshot var2);

   ThemeSnapshot getThemeSnapshot();
}

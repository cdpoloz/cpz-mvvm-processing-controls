package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.state.LabelViewState;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import processing.core.PApplet;

public interface LabelStyle {
   void render(PApplet var1, LabelViewState var2, ThemeSnapshot var3);

   LabelTypography resolveTypography();

   ThemeSnapshot getThemeSnapshot();
}

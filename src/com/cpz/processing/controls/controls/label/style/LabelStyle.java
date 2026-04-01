package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.state.LabelViewState;
import processing.core.PApplet;

public interface LabelStyle {
   void render(PApplet var1, LabelViewState var2);

   LabelTypography resolveTypography();
}

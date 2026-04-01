package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import processing.core.PApplet;

public interface TextFieldStyle {
   void render(PApplet var1, TextFieldViewState var2);

   TextFieldRenderStyle resolveRenderStyle(TextFieldViewState var1);
}

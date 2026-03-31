package com.cpz.processing.controls.textfieldcontrol.style.interfaces;

import com.cpz.processing.controls.textfieldcontrol.style.TextFieldRenderStyle;
import com.cpz.processing.controls.textfieldcontrol.view.TextFieldViewState;
import processing.core.PApplet;

public interface TextFieldRenderer {

    void render(PApplet sketch, TextFieldViewState state, TextFieldRenderStyle style);
}

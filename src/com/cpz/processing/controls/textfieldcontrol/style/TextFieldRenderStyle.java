package com.cpz.processing.controls.textfieldcontrol.style;

import processing.core.PFont;

public record TextFieldRenderStyle(int backgroundColor,
                                   int borderColor,
                                   int textColor,
                                   int cursorColor,
                                   int selectionColor,
                                   int selectionTextColor,
                                   float textSize,
                                   PFont font) {
}

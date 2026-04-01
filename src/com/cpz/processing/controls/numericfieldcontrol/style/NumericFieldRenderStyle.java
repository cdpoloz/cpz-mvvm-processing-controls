package com.cpz.processing.controls.numericfieldcontrol.style;

import processing.core.PFont;

public record NumericFieldRenderStyle(int backgroundColor,
                                      int borderColor,
                                      int textColor,
                                      int cursorColor,
                                      int selectionColor,
                                      int selectionTextColor,
                                      float textSize,
                                      PFont font) {
}

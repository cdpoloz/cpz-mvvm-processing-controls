package com.cpz.processing.controls.labelcontrol.style;

import processing.core.PFont;

public record LabelTypography(PFont font,
                              float textSize,
                              float lineSpacingMultiplier,
                              HorizontalAlign textAlignHorizontal,
                              VerticalAlign textAlignVertical) {
}

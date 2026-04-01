package com.cpz.processing.controls.tooltipoverlay;

import processing.core.PFont;

public record TooltipRenderStyle(int backgroundColor,
                                 int textColor,
                                 int strokeColor,
                                 float strokeWeight,
                                 float textSize,
                                 float textPadding,
                                 float cornerRadius,
                                 float minHeight,
                                 PFont font) {
}

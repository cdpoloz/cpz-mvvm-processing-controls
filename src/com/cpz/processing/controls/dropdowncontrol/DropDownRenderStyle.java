package com.cpz.processing.controls.dropdowncontrol;

import processing.core.PFont;

public record DropDownRenderStyle(int baseFillColor,
                                  int listFillColor,
                                  int itemHoverColor,
                                  int itemSelectedColor,
                                  int strokeColor,
                                  float strokeWeight,
                                  int textColor,
                                  int arrowColor,
                                  float cornerRadius,
                                  float listCornerRadius,
                                  float textSize,
                                  float itemHeight,
                                  float textPadding,
                                  float arrowPadding,
                                  int maxVisibleItems,
                                  PFont font) {
}

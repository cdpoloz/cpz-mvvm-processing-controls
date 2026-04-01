package com.cpz.processing.controls.controls.radiogroup.style;

import java.util.List;
import processing.core.PFont;

public record RadioGroupRenderStyle(List itemStyles, float indicatorOuterDiameter, float indicatorInnerDiameter, float strokeWeight, float textSize, float cornerRadius, PFont font) {
}

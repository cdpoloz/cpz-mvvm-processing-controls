package com.cpz.processing.controls.labelcontrol.style;

import processing.core.PFont;

/**
 * @author CPZ
 */
public class LabelStyleConfig {

    public PFont font;
    public float textSize = 12f;
    public int textColor = 0;
    public float lineSpacingMultiplier = 1.0f;
    public HorizontalAlign alignX = HorizontalAlign.START;
    public VerticalAlign alignY = VerticalAlign.BASELINE;
    public int disabledAlpha = 100;

    public void setAlign(HorizontalAlign alignX, VerticalAlign alignY) {
        this.alignX = alignX == null ? HorizontalAlign.START : alignX;
        this.alignY = alignY == null ? VerticalAlign.BASELINE : alignY;
    }

    public void setAlign(int alignX, int alignY) {
        setAlign(
                com.cpz.processing.controls.labelcontrol.style.render.LabelAlignMapper.fromProcessingHorizontal(alignX),
                com.cpz.processing.controls.labelcontrol.style.render.LabelAlignMapper.fromProcessingVertical(alignY)
        );
    }
}

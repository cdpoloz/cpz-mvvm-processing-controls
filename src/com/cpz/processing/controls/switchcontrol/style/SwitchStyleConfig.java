package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.switchcontrol.style.interfaces.ShapeRenderer;

/**
 * @author CPZ
 */
public final class SwitchStyleConfig {

    public ShapeRenderer shape;
    public int[] stateColors;
    public int strokeColor;
    public float strokeWidth;
    public float strokeWidthHover;
    public float hoverBlendWithWhite;
    public float pressedBlendWithBlack;
    public int disabledAlpha; // 0-255

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shape = shapeRenderer;
    }
}

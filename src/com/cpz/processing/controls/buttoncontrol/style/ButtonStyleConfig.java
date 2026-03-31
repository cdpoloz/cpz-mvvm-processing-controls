package com.cpz.processing.controls.buttoncontrol.style;

import com.cpz.processing.controls.buttoncontrol.style.interfaces.ButtonRenderer;

/**
 * @author CPZ
 */
public final class ButtonStyleConfig {

    public Integer fillOverride;
    public Integer textOverride;
    public Integer strokeOverride;
    public int baseColor;
    public int textColor;
    public int strokeColor;
    public float strokeWeight;
    public float strokeWeightHover;
    public float cornerRadius;
    public int disabledAlpha;
    public float hoverBlendWithWhite;
    public float pressedBlendWithBlack;
    public ButtonRenderer renderer;

    public void setRenderer(ButtonRenderer renderer) {
        this.renderer = renderer;
    }
}

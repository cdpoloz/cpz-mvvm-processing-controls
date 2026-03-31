package com.cpz.processing.controls.checkboxcontrol.style;

import com.cpz.processing.controls.checkboxcontrol.style.interfaces.CheckboxRenderer;

/**
 * @author CPZ
 */
public final class CheckboxStyleConfig {

    public Integer checkedFillOverride;
    public Integer uncheckedFillOverride;
    public Integer hoverFillOverride;
    public Integer pressedFillOverride;
    public Integer checkOverride;
    public Integer strokeOverride;
    public int boxColor;
    public int boxHoverColor;
    public int boxPressedColor;
    public int checkColor;
    public int borderColor;
    public float borderWidth;
    public float borderWidthHover;
    public float cornerRadius;
    public int disabledAlpha;
    public float checkInset;
    public CheckboxRenderer renderer;

    public void setRenderer(CheckboxRenderer renderer) {
        this.renderer = renderer;
    }
}

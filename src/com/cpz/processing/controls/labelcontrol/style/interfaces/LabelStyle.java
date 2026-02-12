package com.cpz.processing.controls.labelcontrol.style.interfaces;

import com.cpz.processing.controls.labelcontrol.view.LabelViewState;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public interface LabelStyle {

    void draw(PApplet sketch, LabelViewState state);

    void applyTextStyle(PApplet sketch);

    float getLineSpacingMultiplier();
}

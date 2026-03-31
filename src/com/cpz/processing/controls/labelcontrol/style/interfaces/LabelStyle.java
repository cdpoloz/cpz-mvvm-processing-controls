package com.cpz.processing.controls.labelcontrol.style.interfaces;

import com.cpz.processing.controls.labelcontrol.style.LabelTypography;
import com.cpz.processing.controls.labelcontrol.view.LabelViewState;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public interface LabelStyle {

    void render(PApplet sketch, LabelViewState state);

    LabelTypography resolveTypography();
}

package com.cpz.processing.controls.switchcontrol.style.interfaces;

import com.cpz.processing.controls.switchcontrol.style.SwitchStyleConfig;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public interface ShapeRenderer {
    void draw(PApplet p, SwitchViewState s, SwitchStyleConfig cfg);
}

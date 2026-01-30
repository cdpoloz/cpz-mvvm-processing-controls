package com.cpz.processing.controls.switchcontrol.view;

/**
 * @author CPZ
 */

public record SwitchViewState(float x,
                              float y,
                              float size,
                              int stateIndex,
                              int totalStates,
                              boolean hovering,
                              boolean enabled) {
}

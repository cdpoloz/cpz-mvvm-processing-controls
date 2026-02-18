package com.cpz.processing.controls.buttoncontrol.view;

/**
 * @author CPZ
 */
public record ButtonViewState(float x,
                              float y,
                              float width,
                              float height,
                              String text,
                              boolean enabled,
                              boolean hovered,
                              boolean pressed) {
}

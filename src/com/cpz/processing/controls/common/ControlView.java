package com.cpz.processing.controls.common;

import com.cpz.processing.controls.layout.LayoutConfig;

public interface ControlView {

    void draw();

    void setPosition(float x, float y);

    default void setLayoutConfig(LayoutConfig layoutConfig) {
    }

}

package com.cpz.processing.controls.slidercontrol.view;

public record SliderViewState(float normalizedValue,
                              boolean hovered,
                              boolean pressed,
                              boolean dragging,
                              boolean enabled) {
}

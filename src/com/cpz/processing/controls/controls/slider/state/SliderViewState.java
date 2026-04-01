package com.cpz.processing.controls.controls.slider.state;

public record SliderViewState(float normalizedValue, boolean hovered, boolean pressed, boolean dragging, boolean showText, String displayText, boolean enabled) {
}

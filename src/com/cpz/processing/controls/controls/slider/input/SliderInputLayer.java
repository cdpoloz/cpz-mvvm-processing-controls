package com.cpz.processing.controls.controls.slider.input;

import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single slider instance.
 */
public final class SliderInputLayer extends DefaultInputLayer {
    private final Slider slider;

    public SliderInputLayer(int priority, Slider slider) {
        super(priority);
        this.slider = Objects.requireNonNull(slider, "slider");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null) {
            return false;
        }
        this.slider.handlePointerEvent(event);
        return true;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}

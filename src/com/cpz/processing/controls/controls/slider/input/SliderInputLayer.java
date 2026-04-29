package com.cpz.processing.controls.controls.slider.input;

import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reusable input layer for one or more slider instances.
 *
 * @author CPZ
 */
public final class SliderInputLayer extends DefaultInputLayer {
    private final List<Slider> sliders = new ArrayList<>();

    public SliderInputLayer(int priority, Slider... sliders) {
        super(priority);
        if (sliders != null) {
            for (Slider slider : sliders) {
                addSlider(slider);
            }
        }
    }

    public void addSlider(Slider slider) {
        this.sliders.add(Objects.requireNonNull(slider, "slider"));
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null) {
            return false;
        }
        boolean consumed = false;
        for (Slider slider : this.sliders) {
            consumed = slider.canConsumePointerEvent(event) || consumed;
            slider.handlePointerEvent(event);
        }
        return consumed;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}

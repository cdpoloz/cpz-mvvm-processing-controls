package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import com.cpz.processing.controls.controls.dropdown.config.DropDownConfig;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfig;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfig;
import com.cpz.processing.controls.controls.slider.config.SliderConfig;
import com.cpz.processing.controls.controls.slider.style.SliderRenderStyle;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.controls.textfield.config.TextFieldConfig;
import org.junit.jupiter.api.Test;
import processing.core.PShape;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

class PublicConstructorCompatibilityTest {
    @Test
    void historicalConfigStyleConstructorsRemainAvailable() {
        assertDoesNotThrow(() -> ButtonConfig.StyleConfig.class.getConstructor(
                Integer.class, Integer.class, Integer.class, Float.class, Float.class,
                Float.class, Integer.class, Float.class, Float.class,
                ButtonConfig.RendererConfig.class
        ));
        assertDoesNotThrow(() -> RadioGroupConfig.StyleConfig.class.getConstructor(
                Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Float.class, Float.class, Float.class, Float.class,
                Float.class, Float.class, Float.class, Float.class, Float.class,
                Float.class, Integer.class
        ));
        assertDoesNotThrow(() -> TextFieldConfig.StyleConfig.class.getConstructor(
                Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Integer.class, Float.class
        ));
        assertDoesNotThrow(() -> NumericFieldConfig.StyleConfig.class.getConstructor(
                Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Integer.class, Float.class
        ));
        assertDoesNotThrow(() -> DropDownConfig.StyleConfig.class.getConstructor(
                Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Integer.class, Float.class, Float.class, Float.class,
                Float.class, Float.class, Float.class, Float.class, Float.class,
                Integer.class, Integer.class
        ));
        assertDoesNotThrow(() -> SliderConfig.StyleConfig.class.getConstructor(
                Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Float.class, Float.class, Float.class, Integer.class,
                Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
                Integer.class, Float.class, Float.class, Float.class, Integer.class,
                Integer.class, Boolean.class, SvgColorMode.class, SliderConfig.RendererConfig.class
        ));
    }

    @Test
    void historicalRenderStyleConstructorsUseAmbientTypography() {
        ButtonRenderStyle button = new ButtonRenderStyle(1, 2, 3.0F, 4, 5.0F, true, "text");
        assertNull(button.font());
        assertNull(button.textSize());

        SliderRenderStyle slider = new SliderRenderStyle(
                1, 2, 3.0F, 4.0F, 5, 6, 7, 8.0F,
                9.0F, 10, SvgColorMode.USE_RENDER_STYLE, (PShape) null, true
        );
        assertNull(slider.font());
        assertNull(slider.textSize());
    }
}

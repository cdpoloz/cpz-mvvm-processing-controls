package com.cpz.processing.controls.main;

import java.util.Locale;

import com.cpz.processing.controls.examples.button.*;
import com.cpz.processing.controls.examples.checkbox.*;
import com.cpz.processing.controls.examples.composition.*;
import com.cpz.processing.controls.examples.dropdown.*;
import com.cpz.processing.controls.examples.label.*;
import com.cpz.processing.controls.examples.numericfield.*;
import com.cpz.processing.controls.examples.radiogroup.*;
import com.cpz.processing.controls.examples.slider.*;
import com.cpz.processing.controls.examples.textfield.*;
import com.cpz.processing.controls.examples.theme.*;
import com.cpz.processing.controls.examples.toggle.*;
import processing.core.PApplet;

/**
 * Entry point for launching development sketches.
 * <p>
 * Responsibilities:
 * - Exercise public controls in an interactive sketch.
 * - Provide a development-time validation surface.
 * <p>
 * Behavior:
 * - Targets interactive validation rather than library reuse.
 * <p>
 * Notes:
 * - This type is intended for development and demonstration flows.
 *
 * @author CPZ
 */
public class Launcher {
    /**
     * Starts the public launcher flow.
     *
     * @param args launcher arguments
     *             <p>
     *             Behavior:
     *             - Executes the public operation exposed by this type.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("en-US"));
        // You can choose an example from the list below

        // Button *****************************************
        PApplet.main(ButtonTest.class);
        //PApplet.main(ButtonSvgTest.class);
        //PApplet.main(ButtonJsonTest.class);
        //PApplet.main(ButtonSvgJsonTest.class);

        // Checkbox ***************************************
        //PApplet.main(CheckboxTest.class);
        //PApplet.main(CheckboxJsonTest.class);
        //PApplet.main(CheckboxSvgJsonTest.class);

        // Toggle *****************************************
        //PApplet.main(ToggleTest.class);
        //PApplet.main(ToggleSvgTest.class);
        //PApplet.main(ToggleJsonTest.class);
        //PApplet.main(ToggleSvgJsonTest.class);

        // Slider *****************************************
        //PApplet.main(SliderTest.class);
        //PApplet.main(SliderSvgTest.class);
        //PApplet.main(SliderJsonTest.class);
        //PApplet.main(SliderSvgJsonTest.class);

        // Label ******************************************
        //PApplet.main(LabelTest.class);
        //PApplet.main(LabelJsonTest.class);

        // RadioGroup *************************************
        //PApplet.main(RadioGroupTest.class);
        //PApplet.main(RadioGroupJsonTest.class);

        // TextField *************************************
        //PApplet.main(TextFieldTest.class);
        //PApplet.main(TextFieldJsonTest.class);

        // NumericField **********************************
        //PApplet.main(NumericFieldTest.class);
        //PApplet.main(NumericFieldJsonTest.class);

        // DropDown *************************************
        //PApplet.main(DropDownTest.class);
        //PApplet.main(DropDownJsonTest.class);

        // Composition **********************************
        //PApplet.main(JsonMultiControlUnidirectionalBindingTest.class);
        //PApplet.main(JsonMultiControlBindingTest.class);

        // Theme ****************************************
        //PApplet.main(ThemeFacadeSketch.class);
    }
}

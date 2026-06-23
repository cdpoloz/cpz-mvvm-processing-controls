package src.main.java.com.cpz.processing.controls.main;

import java.util.Locale;

import com.cpz.processing.controls.examples.button.ButtonJsonTest;
import com.cpz.processing.controls.examples.button.ButtonSvgJsonTest;
import com.cpz.processing.controls.examples.button.ButtonTest;
import com.cpz.processing.controls.examples.checkbox.CheckboxJsonTest;
import com.cpz.processing.controls.examples.checkbox.CheckboxSvgJsonTest;
import com.cpz.processing.controls.examples.checkbox.CheckboxTest;
import com.cpz.processing.controls.examples.composition.JsonMultiControlBindingTest;
import com.cpz.processing.controls.examples.composition.JsonMultiControlUnidirectionalBindingTest;
import com.cpz.processing.controls.examples.dropdown.DropDownJsonTest;
import com.cpz.processing.controls.examples.dropdown.DropDownTest;
import com.cpz.processing.controls.examples.label.LabelJsonTest;
import com.cpz.processing.controls.examples.label.LabelTest;
import com.cpz.processing.controls.examples.numericfield.NumericFieldJsonTest;
import com.cpz.processing.controls.examples.numericfield.NumericFieldTest;
import com.cpz.processing.controls.examples.radiogroup.RadioGroupJsonTest;
import com.cpz.processing.controls.examples.radiogroup.RadioGroupTest;
import com.cpz.processing.controls.examples.slider.SliderJsonTest;
import com.cpz.processing.controls.examples.slider.SliderSvgJsonTest;
import com.cpz.processing.controls.examples.slider.SliderSvgTest;
import com.cpz.processing.controls.examples.slider.SliderTest;
import com.cpz.processing.controls.examples.textfield.TextFieldJsonTest;
import com.cpz.processing.controls.examples.textfield.TextFieldTest;
import com.cpz.processing.controls.examples.theme.ThemeFacadeSketch;
import com.cpz.processing.controls.examples.toggle.ToggleJsonTest;
import com.cpz.processing.controls.examples.toggle.ToggleSvgJsonTest;
import com.cpz.processing.controls.examples.toggle.ToggleSvgTest;
import com.cpz.processing.controls.examples.toggle.ToggleTest;

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
        //PApplet.main(com.cpz.processing.controls.examples.button.ButtonSvgTest.class);
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

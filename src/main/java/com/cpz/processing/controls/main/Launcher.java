package com.cpz.processing.controls.main;

import com.cpz.processing.controls.examples.button.ButtonTest;
import processing.core.PApplet;

import java.util.Locale;

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
        //PApplet.main(ButtonRelativeTest.class);
        //PApplet.main(ButtonJsonRelativeTest.class);
        //PApplet.main(ButtonSvgTest.class);
        //PApplet.main(ButtonPngTest.class);
        //PApplet.main(ButtonJsonTest.class);
        //PApplet.main(ButtonSvgJsonTest.class);
        //PApplet.main(ButtonPngJsonTest.class);

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
        //PApplet.main(LabelRelativeJsonTest.class);

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

        // Tooltip **************************************
        //PApplet.main(TooltipVisualTest.class);
        //PApplet.main(TooltipVisualJsonTest.class);

        // Composition **********************************
        //PApplet.main(JsonMultiControlUnidirectionalBindingTest.class);
        //PApplet.main(JsonMultiControlBindingTest.class);

        // Panel ****************************************
        //PApplet.main(PanelVisualTest.class);
        //PApplet.main(PanelDropDownVisualTest.class);
        //PApplet.main(PanelDropDownJsonTest.class);
        //PApplet.main(PanelStyleRuntimeTest.class);

        // Indicator ************************************
        //PApplet.main(IndicatorTest.class);
        //PApplet.main(IndicatorJsonTest.class);
        //PApplet.main(IndicatorSvgTest.class);
        //PApplet.main(IndicatorSvgJsonTest.class);
        //PApplet.main(IndicatorGraphicTest.class);
        //PApplet.main(IndicatorGraphicJsonTest.class);

        // ProgressBar ************************************
        //PApplet.main(ProgressBarTest.class);
        //PApplet.main(ProgressBarJsonTest.class);

        // Notification ***********************************
        //PApplet.main(NotificationTest.class);
        //PApplet.main(NotificationJsonTest.class);

        // Theme ****************************************
        //PApplet.main(ThemeFacadeSketch.class);
    }
}

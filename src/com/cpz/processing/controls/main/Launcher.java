package com.cpz.processing.controls.main;

import java.util.Locale;

import com.cpz.processing.controls.examples.*;
import processing.core.PApplet;

/**
 * Entry point for launching development sketches.
 *
 * Responsibilities:
 * - Exercise public controls in an interactive sketch.
 * - Provide a development-time validation surface.
 *
 * Behavior:
 * - Targets interactive validation rather than library reuse.
 *
 * Notes:
 * - This type is intended for development and demonstration flows.
 */
public class Launcher {
   /**
    * Starts the public launcher flow.
    *
    * @param args launcher arguments
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static void main(String[] args) {
      Locale.setDefault(Locale.forLanguageTag("en-US"));
      // You can choose an example from the list below
      // Button
      //PApplet.main(ButtonTest.class);
      //PApplet.main(ButtonSvgTest.class);
      //PApplet.main(ButtonJsonTest.class);
      PApplet.main(ButtonSvgJsonTest.class);
      //PApplet.main(CheckboxDevSketch.class);
      //PApplet.main(DropDownDevSketch.class);
      //PApplet.main(LabelDevSketch.class);
      //PApplet.main(NumericFieldDevSketch.class);
      //PApplet.main(RadioGroupDevSketch.class);
      //PApplet.main(SliderDevSketch.class);
      //PApplet.main(BindingDevSketch.class);
      //PApplet.main(ToggleDevSketch.class);
      //PApplet.main(TextFieldDevSketch.class);
      //PApplet.main(ThemeDevSketch.class);
      //PApplet.main(BindingDevSketch.class);
   }
}

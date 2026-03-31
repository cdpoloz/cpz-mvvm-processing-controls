package com.cpz.processing.controls.main;

import com.cpz.processing.controls.dev.*;
import processing.core.PApplet;

import java.util.Locale;

/**
 * @author CPZ
 */
public class Launcher {

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("en-US"));
        //PApplet.main(SwitchDevSketch.class);
        //PApplet.main(LabelDevSketch.class);
        //PApplet.main(ButtonDevSketch.class);
        //PApplet.main(CheckboxDevSketch.class);
        //PApplet.main(SliderDevSketch.class);
        PApplet.main(TextFieldDevSketch.class);
    }

}

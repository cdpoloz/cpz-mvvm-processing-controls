package com.cpz.processing.controls.examples.label;

import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.LabelFactory;
import com.cpz.processing.controls.controls.label.config.LabelConfig;
import com.cpz.processing.controls.controls.label.config.LabelConfigLoader;
import processing.core.PApplet;

import java.io.File;

public class LabelJsonTest extends PApplet {
    private static final String LABEL_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "label-test.json";

    private Label label;

    public void settings() {
        size(600, 260);
        smooth(8);
    }

    public void setup() {
        LabelConfigLoader loader = new LabelConfigLoader(this);
        LabelConfig config = loader.load(LABEL_CONFIG_PATH);
        label = LabelFactory.create(this, config);
    }

    public void draw() {
        background(28);
        label.draw();
        fill(180);
        textAlign(CENTER, CENTER);
        text(label.getCode() + " | config-driven label", 300, 215);
    }
}

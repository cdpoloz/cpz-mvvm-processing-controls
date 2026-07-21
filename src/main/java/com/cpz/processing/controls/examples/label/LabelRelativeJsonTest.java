package com.cpz.processing.controls.examples.label;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.panel.Panel;
import processing.core.PApplet;

import java.io.File;
import java.util.Map;

/**
 * Visual check for JSON fonts combined with relative label bounds and text size.
 *
 * @author CPZ
 */
public class LabelRelativeJsonTest extends PApplet {
    private static final String CONFIG_PATH = "data" + File.separator + "config" + File.separator + "label-relative.json";

    private Label rootRelative;
    private Label absoluteTopLevel;
    private Label panelRelative;
    private Panel panel;

    public static void main(String[] args) {
        PApplet.main(LabelRelativeJsonTest.class);
    }

    public void settings() {
        size(900, 460, P2D);
        smooth(8);
    }

    public void setup() {
        Map<String, Control> controls = new ControlConfigLoader(this).load(CONFIG_PATH);
        this.panel = (Panel) controls.get("pnlLabelRelative");
        this.rootRelative = (Label) controls.get("lblRootRelative");
        this.panelRelative = (Label) controls.get("lblPanelRelative");
        this.absoluteTopLevel = (Label) controls.get("lblAbsoluteTopLevel");
        this.panel.add(this.panelRelative);
    }

    public void draw() {
        background(24, 27, 31);
        this.rootRelative.draw();
        this.absoluteTopLevel.draw();
        this.panel.draw();
    }
}

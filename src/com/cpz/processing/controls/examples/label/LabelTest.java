package com.cpz.processing.controls.examples.label;

import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class LabelTest extends PApplet {
    private Label label;

    public void settings() {
        size(600, 260);
        smooth(8);
    }

    public void setup() {
        float x = 120f;
        float y = 70f;
        float w = 360f;
        float h = 100f;
        label = new Label(this, "lblTest", "Label facade\nprogrammatic example", x, y, w, h);
        // style
        LabelStyleConfig lsc = new LabelStyleConfig();
        lsc.textSize = 24.0f;
        lsc.textColor = Colors.rgb(210, 228, 255);
        lsc.lineSpacingMultiplier = 1.2f;
        lsc.alignX = HorizontalAlign.CENTER;
        lsc.alignY = VerticalAlign.CENTER;
        lsc.disabledAlpha = 80;
        label.setStyle(new DefaultLabelStyle(lsc));
    }

    public void draw() {
        background(28);
        label.draw();
        fill(180);
        textAlign(CENTER, CENTER);
        text(label.getCode() + " | non-interactive label", 300, 215);
    }
}

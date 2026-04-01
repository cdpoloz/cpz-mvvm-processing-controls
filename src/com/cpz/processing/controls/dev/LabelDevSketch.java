package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import processing.core.PApplet;

public class LabelDevSketch extends PApplet {
    private LabelView labelDefault;
    private LabelView labelMultiline;
    private LabelView labelCentered;
    private LabelView labelDisabled;
    private LabelView labelCustom;

    public void settings() {
        this.size(900, 600);
    }

    public void setup() {
        LabelViewModel var1 = new LabelViewModel(new LabelModel());
        var1.setText("Default Label");
        this.labelDefault = new LabelView(this, var1, 50.0F, 80.0F);
        LabelViewModel var2 = new LabelViewModel(new LabelModel());
        var2.setText("Line 1\nLine 2\nLine 3");
        this.labelMultiline = new LabelView(this, var2, 50.0F, 150.0F);
        LabelViewModel var3 = new LabelViewModel(new LabelModel());
        var3.setText("Centered\nBlock");
        this.labelCentered = new LabelView(this, var3, 0.0F, 0.0F);
        this.labelCentered.centerBlockAround((float) this.width / 2.0F, 250.0F);
        LabelViewModel var4 = new LabelViewModel(new LabelModel());
        var4.setText("Disabled Label");
        var4.setEnabled(false);
        this.labelDisabled = new LabelView(this, var4, 50.0F, 350.0F);
        LabelViewModel var5 = new LabelViewModel(new LabelModel());
        var5.setText("Custom\nStyle");
        LabelStyleConfig var6 = new LabelStyleConfig();
        var6.textSize = 24.0F;
        var6.textColor = this.color(0, 120, 220);
        var6.alignX = HorizontalAlign.CENTER;
        var6.alignY = VerticalAlign.TOP;
        var6.lineSpacingMultiplier = 1.5F;
        var6.disabledAlpha = 60;
        this.labelCustom = new LabelView(this, var5, 0.0F, 0.0F);
        this.labelCustom.setStyle(new DefaultLabelStyle(var6));
        this.labelCustom.centerBlockAround((float) this.width / 2.0F, 450.0F);
    }

    public void draw() {
        this.background(240);
        this.labelDefault.draw();
        this.labelMultiline.draw();
        this.labelCentered.draw();
        this.labelDisabled.draw();
        this.labelCustom.draw();
    }

    public void keyReleased() {
        if (this.key == ESC) this.key = 0;
    }

    public void keyPressed() {
        if (key == ESC) key = 0;
    }

    public void keyTyped() {
        if (key == ESC) key = 0;
    }
}

package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.labelcontrol.LabelModel;
import com.cpz.processing.controls.labelcontrol.style.LabelStyleConfig;
import com.cpz.processing.controls.labelcontrol.style.render.DefaultTextRenderer;
import com.cpz.processing.controls.labelcontrol.view.LabelView;
import com.cpz.processing.controls.labelcontrol.view.LabelViewModel;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class LabelDevSketch extends PApplet {

    private LabelView labelDefault;
    private LabelView labelMultiline;
    private LabelView labelCentered;
    private LabelView labelDisabled;
    private LabelView labelCustom;

    @Override
    public void settings() {
        size(900, 600);
    }

    @Override
    public void setup() {

        // A) Label con estilo por defecto.
        LabelViewModel vmDefault = new LabelViewModel(new LabelModel());
        vmDefault.setText("Default Label");
        labelDefault = new LabelView(this, vmDefault, 50, 80);

        // B) Multilínea con estilo por defecto.
        LabelViewModel vmMultiline = new LabelViewModel(new LabelModel());
        vmMultiline.setText("Línea 1\nLínea 2\nLínea 3");
        labelMultiline = new LabelView(this, vmMultiline, 50, 150);

        // C) Bloque centrado usando centerBlockAround(...).
        LabelViewModel vmCentered = new LabelViewModel(new LabelModel());
        vmCentered.setText("Bloque\nCentrado");
        labelCentered = new LabelView(this, vmCentered, 0, 0);
        labelCentered.centerBlockAround(width / 2f, 250);

        // D) Label deshabilitado para probar disabledAlpha.
        LabelViewModel vmDisabled = new LabelViewModel(new LabelModel());
        vmDisabled.setText("Label Deshabilitado");
        vmDisabled.setEnabled(false);
        labelDisabled = new LabelView(this, vmDisabled, 50, 350);

        // E) Estilo personalizado: fuente, tamaño, color, alineación y espaciado.
        LabelViewModel vmCustom = new LabelViewModel(new LabelModel());
        vmCustom.setText("Estilo\nPersonalizado");
        LabelStyleConfig customConfig = new LabelStyleConfig();
        customConfig.textSize = 24f;
        customConfig.textColor = color(0, 120, 220);
        customConfig.alignX = PApplet.CENTER;
        customConfig.alignY = PApplet.TOP;
        customConfig.lineSpacingMultiplier = 1.5f;
        customConfig.disabledAlpha = 60;
        labelCustom = new LabelView(this, vmCustom, 0, 0);
        labelCustom.setStyle(new DefaultTextRenderer(customConfig));
        labelCustom.centerBlockAround(width / 2f, 450);
    }

    @Override
    public void draw() {
        background(240);
        labelDefault.draw();
        labelMultiline.draw();
        labelCentered.draw();
        labelDisabled.draw();
        labelCustom.draw();
    }
}

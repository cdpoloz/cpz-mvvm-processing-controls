package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.hit.CircleHitTest;
import com.cpz.processing.controls.switchcontrol.SwitchInputAdapter;
import com.cpz.processing.controls.switchcontrol.SwitchModel;
import com.cpz.processing.controls.switchcontrol.style.ParametricSwitchStyle;
import com.cpz.processing.controls.switchcontrol.style.render.RectShapeRenderer;
import com.cpz.processing.controls.switchcontrol.style.SwitchStyleConfig;
import com.cpz.processing.controls.switchcontrol.view.SwitchView;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewModel;
import com.cpz.processing.controls.util.Colors;
import com.cpz.processing.controls.hit.RectHitTest;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class SwitchDevSketch extends PApplet {

    private SwitchViewModel swViewModel1, swViewModel2;
    private SwitchView swView1, swView2;
    private SwitchInputAdapter swInput1, swInput2;

    @Override
    public void settings() {
        size(600, 400);
        smooth(4);
    }

    @Override
    public void setup() {
        noStroke();
        /*
        Switch 1:
        - 2 estados
        - estilo por defecto (circular)
        - hit test circular (acorde con el estilo)
        */
        swInput1 = new SwitchInputAdapter();
        swViewModel1 = new SwitchViewModel(new SwitchModel());
        swViewModel1.setTotalStates(2);
        swView1 = new SwitchView(this, swViewModel1, 200, 200, 60);
        swView1.setHitTest(new CircleHitTest());
        /*
        Switch 2:
        - 3 estados
        - estilo personalizado (rectangular)
        */
        swInput2 = new SwitchInputAdapter();
        swViewModel2 = new SwitchViewModel(new SwitchModel());
        swViewModel2.setTotalStates(3);
        swView2 = new SwitchView(this, swViewModel2, 400, 200, 40, 30);
        SwitchStyleConfig cfg2 = new SwitchStyleConfig();
        cfg2.shape = new RectShapeRenderer();
        cfg2.colorEstados = new int[]{Colors.rgb(0, 192, 192), Colors.rgb(192, 0, 192), Colors.rgb(192, 192, 0)};
        cfg2.colorStroke = Colors.gray(255);
        cfg2.strokeWidth = 2;
        cfg2.strokeWidthHover = 4;
        cfg2.alfaDeshabilitado = 60;
        cfg2.esquinaRadio = 6;
        swView2.setStyle(new ParametricSwitchStyle(cfg2));
        swView2.setHitTest(new RectHitTest());
    }

    @Override
    public void draw() {
        update();
        dibujar();
    }

    private void update() {
        swInput1.update(this, swView1, swViewModel1);
        swInput2.update(this, swView2, swViewModel2);
        swViewModel2.setEnabled(swViewModel1.getState() == 1);
    }

    private void dibujar() {
        background(32);
        dibujarElementos();
        dibujarDebug();
    }

    private void dibujarElementos() {
        swView1.draw();
        swView2.draw();
    }

    private void dibujarDebug() {
        pushStyle();
        fill(220);
        textAlign(CENTER);
        text("Switch 2 estados", 200, 120);
        text("Estado: " + swViewModel1.getState(), 200, 140);
        text("Switch 3 estados", 400, 120);
        text("Estado: " + swViewModel2.getState(), 400, 140);
        textAlign(LEFT);
        String s = "Pruebas:\n"
                + "- Click cambia estado en release\n"
                + "- Arrastrar fuera cancela\n" +
                "- Hover visible\n"
                + "- Estados cíclicos";
        text(s, 20, height - 80);
        popStyle();
    }
}

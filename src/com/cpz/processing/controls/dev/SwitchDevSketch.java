package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.hit.CircleHitTest;
import com.cpz.processing.controls.switchcontrol.SwitchInputAdapter;
import com.cpz.processing.controls.switchcontrol.SwitchModel;
import com.cpz.processing.controls.switchcontrol.style.ParametricSwitchStyle;
import com.cpz.processing.controls.switchcontrol.style.SwitchStyleConfig;
import com.cpz.processing.controls.switchcontrol.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.switchcontrol.style.render.SvgShapeRenderer;
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
        size(760, 440);
        smooth(4);
    }

    @Override
    public void setup() {
        noStroke();
        /*
        Switch 1:
        - 2 states
        - default circular style
        - circular hit test matching the style
        */
        swViewModel1 = new SwitchViewModel(new SwitchModel());
        swViewModel1.setTotalStates(2);
        swView1 = new SwitchView(this, swViewModel1, 220, 220, 76);
        swView1.setHitTest(new CircleHitTest());
        swView1.setStyle(new ParametricSwitchStyle(createCircularConfig()));
        swInput1 = new SwitchInputAdapter(swView1, swViewModel1);
        /*
        Switch 2:
        - 3 states
        - SVG renderer scaling to view width/height
        */
        swViewModel2 = new SwitchViewModel(new SwitchModel());
        swViewModel2.setTotalStates(3);
        swView2 = new SwitchView(this, swViewModel2, 540, 220, 120, 100);
        swView2.setStyle(new ParametricSwitchStyle(createSvgConfig()));
        swView2.setHitTest(new RectHitTest());
        swInput2 = new SwitchInputAdapter(swView2, swViewModel2);
    }

    @Override
    public void draw() {
        update();
        dibujar();
    }

    @Override
    public void keyReleased() {
        if (key == ESC) key = 0;
    }

    @Override
    public void mouseMoved() {
        swInput1.handleMouseMove(mouseX, mouseY);
        swInput2.handleMouseMove(mouseX, mouseY);
    }

    @Override
    public void mouseDragged() {
        mouseMoved();
    }

    @Override
    public void mousePressed() {
        swInput1.handleMousePress(mouseX, mouseY);
        swInput2.handleMousePress(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        swInput1.handleMouseRelease(mouseX, mouseY);
        swInput2.handleMouseRelease(mouseX, mouseY);
    }

    private void update() {
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
        text("Circle Renderer", 220, 110);
        text("State: " + swViewModel1.getState(), 220, 132);
        text("SVG Renderer", 540, 110);
        text("State: " + swViewModel2.getState(), 540, 132);
        textAlign(LEFT);
        String s = "Checks:\n"
                + "- Click changes state on release\n"
                + "- Dragging outside cancels\n"
                + "- Hover/pressed modify final fill\n"
                + "- SVG switch is disabled while circle is OFF\n"
                + "- SVG scales using SwitchView width/height";
        text(s, 24, height - 108);
        text("SVG enabled: " + swViewModel2.isEnabled(), 420, 344);
        text("Circle hovered: " + swViewModel1.isHovered(), 420, 366);
        text("SVG pressed: " + swViewModel2.isPressed(), 420, 388);
        popStyle();
    }

    private SwitchStyleConfig createCircularConfig() {
        SwitchStyleConfig config = new SwitchStyleConfig();
        config.setShapeRenderer(new CircleShapeRenderer());
        config.stateColors = new int[]{Colors.gray(60), Colors.rgb(48, 186, 96)};
        config.strokeColor = Colors.gray(255);
        config.strokeWidth = 2f;
        config.strokeWidthHover = 4f;
        config.hoverBlendWithWhite = 0.18f;
        config.pressedBlendWithBlack = 0.20f;
        config.disabledAlpha = 70;
        return config;
    }

    private SwitchStyleConfig createSvgConfig() {
        SwitchStyleConfig config = new SwitchStyleConfig();
        config.setShapeRenderer(new SvgShapeRenderer(this, "data/img/test.svg"));
        config.stateColors = new int[]{
                Colors.rgb(80, 100, 220),
                Colors.rgb(235, 160, 40),
                Colors.rgb(32, 188, 176)
        };
        config.strokeColor = Colors.gray(255);
        config.strokeWidth = 1.5f;
        config.strokeWidthHover = 3.5f;
        config.hoverBlendWithWhite = 0.14f;
        config.pressedBlendWithBlack = 0.24f;
        config.disabledAlpha = 70;
        return config;
    }
}

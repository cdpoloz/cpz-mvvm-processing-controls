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
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import com.cpz.processing.controls.util.Colors;
import com.cpz.processing.controls.hit.RectHitTest;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class SwitchDevSketch extends PApplet {

    private final InputManager inputManager = new InputManager();

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

        inputManager.registerLayer(new SwitchRootInputLayer());
    }

    @Override
    public void draw() {
        update();
        dibujar();
    }

    @Override
    public void keyReleased() {
        if (key == ESC) key = 0;
        inputManager.dispatchKeyboard(new KeyboardEvent(
                KeyboardEvent.Type.RELEASE,
                key,
                keyCode,
                keyEvent != null && keyEvent.isShiftDown(),
                keyEvent != null && keyEvent.isControlDown(),
                keyEvent != null && keyEvent.isAltDown()
        ));
    }

    @Override
    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, mouseX, mouseY, mouseButton));
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

    private final class SwitchRootInputLayer extends DefaultInputLayer {

        private SwitchRootInputLayer() {
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case MOVE:
                case DRAG:
                    swInput1.handleMouseMove(event.getX(), event.getY());
                    swInput2.handleMouseMove(event.getX(), event.getY());
                    return true;

                case PRESS:
                    swInput1.handleMousePress(event.getX(), event.getY());
                    swInput2.handleMousePress(event.getX(), event.getY());
                    return true;

                case RELEASE:
                    swInput1.handleMouseRelease(event.getX(), event.getY());
                    swInput2.handleMouseRelease(event.getX(), event.getY());
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            return false;
        }
    }
}

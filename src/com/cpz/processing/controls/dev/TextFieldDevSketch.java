package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.common.input.KeyboardInputAdapter;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import com.cpz.processing.controls.textfieldcontrol.input.TextFieldInputAdapter;
import com.cpz.processing.controls.textfieldcontrol.model.TextFieldModel;
import com.cpz.processing.controls.textfieldcontrol.style.DefaultTextFieldStyle;
import com.cpz.processing.controls.textfieldcontrol.style.TextFieldStyleConfig;
import com.cpz.processing.controls.textfieldcontrol.view.TextFieldView;
import com.cpz.processing.controls.textfieldcontrol.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;
import processing.core.PFont;

public class TextFieldDevSketch extends PApplet {

    private final FocusManager focusManager = new FocusManager();
    private final InputManager inputManager = new InputManager();

    private TextFieldView customFontView;
    private TextFieldView defaultFontView;
    private TextFieldInputAdapter customFontInput;
    private TextFieldInputAdapter defaultFontInput;
    private KeyboardInputAdapter keyboardAdapter;

    @Override
    public void settings() {
        size(980, 420);
        smooth(4);
    }

    @Override
    public void setup() {
        PFont abel = createFont("data/font/abel-regular.ttf", 16, true);
        keyboardAdapter = new KeyboardInputAdapter(focusManager);

        TextFieldViewModel customFontViewModel = new TextFieldViewModel(new TextFieldModel());
        customFontViewModel.setText("Custom font field");
        customFontView = new TextFieldView(this, customFontViewModel, width * 0.5f, 140f, 420f, 46f);
        customFontView.setStyle(new DefaultTextFieldStyle(createCustomFontStyle(abel)));
        customFontInput = new TextFieldInputAdapter(customFontView, customFontViewModel, focusManager);

        TextFieldViewModel defaultFontViewModel = new TextFieldViewModel(new TextFieldModel());
        defaultFontViewModel.setText("Default font field");
        defaultFontView = new TextFieldView(this, defaultFontViewModel, width * 0.5f, 250f, 420f, 46f);
        defaultFontView.setStyle(new DefaultTextFieldStyle(createDefaultFontStyle()));
        defaultFontInput = new TextFieldInputAdapter(defaultFontView, defaultFontViewModel, focusManager);

        inputManager.registerLayer(new TextFieldRootInputLayer());
    }

    @Override
    public void draw() {
        background(242);
        drawTitles();
        customFontView.draw();
        defaultFontView.draw();
    }

    @Override
    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, mouseX, mouseY, mouseButton));
    }

    @Override
    public void keyPressed() {
        if (key == ESC) {
            key = 0;
        }
        inputManager.dispatchKeyboard(new KeyboardEvent(
                KeyboardEvent.Type.PRESS,
                key,
                keyCode,
                keyEvent != null && keyEvent.isShiftDown(),
                keyEvent != null && keyEvent.isControlDown(),
                keyEvent != null && keyEvent.isAltDown()
        ));
    }

    @Override
    public void keyReleased() {
        if (key == ESC) {
            key = 0;
        }
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
    public void keyTyped() {
        inputManager.dispatchKeyboard(new KeyboardEvent(
                KeyboardEvent.Type.TYPE,
                key,
                keyCode,
                keyEvent != null && keyEvent.isShiftDown(),
                keyEvent != null && keyEvent.isControlDown(),
                keyEvent != null && keyEvent.isAltDown()
        ));
    }

    private void drawTitles() {
        pushStyle();
        fill(36);
        textAlign(CENTER, CENTER);
        textSize(20);
        text("TextField Dev Sketch", width * 0.5f, 52f);
        textSize(14);
        text("Custom font: Abel from data/font/abel-regular.ttf", width * 0.5f, 88f);
        text("Default font: Processing fallback when font is null", width * 0.5f, 198f);
        popStyle();
    }

    private TextFieldStyleConfig createCustomFontStyle(PFont font) {
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.backgroundColor = Colors.rgb(248, 245, 238);
        config.borderColor = Colors.rgb(167, 93, 68);
        config.textColor = Colors.rgb(48, 34, 28);
        config.cursorColor = Colors.rgb(208, 96, 48);
        config.selectionColor = Colors.rgb(228, 197, 168);
        config.selectionTextColor = Colors.rgb(48, 34, 28);
        config.textSize = 18f;
        config.font = font;
        return config;
    }

    private TextFieldStyleConfig createDefaultFontStyle() {
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.backgroundColor = Colors.rgb(236, 242, 248);
        config.borderColor = Colors.rgb(72, 116, 156);
        config.textColor = Colors.rgb(28, 44, 62);
        config.cursorColor = Colors.rgb(38, 132, 212);
        config.selectionColor = Colors.rgb(182, 217, 248);
        config.selectionTextColor = Colors.rgb(28, 44, 62);
        config.textSize = 16f;
        config.font = null;
        return config;
    }

    private final class TextFieldRootInputLayer extends DefaultInputLayer {

        private TextFieldRootInputLayer() {
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case PRESS:
                    boolean handled = customFontInput.handleMousePress(event.getX(), event.getY());
                    if (!handled) {
                        handled = defaultFontInput.handleMousePress(event.getX(), event.getY());
                    }
                    if (!handled) {
                        focusManager.clearFocus();
                    }
                    return true;

                case DRAG:
                    customFontInput.handleMouseDrag(event.getX(), event.getY());
                    defaultFontInput.handleMouseDrag(event.getX(), event.getY());
                    return true;

                case RELEASE:
                    customFontInput.handleMouseRelease();
                    defaultFontInput.handleMouseRelease();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            switch (event.getType()) {
                case PRESS:
                    keyboardAdapter.handleKeyboardEvent(event);
                    return true;

                case TYPE:
                    keyboardAdapter.handleKeyboardEvent(event);
                    return true;

                default:
                    return false;
            }
        }
    }
}

package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.common.input.KeyboardInputAdapter;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import com.cpz.processing.controls.numericfieldcontrol.model.NumericFieldModel;
import com.cpz.processing.controls.numericfieldcontrol.style.NumericFieldDefaultStyles;
import com.cpz.processing.controls.numericfieldcontrol.view.NumericFieldInputAdapter;
import com.cpz.processing.controls.numericfieldcontrol.view.NumericFieldView;
import com.cpz.processing.controls.numericfieldcontrol.viewmodel.NumericFieldViewModel;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.math.BigDecimal;

public class NumericFieldDevSketch extends PApplet {

    private final FocusManager focusManager = new FocusManager();
    private final InputManager inputManager = new InputManager();

    private NumericFieldView primaryFieldView;
    private NumericFieldView secondaryFieldView;
    private NumericFieldViewModel primaryFieldViewModel;
    private NumericFieldViewModel secondaryFieldViewModel;
    private NumericFieldInputAdapter primaryFieldInput;
    private NumericFieldInputAdapter secondaryFieldInput;
    private KeyboardInputAdapter keyboardAdapter;
    private String lastChangeMessage = "No committed change yet";
    private int externalUpdateCount;

    @Override
    public void settings() {
        size(980, 460);
        smooth(4);
    }

    @Override
    public void setup() {
        keyboardAdapter = new KeyboardInputAdapter(focusManager);

        primaryFieldViewModel = new NumericFieldViewModel(new NumericFieldModel(
                new BigDecimal("12.50"),
                BigDecimal.ZERO,
                new BigDecimal("99.99"),
                new BigDecimal("0.25"),
                false,
                true,
                2
        ));
        primaryFieldView = new NumericFieldView(this, primaryFieldViewModel, width * 0.5f, 145f, 380f, 46f);
        primaryFieldView.setStyle(NumericFieldDefaultStyles.standard());
        primaryFieldInput = new NumericFieldInputAdapter(primaryFieldView, primaryFieldViewModel, focusManager);
        primaryFieldViewModel.setOnValueChanged(value -> lastChangeMessage = "Primary committed -> " + value.toPlainString());

        secondaryFieldViewModel = new NumericFieldViewModel(new NumericFieldModel(
                new BigDecimal("-5"),
                new BigDecimal("-10"),
                new BigDecimal("10"),
                BigDecimal.ONE,
                true,
                false,
                0
        ));
        secondaryFieldView = new NumericFieldView(this, secondaryFieldViewModel, width * 0.5f, 260f, 380f, 46f);
        secondaryFieldView.setStyle(NumericFieldDefaultStyles.standard());
        secondaryFieldInput = new NumericFieldInputAdapter(secondaryFieldView, secondaryFieldViewModel, focusManager);
        secondaryFieldViewModel.setOnValueChanged(value -> lastChangeMessage = "Secondary committed -> " + value.toPlainString());

        inputManager.registerLayer(new NumericFieldRootInputLayer());
    }

    @Override
    public void draw() {
        background(242);
        drawTitles();
        primaryFieldView.draw();
        secondaryFieldView.draw();
        drawDebug();
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
    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                mouseX,
                mouseY,
                mouseButton,
                event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
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
        text("NumericField Dev Sketch", width * 0.5f, 52f);
        textSize(14);
        text("Type numbers, try -, ., -., 0., use arrows, wheel, SHIFT x10, CTRL /10, and press 'u' for external update.", width * 0.5f, 86f);
        popStyle();
    }

    private void drawDebug() {
        pushStyle();
        fill(36);
        textAlign(LEFT, TOP);
        text("Primary value: " + primaryFieldViewModel.getValue().toPlainString(), 90, 330);
        text("Primary text: " + primaryFieldViewModel.getText(), 90, 352);
        text("Primary focused/editing: " + primaryFieldViewModel.isFocused() + " / " + primaryFieldViewModel.isEditing(), 90, 374);
        text("Secondary value: " + secondaryFieldViewModel.getValue().toPlainString(), 90, 404);
        text("Secondary text: " + secondaryFieldViewModel.getText(), 420, 330);
        text("Secondary focused/editing: " + secondaryFieldViewModel.isFocused() + " / " + secondaryFieldViewModel.isEditing(), 420, 352);
        text("Last change: " + lastChangeMessage, 420, 378, 460, 32);
        text("External updates: " + externalUpdateCount, 420, 404);
        text("Checks: '-', '.', '-.', '0.' stay editable, invalid chars rejected, commit fallback, arrows/wheel clamp, external sync when not editing.", 420, 426, 460, 48);
        popStyle();
    }

    private final class NumericFieldRootInputLayer extends DefaultInputLayer {

        private NumericFieldRootInputLayer() {
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case MOVE:
                    primaryFieldInput.handleMouseMove(event.getX(), event.getY());
                    secondaryFieldInput.handleMouseMove(event.getX(), event.getY());
                    return true;

                case DRAG:
                    primaryFieldInput.handleMouseDrag(event.getX(), event.getY());
                    secondaryFieldInput.handleMouseDrag(event.getX(), event.getY());
                    return true;

                case PRESS:
                    boolean handled = primaryFieldInput.handleMousePress(event.getX(), event.getY());
                    if (!handled) {
                        handled = secondaryFieldInput.handleMousePress(event.getX(), event.getY());
                    }
                    if (!handled) {
                        focusManager.clearFocus();
                    }
                    return true;

                case RELEASE:
                    primaryFieldInput.handleMouseRelease(event.getX(), event.getY());
                    secondaryFieldInput.handleMouseRelease(event.getX(), event.getY());
                    return true;

                case WHEEL:
                    primaryFieldInput.handleMouseWheel(event.getWheelDelta(), event.isShiftDown(), event.isControlDown());
                    secondaryFieldInput.handleMouseWheel(event.getWheelDelta(), event.isShiftDown(), event.isControlDown());
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            if (event.getType() == KeyboardEvent.Type.PRESS && (event.getKey() == 'u' || event.getKey() == 'U')) {
                externalUpdateCount++;
                if (primaryFieldViewModel.isFocused()) {
                    primaryFieldViewModel.setValue(new BigDecimal("42.75"));
                    lastChangeMessage = "External primary update #" + externalUpdateCount;
                } else {
                    secondaryFieldViewModel.setValue(new BigDecimal("-3"));
                    lastChangeMessage = "External secondary update #" + externalUpdateCount;
                }
                return true;
            }
            keyboardAdapter.handleKeyboardEvent(event);
            return true;
        }
    }
}

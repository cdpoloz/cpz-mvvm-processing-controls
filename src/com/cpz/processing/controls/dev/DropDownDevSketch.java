package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.buttoncontrol.input.ButtonInputAdapter;
import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.buttoncontrol.style.ButtonDefaultStyles;
import com.cpz.processing.controls.buttoncontrol.view.ButtonView;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.dropdowncontrol.DefaultDropDownStyle;
import com.cpz.processing.controls.dropdowncontrol.DropDownModel;
import com.cpz.processing.controls.dropdowncontrol.DropDownOverlayController;
import com.cpz.processing.controls.dropdowncontrol.DropDownStyleConfig;
import com.cpz.processing.controls.dropdowncontrol.DropDownView;
import com.cpz.processing.controls.dropdowncontrol.DropDownViewModel;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import com.cpz.processing.controls.layout.Anchor;
import com.cpz.processing.controls.layout.LayoutConfig;
import com.cpz.processing.controls.overlay.OverlayEntry;
import com.cpz.processing.controls.overlay.OverlayManager;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

import java.util.List;

public class DropDownDevSketch extends PApplet {

    private static final int ROOT_LAYER_PRIORITY = 0;
    private static final int DROPDOWN_OVERLAY_PRIORITY = 100;

    private final FocusManager focusManager = new FocusManager();
    private final InputManager inputManager = new InputManager();
    private final OverlayManager overlayManager = new OverlayManager();

    private DropDownView firstDropDownView;
    private DropDownViewModel firstDropDownViewModel;
    private DropDownView secondDropDownView;
    private DropDownViewModel secondDropDownViewModel;
    private DropDownOverlayController firstOverlayController;
    private DropDownOverlayController secondOverlayController;
    private ButtonView buttonView;
    private ButtonInputAdapter buttonInputAdapter;
    private String statusText = "Sin interaccion";

    @Override
    public void settings() {
        size(960, 560);
        smooth(4);
    }

    @Override
    public void setup() {
        firstDropDownViewModel = new DropDownViewModel(new DropDownModel(List.of(
                "Option Alpha",
                "Option Beta",
                "Option Gamma",
                "Option Delta",
                "Option Epsilon",
                "Option Zeta",
                "Option Eta"
        ), 1));
        firstDropDownView = new DropDownView(this, firstDropDownViewModel, width * 0.32f, 150f, 280f, 42f);
        firstDropDownView.setStyle(new DefaultDropDownStyle(createDropDownStyle()));
        firstDropDownView.setLayoutConfig(createLayout(0.20f, 0.20f, Anchor.TOP_LEFT));
        focusManager.register(firstDropDownViewModel);

        secondDropDownViewModel = new DropDownViewModel(new DropDownModel(List.of(
                "Red",
                "Green",
                "Blue",
                "Cyan",
                "Magenta",
                "Yellow"
        ), 2));
        secondDropDownView = new DropDownView(this, secondDropDownViewModel, width * 0.68f, 150f, 280f, 42f);
        secondDropDownView.setStyle(new DefaultDropDownStyle(createDropDownStyle()));
        secondDropDownView.setLayoutConfig(createLayout(0.80f, 0.20f, Anchor.TOP_RIGHT));
        focusManager.register(secondDropDownViewModel);

        firstOverlayController = new DropDownOverlayController(
                firstDropDownView,
                firstDropDownViewModel,
                focusManager,
                overlayManager,
                inputManager,
                DROPDOWN_OVERLAY_PRIORITY
        );
        secondOverlayController = new DropDownOverlayController(
                secondDropDownView,
                secondDropDownViewModel,
                focusManager,
                overlayManager,
                inputManager,
                DROPDOWN_OVERLAY_PRIORITY
        );
        firstOverlayController.setTransferHandler(this::handleOverlayTransfer);
        secondOverlayController.setTransferHandler(this::handleOverlayTransfer);

        ButtonViewModel buttonViewModel = new ButtonViewModel(new ButtonModel("Underlying Button"));
        buttonViewModel.setClickListener(() -> statusText = "Button clicked");
        buttonView = new ButtonView(this, buttonViewModel, width * 0.5f, 250f, 240f, 52f);
        buttonView.setStyle(ButtonDefaultStyles.primary());
        buttonView.setLayoutConfig(createLayout(0.50f, 0.45f, Anchor.TOP_CENTER));
        buttonInputAdapter = new ButtonInputAdapter(buttonView, buttonViewModel);

        inputManager.registerLayer(new RootInputLayer());
        syncOverlayControllers();
    }

    @Override
    public void draw() {
        syncOverlayControllers();
        background(246);
        drawFrame();
        buttonView.draw();
        drawCollapsedDropDowns();
        drawNotes();
        drawRegisteredOverlays();
    }

    @Override
    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, mouseX, mouseY, mouseButton));
        syncOverlayControllers();
        DropDownView expandedDropDown = getExpandedDropDown();
        if (expandedDropDown != null) {
            resetHoverForInactiveDropDowns(expandedDropDown);
            buttonInputAdapter.handleMouseMove(-1f, -1f);
        }
    }

    @Override
    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton));
        syncOverlayControllers();
        updateStatusAfterInput();
    }

    @Override
    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, mouseX, mouseY, mouseButton));
        syncOverlayControllers();
    }

    @Override
    public void keyReleased() {
        if (key == ESC) key = 0;
    }

    @Override
    public void keyPressed() {
        if (key == ESC) {
            key = 0;
            overlayManager.getTopOverlay().ifPresent(this::closeTopOverlay);
            syncOverlayControllers();
            updateStatusAfterInput();
            return;
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
    public void exit() {
        firstOverlayController.dispose();
        secondOverlayController.dispose();
        overlayManager.clearAll();
        super.exit();
    }

    private void drawCollapsedDropDowns() {
        if (!firstDropDownViewModel.isExpanded()) {
            firstDropDownView.draw();
        }
        if (!secondDropDownViewModel.isExpanded()) {
            secondDropDownView.draw();
        }
    }

    private void drawRegisteredOverlays() {
        for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private void closeTopOverlay(OverlayEntry overlay) {
        if (overlay.getOnClose() != null) {
            overlay.getOnClose().run();
        }
    }

    private DropDownView getExpandedDropDown() {
        if (firstDropDownViewModel.isExpanded()) {
            return firstDropDownView;
        }
        if (secondDropDownViewModel.isExpanded()) {
            return secondDropDownView;
        }
        return null;
    }

    private void syncOverlayControllers() {
        firstOverlayController.syncRegistration();
        secondOverlayController.syncRegistration();
    }

    private boolean handleOverlayTransfer(DropDownOverlayController source, PointerEvent event) {
        if (event.getType() != PointerEvent.Type.PRESS) {
            return false;
        }
        DropDownView targetView = findClickedDropDown(event.getX(), event.getY());
        if (targetView == null || targetView == source.getView()) {
            return false;
        }
        closeOtherDropDowns(targetView);
        targetView.handleMousePress(event.getX(), event.getY(), focusManager);
        return true;
    }

    private DropDownView findClickedDropDown(float mx, float my) {
        if (firstDropDownView.contains(mx, my)) {
            return firstDropDownView;
        }
        if (secondDropDownView.contains(mx, my)) {
            return secondDropDownView;
        }
        return null;
    }

    private void closeOtherDropDowns(DropDownView activeDropDown) {
        if (activeDropDown != firstDropDownView) {
            firstDropDownViewModel.close();
            firstDropDownView.handleMouseMove(-1f, -1f);
        }
        if (activeDropDown != secondDropDownView) {
            secondDropDownViewModel.close();
            secondDropDownView.handleMouseMove(-1f, -1f);
        }
    }

    private void resetHoverForInactiveDropDowns(DropDownView activeDropDown) {
        if (firstDropDownView != activeDropDown) {
            firstDropDownView.handleMouseMove(-1f, -1f);
        }
        if (secondDropDownView != activeDropDown) {
            secondDropDownView.handleMouseMove(-1f, -1f);
        }
    }

    private void updateStatusAfterInput() {
        if (firstDropDownViewModel.isExpanded()) {
            statusText = "First DropDown expanded";
            return;
        }
        if (secondDropDownViewModel.isExpanded()) {
            statusText = "Second DropDown expanded";
            return;
        }
        statusText = "Selected: first=" + firstDropDownViewModel.getSelectedText()
                + " | second=" + secondDropDownViewModel.getSelectedText();
    }

    private void drawFrame() {
        pushStyle();
        fill(255);
        stroke(Colors.rgb(210, 218, 229));
        rect(54, 54, width - 108, height - 108, 18f);
        popStyle();
    }

    private void drawNotes() {
        pushStyle();
        fill(Colors.rgb(28, 36, 46));
        textAlign(LEFT, TOP);
        textSize(24);
        text("DropDown Overlay Dev Sketch", 92, 86);
        textSize(14);
        text("Input flows through InputManager. Expanded dropdowns register as overlays and capture pointer events by priority.", 92, 116, 720, 40);
        text("Current status: " + statusText, 92, 420);
        text("First hovered item index: " + firstDropDownView.getHoveredIndex(), 92, 446);
        text("Second hovered item index: " + secondDropDownView.getHoveredIndex(), 92, 472);
        text("Active overlays: " + overlayManager.getActiveOverlays().size(), 92, 498);
        popStyle();
    }

    private DropDownStyleConfig createDropDownStyle() {
        DropDownStyleConfig config = new DropDownStyleConfig();
        config.cornerRadius = 10f;
        config.listCornerRadius = 10f;
        config.strokeWeight = 1.5f;
        config.focusedStrokeWeight = 2.5f;
        config.textSize = 16f;
        config.itemHeight = 36f;
        config.textPadding = 12f;
        config.arrowPadding = 18f;
        config.maxVisibleItems = 6;
        return config;
    }

    private LayoutConfig createLayout(float normalizedX, float normalizedY, Anchor anchor) {
        LayoutConfig config = new LayoutConfig(normalizedX, normalizedY);
        config.setAnchor(anchor);
        return config;
    }

    private final class RootInputLayer extends DefaultInputLayer {

        private RootInputLayer() {
            super(ROOT_LAYER_PRIORITY);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case MOVE:
                    handleRootMove(event);
                    return true;

                case PRESS:
                    handleRootPress(event);
                    return true;

                case RELEASE:
                    handleRootRelease(event);
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            return false;
        }

        private void handleRootMove(PointerEvent event) {
            firstDropDownView.handleMouseMove(event.getX(), event.getY());
            secondDropDownView.handleMouseMove(event.getX(), event.getY());
            buttonInputAdapter.handleMouseMove(event.getX(), event.getY());
        }

        private void handleRootPress(PointerEvent event) {
            DropDownView clickedDropDown = findClickedDropDown(event.getX(), event.getY());
            if (clickedDropDown != null) {
                closeOtherDropDowns(clickedDropDown);
                clickedDropDown.handleMousePress(event.getX(), event.getY(), focusManager);
                return;
            }

            if (buttonView.contains(event.getX(), event.getY())) {
                buttonInputAdapter.handleMousePress(event.getX(), event.getY());
                focusManager.clearFocus();
                return;
            }

            focusManager.clearFocus();
        }

        private void handleRootRelease(PointerEvent event) {
            DropDownView expandedDropDown = firstDropDownViewModel.isExpanded()
                    ? firstDropDownView
                    : secondDropDownViewModel.isExpanded() ? secondDropDownView : null;
            if (expandedDropDown != null) {
                expandedDropDown.handleMouseRelease(event.getX(), event.getY());
                resetHoverForInactiveDropDowns(expandedDropDown);
                buttonInputAdapter.handleMouseRelease(-1f, -1f);
                return;
            }

            firstDropDownView.handleMouseRelease(event.getX(), event.getY());
            secondDropDownView.handleMouseRelease(event.getX(), event.getY());
            buttonInputAdapter.handleMouseRelease(event.getX(), event.getY());
        }
    }
}

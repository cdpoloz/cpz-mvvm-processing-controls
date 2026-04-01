package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.controls.button.input.ButtonInputAdapter;
import com.cpz.processing.controls.controls.button.view.ButtonView;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.model.DropDownModel;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.dropdown.util.DropDownOverlayController;
import com.cpz.processing.controls.controls.dropdown.view.DropDownView;
import com.cpz.processing.controls.controls.dropdown.viewmodel.DropDownViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.layout.Anchor;
import com.cpz.processing.controls.core.layout.LayoutConfig;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.processing.controls.core.util.Colors;

import java.util.List;
import java.util.Objects;

import processing.core.PApplet;

public class DropDownDevSketch extends PApplet {
    private static final int ROOT_LAYER_PRIORITY = 0;
    private static final int DROPDOWN_OVERLAY_PRIORITY = 100;
    private final FocusManager focusManager = new FocusManager();
    private final InputManager inputManager = new InputManager();
    private final OverlayManager overlayManager;
    private DropDownView firstDropDownView;
    private DropDownViewModel firstDropDownViewModel;
    private DropDownView secondDropDownView;
    private DropDownViewModel secondDropDownViewModel;
    private DropDownOverlayController firstOverlayController;
    private DropDownOverlayController secondOverlayController;
    private TooltipOverlayController firstTooltipController;
    private TooltipOverlayController secondTooltipController;
    private TooltipOverlayController buttonTooltipController;
    private ButtonView buttonView;
    private ButtonInputAdapter buttonInputAdapter;
    private String statusText;

    public DropDownDevSketch() {
        this.overlayManager = new OverlayManager(this.focusManager);
        this.statusText = "Sin interaccion";
    }

    public void settings() {
        this.size(960, 560);
        this.smooth(4);
    }

    public void setup() {
        this.firstDropDownViewModel = new DropDownViewModel(new DropDownModel(List.of("Option Alpha", "Option Beta", "Option Gamma", "Option Delta", "Option Epsilon", "Option Zeta", "Option Eta"), 1));
        this.firstDropDownView = new DropDownView(this, this.firstDropDownViewModel, (float) this.width * 0.32F, 150.0F, 280.0F, 42.0F);
        this.firstDropDownView.setStyle(new DefaultDropDownStyle(this.createDropDownStyle()));
        this.firstDropDownView.setLayoutConfig(this.createLayout(0.2F, 0.2F, Anchor.TOP_LEFT));
        this.focusManager.register(this.firstDropDownViewModel);
        this.secondDropDownViewModel = new DropDownViewModel(new DropDownModel(List.of("Red", "Green", "Blue", "Cyan", "Magenta", "Yellow"), 2));
        this.secondDropDownView = new DropDownView(this, this.secondDropDownViewModel, (float) this.width * 0.68F, 150.0F, 280.0F, 42.0F);
        this.secondDropDownView.setStyle(new DefaultDropDownStyle(this.createDropDownStyle()));
        this.secondDropDownView.setLayoutConfig(this.createLayout(0.8F, 0.2F, Anchor.TOP_RIGHT));
        this.focusManager.register(this.secondDropDownViewModel);
        this.firstOverlayController = new DropDownOverlayController(this.firstDropDownView, this.firstDropDownViewModel, this.focusManager, this.overlayManager, this.inputManager, 100);
        this.secondOverlayController = new DropDownOverlayController(this.secondDropDownView, this.secondDropDownViewModel, this.focusManager, this.overlayManager, this.inputManager, 100);
        this.firstOverlayController.setTransferHandler(this::handleOverlayTransfer);
        this.secondOverlayController.setTransferHandler(this::handleOverlayTransfer);
        ButtonViewModel var1 = new ButtonViewModel(new ButtonModel("Underlying Button"));
        var1.setClickListener(() -> this.statusText = "Button clicked");
        this.buttonView = new ButtonView(this, var1, (float) this.width * 0.5F, 250.0F, 240.0F, 52.0F);
        this.buttonView.setStyle(ButtonDefaultStyles.primary());
        this.buttonView.setLayoutConfig(this.createLayout(0.5F, 0.45F, Anchor.TOP_CENTER));
        this.buttonInputAdapter = new ButtonInputAdapter(this.buttonView, var1);
        OverlayManager var10004 = this.overlayManager;
        DropDownViewModel var10005 = this.firstDropDownViewModel;
        Objects.requireNonNull(var10005);
        this.firstTooltipController = new TooltipOverlayController(this, var10004, var10005::isHovered, () -> "Choose the first option set", new TooltipOverlayController.AnchorBoundsProvider() {
            {
                Objects.requireNonNull(DropDownDevSketch.this);
            }

            public float getCenterX() {
                return DropDownDevSketch.this.firstDropDownView.getX();
            }

            public float getTopY() {
                return DropDownDevSketch.this.firstDropDownView.getY() - DropDownDevSketch.this.firstDropDownView.getHeight() * 0.5F;
            }
        });
        var10004 = this.overlayManager;
        var10005 = this.secondDropDownViewModel;
        Objects.requireNonNull(var10005);
        this.secondTooltipController = new TooltipOverlayController(this, var10004, var10005::isHovered, () -> "Choose the color set", new TooltipOverlayController.AnchorBoundsProvider() {
            {
                Objects.requireNonNull(DropDownDevSketch.this);
            }

            public float getCenterX() {
                return DropDownDevSketch.this.secondDropDownView.getX();
            }

            public float getTopY() {
                return DropDownDevSketch.this.secondDropDownView.getY() - DropDownDevSketch.this.secondDropDownView.getHeight() * 0.5F;
            }
        });
        this.buttonTooltipController = new TooltipOverlayController(this, this.overlayManager, () -> var1.isHovered(), () -> "Tooltip overlays do not block the button", new TooltipOverlayController.AnchorBoundsProvider() {
            {
                Objects.requireNonNull(DropDownDevSketch.this);
            }

            public float getCenterX() {
                return DropDownDevSketch.this.buttonView.getX();
            }

            public float getTopY() {
                return DropDownDevSketch.this.buttonView.getY() - DropDownDevSketch.this.buttonView.getHeight() * 0.5F;
            }
        });
        this.inputManager.registerLayer(new RootInputLayer());
        this.syncOverlayControllers();
    }

    public void draw() {
        this.syncOverlayControllers();
        this.syncTooltipControllers();
        this.background(246);
        this.drawFrame();
        this.buttonView.draw();
        this.drawCollapsedDropDowns();
        this.drawNotes();
        this.drawRegisteredOverlays();
    }

    public void mouseMoved() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
        this.syncOverlayControllers();
        DropDownView var1 = this.getExpandedDropDown();
        if (var1 != null) {
            this.resetHoverForInactiveDropDowns(var1);
            this.buttonInputAdapter.handleMouseMove(-1.0F, -1.0F);
        }

    }

    public void mousePressed() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
        this.syncOverlayControllers();
        this.updateStatusAfterInput();
    }

    public void mouseReleased() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
        this.syncOverlayControllers();
    }

    public void keyPressed() {
        if (this.key == ESC) {
            this.key = 0;
            this.overlayManager.getTopOverlay().ifPresent(this::closeTopOverlay);
            this.syncOverlayControllers();
            this.updateStatusAfterInput();
        } else {
            this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.PRESS, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
        }
    }

    public void keyReleased() {
        if (this.key == ESC) this.key = 0;
        this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
    }

    public void keyTyped() {
        if (key == ESC) key = 0;
    }

    public void exit() {
        this.firstOverlayController.dispose();
        this.secondOverlayController.dispose();
        this.firstTooltipController.dispose();
        this.secondTooltipController.dispose();
        this.buttonTooltipController.dispose();
        this.overlayManager.clearAll();
        super.exit();
    }

    private void drawCollapsedDropDowns() {
        if (!this.firstDropDownViewModel.isExpanded()) {
            this.firstDropDownView.draw();
        }

        if (!this.secondDropDownViewModel.isExpanded()) {
            this.secondDropDownView.draw();
        }

    }

    private void drawRegisteredOverlays() {
        for (OverlayEntry var2 : this.overlayManager.getActiveOverlays()) {
            var2.getRender().run();
        }

    }

    private void closeTopOverlay(OverlayEntry var1) {
        if (var1.getOnClose() != null) {
            var1.getOnClose().run();
        }

    }

    private DropDownView getExpandedDropDown() {
        if (this.firstDropDownViewModel.isExpanded()) {
            return this.firstDropDownView;
        } else {
            return this.secondDropDownViewModel.isExpanded() ? this.secondDropDownView : null;
        }
    }

    private void syncOverlayControllers() {
        this.firstOverlayController.syncRegistration();
        this.secondOverlayController.syncRegistration();
    }

    private void syncTooltipControllers() {
        this.firstTooltipController.sync();
        this.secondTooltipController.sync();
        this.buttonTooltipController.sync();
    }

    private boolean handleOverlayTransfer(DropDownOverlayController var1, PointerEvent var2) {
        if (var2.getType() != PointerEvent.Type.PRESS) {
            return false;
        } else {
            DropDownView var3 = this.findClickedDropDown(var2.getX(), var2.getY());
            if (var3 != null && var3 != var1.getView()) {
                this.closeOtherDropDowns(var3);
                var3.handleMousePress(var2.getX(), var2.getY(), this.focusManager);
                return true;
            } else {
                return false;
            }
        }
    }

    private DropDownView findClickedDropDown(float var1, float var2) {
        if (this.firstDropDownView.contains(var1, var2)) {
            return this.firstDropDownView;
        } else {
            return this.secondDropDownView.contains(var1, var2) ? this.secondDropDownView : null;
        }
    }

    private void closeOtherDropDowns(DropDownView var1) {
        if (var1 != this.firstDropDownView) {
            this.firstDropDownViewModel.close();
            this.firstDropDownView.handleMouseMove(-1.0F, -1.0F);
        }

        if (var1 != this.secondDropDownView) {
            this.secondDropDownViewModel.close();
            this.secondDropDownView.handleMouseMove(-1.0F, -1.0F);
        }

    }

    private void resetHoverForInactiveDropDowns(DropDownView var1) {
        if (this.firstDropDownView != var1) {
            this.firstDropDownView.handleMouseMove(-1.0F, -1.0F);
        }

        if (this.secondDropDownView != var1) {
            this.secondDropDownView.handleMouseMove(-1.0F, -1.0F);
        }

    }

    private void updateStatusAfterInput() {
        if (this.firstDropDownViewModel.isExpanded()) {
            this.statusText = "First DropDown expanded";
        } else if (this.secondDropDownViewModel.isExpanded()) {
            this.statusText = "Second DropDown expanded";
        } else {
            String var10001 = this.firstDropDownViewModel.getSelectedText();
            this.statusText = "Selected: first=" + var10001 + " | second=" + this.secondDropDownViewModel.getSelectedText();
        }
    }

    private void drawFrame() {
        this.pushStyle();
        this.fill(255);
        this.stroke(Colors.rgb(210, 218, 229));
        this.rect(54.0F, 54.0F, (float) (this.width - 108), (float) (this.height - 108), 18.0F);
        this.popStyle();
    }

    private void drawNotes() {
        this.pushStyle();
        this.fill(Colors.rgb(28, 36, 46));
        this.textAlign(37, 101);
        this.textSize(24.0F);
        this.text("DropDown Overlay Dev Sketch", 92.0F, 86.0F);
        this.textSize(14.0F);
        this.text("Input flows through InputManager. Expanded dropdowns register as overlays and capture pointer events by priority.", 92.0F, 116.0F, 720.0F, 40.0F);
        this.text("Current status: " + this.statusText, 92.0F, 420.0F);
        this.text("First hovered item index: " + this.firstDropDownView.getHoveredIndex(), 92.0F, 446.0F);
        this.text("Second hovered item index: " + this.secondDropDownView.getHoveredIndex(), 92.0F, 472.0F);
        this.text("Active overlays: " + this.overlayManager.getActiveOverlays().size(), 92.0F, 498.0F);
        this.text("Focused target: " + (this.focusManager.getFocused() == null ? "none" : this.focusManager.getFocused().getClass().getSimpleName()), 92.0F, 524.0F);
        this.popStyle();
    }

    private DropDownStyleConfig createDropDownStyle() {
        DropDownStyleConfig var1 = new DropDownStyleConfig();
        var1.cornerRadius = 10.0F;
        var1.listCornerRadius = 10.0F;
        var1.strokeWeight = 1.5F;
        var1.focusedStrokeWeight = 2.5F;
        var1.textSize = 16.0F;
        var1.itemHeight = 36.0F;
        var1.textPadding = 12.0F;
        var1.arrowPadding = 18.0F;
        var1.maxVisibleItems = 6;
        return var1;
    }

    private LayoutConfig createLayout(float var1, float var2, Anchor var3) {
        LayoutConfig var4 = new LayoutConfig(var1, var2);
        var4.setAnchor(var3);
        return var4;
    }

    private final class RootInputLayer extends DefaultInputLayer {
        private RootInputLayer() {
            Objects.requireNonNull(DropDownDevSketch.this);
            super(0);
        }

        public boolean handlePointerEvent(PointerEvent var1) {
            switch (var1.getType()) {
                case MOVE:
                case DRAG:
                    this.handleRootMove(var1);
                    return true;
                case PRESS:
                    this.handleRootPress(var1);
                    return true;
                case RELEASE:
                    this.handleRootRelease(var1);
                    return true;
                default:
                    return false;
            }
        }

        public boolean handleKeyboardEvent(KeyboardEvent var1) {
            return false;
        }

        private void handleRootMove(PointerEvent var1) {
            DropDownDevSketch.this.firstDropDownView.handleMouseMove(var1.getX(), var1.getY());
            DropDownDevSketch.this.secondDropDownView.handleMouseMove(var1.getX(), var1.getY());
            DropDownDevSketch.this.buttonInputAdapter.handleMouseMove(var1.getX(), var1.getY());
        }

        private void handleRootPress(PointerEvent var1) {
            DropDownView var2 = DropDownDevSketch.this.findClickedDropDown(var1.getX(), var1.getY());
            if (var2 != null) {
                DropDownDevSketch.this.closeOtherDropDowns(var2);
                var2.handleMousePress(var1.getX(), var1.getY(), DropDownDevSketch.this.focusManager);
            } else if (DropDownDevSketch.this.buttonView.contains(var1.getX(), var1.getY())) {
                DropDownDevSketch.this.buttonInputAdapter.handleMousePress(var1.getX(), var1.getY());
                DropDownDevSketch.this.focusManager.clearFocus();
            } else {
                DropDownDevSketch.this.focusManager.clearFocus();
            }
        }

        private void handleRootRelease(PointerEvent var1) {
            DropDownView var2 = DropDownDevSketch.this.firstDropDownViewModel.isExpanded() ? DropDownDevSketch.this.firstDropDownView : (DropDownDevSketch.this.secondDropDownViewModel.isExpanded() ? DropDownDevSketch.this.secondDropDownView : null);
            if (var2 != null) {
                var2.handleMouseRelease(var1.getX(), var1.getY());
                DropDownDevSketch.this.resetHoverForInactiveDropDowns(var2);
                DropDownDevSketch.this.buttonInputAdapter.handleMouseRelease(-1.0F, -1.0F);
            } else {
                DropDownDevSketch.this.firstDropDownView.handleMouseRelease(var1.getX(), var1.getY());
                DropDownDevSketch.this.secondDropDownView.handleMouseRelease(var1.getX(), var1.getY());
                DropDownDevSketch.this.buttonInputAdapter.handleMouseRelease(var1.getX(), var1.getY());
            }
        }
    }
}

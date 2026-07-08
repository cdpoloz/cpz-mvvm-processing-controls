package com.cpz.processing.controls.examples.notification;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationPlacement;
import com.cpz.processing.controls.core.overlay.notification.NotificationSeverity;
import com.cpz.processing.controls.core.overlay.notification.NotificationStyle;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * Visual example for toast-style notification overlays.
 *
 * @author CPZ
 */
public class NotificationTest extends PApplet {
    private InputManager inputManager;
    private OverlayManager overlayManager;
    private NotificationManager notifications;
    private Button infoButton;
    private Button successButton;
    private Button warningButton;
    private Button errorButton;

    public static void main(String[] args) {
        PApplet.main(NotificationTest.class);
    }

    public void settings() {
        size(760, 420);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.notifications = new NotificationManager(this, this.overlayManager);
        this.notifications.setPlacement(NotificationPlacement.TOP_RIGHT);
        this.notifications.setDefaultDurationMillis(3000L);
        this.notifications.setSeverityDurationMillis(NotificationSeverity.WARNING, 4500L);
        this.notifications.setSeverityDurationMillis(NotificationSeverity.ERROR, 6000L);
        this.notifications.setMaxVisible(4);
        this.notifications.setStyle(new NotificationStyle()
                .setWidth(340.0F)
                .setMargin(18.0F)
                .setGap(8.0F)
                .setTextSize(14.0F)
                .setTextPadding(12.0F)
                .setMinHeight(48.0F)
                .setAccentWidth(5.0F)
                .setCornerRadius(8.0F)
                .setStrokeWeight(1.0F));

        this.infoButton = createButton("btnInfo", "Info", 100.0F, 96.0F);
        this.successButton = createButton("btnSuccess", "Success", 100.0F, 154.0F);
        this.warningButton = createButton("btnWarning", "Warning", 100.0F, 212.0F);
        this.errorButton = createButton("btnError", "Error", 100.0F, 270.0F);

        this.infoButton.setClickListener(() -> this.notifications.info("Information message shown from a runtime event."));
        this.successButton.setClickListener(() -> this.notifications.success("Operation completed successfully."));
        this.warningButton.setClickListener(() -> this.notifications.warning("Warning: review the current settings before continuing."));
        this.errorButton.setClickListener(() -> this.notifications.error("Error: the requested operation could not be completed."));

        this.inputManager.registerLayer(new ButtonInputLayer(
                0,
                this.infoButton,
                this.successButton,
                this.warningButton,
                this.errorButton
        ));
    }

    public void draw() {
        background(28);
        this.drawHeader();
        this.infoButton.draw();
        this.successButton.draw();
        this.warningButton.draw();
        this.errorButton.draw();
        this.drawActiveOverlays();
    }

    public void mouseMoved() {
        this.dispatchPointer(PointerEvent.Type.MOVE);
    }

    public void mouseDragged() {
        this.dispatchPointer(PointerEvent.Type.DRAG);
    }

    public void mousePressed() {
        this.dispatchPointer(PointerEvent.Type.PRESS);
    }

    public void mouseReleased() {
        this.dispatchPointer(PointerEvent.Type.RELEASE);
    }

    public void exit() {
        if (this.notifications != null) {
            this.notifications.dispose();
        }
        if (this.overlayManager != null) {
            this.overlayManager.clearAll();
        }
        super.exit();
    }

    private Button createButton(String code, String text, float x, float y) {
        Button button = new Button(this, code, text, x, y, 150.0F, 42.0F);
        button.setStyle(ButtonDefaultStyles.primary());
        return button;
    }

    private void drawHeader() {
        fill(Colors.gray(230));
        textAlign(LEFT, TOP);
        textSize(22.0F);
        text("Notification overlay", 64.0F, 42.0F);
        fill(Colors.gray(160));
        textSize(14.0F);
        text("Click buttons while notifications are visible; input remains routed to controls.", 250.0F, 104.0F);
    }

    private void drawActiveOverlays() {
        for (OverlayEntry entry : this.overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private void dispatchPointer(PointerEvent.Type type) {
        this.inputManager.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }
}

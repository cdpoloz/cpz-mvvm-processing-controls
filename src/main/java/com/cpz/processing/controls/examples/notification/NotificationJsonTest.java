package com.cpz.processing.controls.examples.notification;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.notification.config.NotificationConfigLoader;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * Visual example for notification manager/style JSON configuration.
 *
 * @author CPZ
 */
public class NotificationJsonTest extends PApplet {
    private InputManager inputManager;
    private OverlayManager overlayManager;
    private NotificationManager notifications;
    private Button infoButton;
    private Button successButton;
    private Button warningButton;
    private Button errorButton;

    public static void main(String[] args) {
        PApplet.main(NotificationJsonTest.class);
    }

    public void settings() {
        size(760, 420);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.notifications = new NotificationManager(this, this.overlayManager);
        NotificationConfigLoader.apply(this, "config/notification.json", this.notifications);

        this.infoButton = createButton("btnInfo", "Info", 100.0F, 96.0F);
        this.successButton = createButton("btnSuccess", "Success", 100.0F, 154.0F);
        this.warningButton = createButton("btnWarning", "Warning", 100.0F, 212.0F);
        this.errorButton = createButton("btnError", "Error", 100.0F, 270.0F);

        this.infoButton.setClickListener(() -> this.notifications.info("Information message configured by standalone JSON."));
        this.successButton.setClickListener(() -> this.notifications.success("Operation completed successfully."));
        this.warningButton.setClickListener(() -> this.notifications.warning("Warning duration and color come from notification.json."));
        this.errorButton.setClickListener(() -> this.notifications.error("Error duration and color come from notification.json."));

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
        text("Notification JSON config", 64.0F, 42.0F);
        fill(Colors.gray(160));
        textSize(14.0F);
        text("Standalone JSON config controls placement, durations, style, and typography.", 250.0F, 104.0F);
        text("Messages remain runtime events from button callbacks.", 250.0F, 128.0F);
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

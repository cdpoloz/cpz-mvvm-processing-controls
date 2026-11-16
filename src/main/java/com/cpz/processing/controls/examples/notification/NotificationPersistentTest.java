package com.cpz.processing.controls.examples.notification;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.notification.Notification;
import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationSeverity;
import com.cpz.processing.controls.core.overlay.notification.config.NotificationConfigLoader;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/** Visual check for persistent notifications, optional close buttons and pending items. */
public class NotificationPersistentTest extends PApplet {
    private final InputManager input = new InputManager();
    private final OverlayManager overlays = new OverlayManager();
    private final List<Button> buttons = new ArrayList<>();
    private final List<String> persistentIds = new ArrayList<>();
    private NotificationManager notifications;
    private int sequence;

    public static void main(String[] args) {
        PApplet.main(NotificationPersistentTest.class);
    }

    public void settings() {
        size(900, 560);
    }

    public void setup() {
        surface.setResizable(true);
        this.notifications = new NotificationManager(this, this.overlays);
        this.reloadConfig();
        this.input.registerLayer(this.notifications.getInputLayer());
        this.addButton("Persistent info", 130, () -> this.addPersistent(NotificationSeverity.INFO));
        this.addButton("Persistent warning", 190, () -> this.addPersistent(NotificationSeverity.WARNING));
        this.addButton("Persistent error", 250, () -> this.addPersistent(NotificationSeverity.ERROR));
        this.addButton("Timed info", 310, () -> this.notifications.info("Timed info: no close button, expires normally."));
        this.addButton("Dismiss oldest by ID", 370, this::dismissOldest);
        this.addButton("Clear all", 430, () -> {
            this.notifications.clear();
            this.persistentIds.clear();
        });
        this.input.registerLayer(new ButtonInputLayer(0, this.buttons.toArray(Button[]::new)));
    }

    private void reloadConfig() {
        NotificationConfigLoader.apply(this, "config/notification-persistent.json", this.notifications);
    }

    private void addPersistent(NotificationSeverity severity) {
        Notification item = this.notifications.showPersistent(
                "Persistent " + severity + " #" + (++this.sequence) + ": remains until explicitly closed.", severity);
        this.persistentIds.add(item.getId());
    }

    private void dismissOldest() {
        while (!this.persistentIds.isEmpty()) {
            if (this.notifications.dismiss(this.persistentIds.remove(0))) return;
        }
    }

    private void addButton(String text, float y, Runnable action) {
        Button button = new Button(this, "persistent-" + this.buttons.size(), text, 160, y, 270, 42);
        button.setStyle(ButtonDefaultStyles.primary());
        button.setClickListener(action::run);
        this.buttons.add(button);
    }

    public void draw() {
        background(28);
        fill(230);
        textAlign(LEFT, TOP);
        textSize(20);
        text("Persistent notifications", 24, 24);
        textSize(13);
        fill(180);
        text("R: reload JSON. Set persistentDismissible to false to hide X.\n"
                + "Add more than 3 items to check pending retention and severity order.", 24, 58);
        this.buttons.forEach(Button::draw);
        text("Active (including pending): " + this.notifications.size(), 24, height - 48);
        text("Close buttons enabled: " + this.notifications.isPersistentDismissible(), 24, height - 28);
        this.overlays.getActiveOverlays().forEach(entry -> entry.getRender().run());
    }

    public void keyPressed() {
        if (key == 'r' || key == 'R') this.reloadConfig();
    }

    public void mouseMoved() { this.dispatch(PointerEvent.Type.MOVE); }
    public void mouseDragged() { this.dispatch(PointerEvent.Type.DRAG); }
    public void mousePressed() { this.dispatch(PointerEvent.Type.PRESS); }
    public void mouseReleased() { this.dispatch(PointerEvent.Type.RELEASE); }

    private void dispatch(PointerEvent.Type type) {
        this.input.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }

    public void exit() {
        if (this.notifications != null) {
            this.notifications.dispose();
            this.input.unregisterLayer(this.notifications.getInputLayer());
        }
        super.exit();
    }
}

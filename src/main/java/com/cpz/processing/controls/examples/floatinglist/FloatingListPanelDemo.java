package com.cpz.processing.controls.examples.floatinglist;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.floatinglist.FloatingListConfigLoader;
import com.cpz.processing.controls.controls.floatinglist.FloatingListPanel;
import com.cpz.processing.controls.controls.floatinglist.FloatingListStyle;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

/** Interactive P2D example for anchoring, live list changes and overlay input. */
public final class FloatingListPanelDemo extends PApplet {
    private static final FloatingListPanel.Dimensions LIST_SIZE =
            new FloatingListPanel.Dimensions(390, 286, 34, 42, 16, 82);
    private static final String[] PRESETS = {"legacy", "absolute", "relative", "mixed"};
    private final InputManager input = new InputManager();
    private final OverlayManager overlays = new OverlayManager();
    private final ProcessingKeyboardAdapter keyboard = new ProcessingKeyboardAdapter(new KeyboardState(), input);
    private final ArrayList<Integer> items = new ArrayList<>();
    private final Map<PointerEvent.Type, Integer> lowerEvents = new EnumMap<>(PointerEvent.Type.class);
    private FloatingListPanel<Integer, Integer> popup;
    private Button trigger;
    private OverlayEntry lowerOverlay;
    private int corner;
    private int preset;
    private boolean longTitle;
    private int nextKey;
    private Integer firstVisible;
    private float firstY;

    public static void main(String[] args) {
        PApplet.main(FloatingListPanelDemo.class);
    }

    @Override
    public void settings() {
        size(880, 580, P2D);
    }

    @Override
    public void setup() {
        surface.setTitle("Controls - Floating list P2D");
        surface.setResizable(true);
        this.popup = new FloatingListPanel<>(this, this.overlays, this.input, 100,
                LIST_SIZE,
                "Active items", "Close", "No items", key -> key, this::drawRow);
        this.trigger = new Button(this, "open-list", "Open list", 0, 0, 120, 36);
        this.trigger.setClickListener(() -> this.popup.open(this::anchorBounds));
        var lowerLayer = new DefaultInputLayer(10) {
            @Override
            public boolean handlePointerEvent(PointerEvent event) {
                if (event.getType() != PointerEvent.Type.MOVE) {
                    lowerEvents.merge(event.getType(), 1, Integer::sum);
                }
                trigger.handlePointerEvent(event);
                return true;
            }
        };
        this.lowerOverlay = new OverlayEntry(10, this::drawLowerOverlay, lowerLayer);
        this.overlays.register(this.lowerOverlay);
        this.input.registerLayer(lowerLayer);
        this.seedItems();
    }

    private ResolvedBounds anchorBounds() {
        float x = (this.corner % 2 == 0) ? 12 : Math.max(0, width - 132);
        float y = this.corner < 2 ? 12 : Math.max(0, height - 48);
        if (this.corner == 4) y = Math.max(0, height * 0.73f - 36);
        return new ResolvedBounds(x, y, 120, 36);
    }

    private void seedItems() {
        this.items.clear();
        for (int i = 0; i < 60; i++) this.items.add(++this.nextKey);
        this.updateItems();
    }

    private void updateItems() {
        this.popup.setItems(this.items);
        this.updateTitle();
    }

    private void updateTitle() {
        String title = "Items · " + this.items.size();
        if (this.longTitle) title += " · A longer title that stays inside the available header";
        this.popup.setTitle(title);
    }

    private void cyclePreset() {
        this.preset = (this.preset + 1) % PRESETS.length;
        this.popup.setDimensions(LIST_SIZE);
        this.popup.setStyle(FloatingListStyle.defaults());
        if (this.preset != 0) {
            FloatingListConfigLoader.apply(this, "config/floating-list-" + PRESETS[this.preset] + ".json", this.popup);
        }
        this.updateTitle();
    }

    @Override
    public void draw() {
        background(19, 24, 31);
        ResolvedBounds anchor = this.anchorBounds();
        this.trigger.setBounds(ControlBounds.absolute(anchor.x() + anchor.width() / 2,
                anchor.y() + anchor.height() / 2, anchor.width(), anchor.height()));
        this.trigger.draw();
        this.firstVisible = null;
        // Dispatch prioritizes higher Z. Painting puts those entries on top.
        this.overlays.getActiveOverlays().stream()
                .sorted(Comparator.comparingInt(OverlayEntry::getZIndex))
                .forEach(entry -> entry.getRender().run());

        this.drawStatus();
    }

    private void drawStatus() {
        // Draw after the popup to make a leaked viewport clip apparent.
        var measures = this.popup.getMeasures();
        float panelWidth = Math.min(width, measures.width().resolve(height));
        float margin = Math.min(width / 2f, measures.canvasMargin().resolve(height));
        float occupiedWidth = Math.min(width, panelWidth + Math.max(12, margin));
        float statusWidth = width - occupiedWidth - 28;
        if (statusWidth < 100 && this.popup.isOpen()) return;
        boolean wide = statusWidth >= 430;
        float statusX = this.corner % 2 == 0 && statusWidth >= 100 ? occupiedWidth + 12 : 12;
        float statusY = wide ? height / 2f - 42 : 12;
        statusWidth = Math.max(100, statusWidth);
        fill(227, 234, 241);
        textSize(13);
        textAlign(LEFT, TOP);
        rectMode(CORNER);
        text("A  Insert first    D  Remove first    F  Remove first visible", statusX, statusY, statusWidth, wide ? 19 : 46);
        text("E  Empty    R  Refill    B  Move anchor    G  Clear overlays    Esc  Close",
                statusX, statusY + (wide ? 19 : 48), statusWidth, wide ? 20 : 46);
        int configY = wide ? 42 : 98;
        text("M  Style and units (" + PRESETS[this.preset] + ")    T  Long title    L  Short list",
                statusX, statusY + configY, statusWidth, wide ? 19 : 48);
        float countersY = statusY + configY + (wide ? 24 : 48);
        text("Lower layer  " + this.lowerEvents, statusX, countersY, statusWidth, wide ? 19 : 44);
        text("Items: " + this.items.size() + "   First visible: " + this.firstVisible
                + (this.firstVisible == null ? "" : " at y=" + nf(this.firstY, 1, 2)),
                statusX, countersY + (wide ? 19 : 45), statusWidth, 44);
    }

    private void drawRow(Integer item, ResolvedBounds row) {
        if (this.firstVisible == null) {
            this.firstVisible = item;
            this.firstY = row.y();
        }
        noStroke();
        fill(item % 2 == 0 ? 0xff30404e : 0xff263542);
        rect(row.x(), row.y(), row.width(), row.height());
        fill(233);
        textSize(15);
        textAlign(LEFT, CENTER);
        text("Item " + item, row.x() + 12, row.y() + row.height() / 2);
    }

    private void drawLowerOverlay() {
        noStroke();
        fill(110, 69, 44);
        rect(0, height / 2f - 34, width, 104);
        // Bars overlap the popup near both edges and make the stacking order visible.
        rect(0, 70, width, 28);
        rect(0, height - 98, width, 28);
    }

    @Override
    public void keyPressed() {
        char pressed = key;
        // Processing otherwise calls exit after the PRESS callback.
        if (pressed == ESC) key = 0;
        this.keyboard.keyPressed(pressed, keyCode);
        switch (Character.toLowerCase(pressed)) {
            case 'a' -> this.items.add(0, ++this.nextKey);
            case 'd' -> { if (!this.items.isEmpty()) this.items.remove(0); }
            case 'f' -> { if (this.firstVisible != null) this.items.remove(this.firstVisible); }
            case 'e' -> this.items.clear();
            case 'l' -> {
                if (this.items.size() > 2) this.items.subList(2, this.items.size()).clear();
            }
            case 'm' -> { this.cyclePreset(); return; }
            case 't' -> { this.longTitle = !this.longTitle; this.updateTitle(); return; }
            case 'r' -> { this.seedItems(); return; }
            case 'b' -> { this.corner = (this.corner + 1) % 5; return; }
            case 'g' -> {
                this.overlays.clearAll();
                this.overlays.register(this.lowerOverlay);
                return;
            }
            default -> { return; }
        }
        this.updateItems();
    }

    @Override
    public void keyReleased() {
        this.keyboard.keyReleased(key, keyCode);
    }

    @Override public void mouseMoved() { this.pointer(PointerEvent.Type.MOVE); }
    @Override public void mousePressed() { this.pointer(PointerEvent.Type.PRESS); }
    @Override public void mouseDragged() { this.pointer(PointerEvent.Type.DRAG); }
    @Override public void mouseReleased() { this.pointer(PointerEvent.Type.RELEASE); }

    @Override
    public void mouseWheel(MouseEvent event) {
        this.input.dispatchPointer(new PointerEvent(PointerEvent.Type.WHEEL, event.getX(), event.getY(),
                event.getButton(), event.getCount(), event.isShiftDown(), event.isControlDown()));
    }

    private void pointer(PointerEvent.Type type) {
        this.input.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }

    @Override
    public void dispose() {
        if (this.popup != null) this.popup.close();
        if (this.lowerOverlay != null) {
            this.overlays.unregister(this.lowerOverlay);
            this.input.unregisterLayer(this.lowerOverlay.getInputLayer());
        }
        super.dispose();
    }
}

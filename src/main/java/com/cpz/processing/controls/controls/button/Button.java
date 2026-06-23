package com.cpz.processing.controls.controls.button;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.input.ButtonInputAdapter;
import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.style.ButtonStyle;
import com.cpz.processing.controls.controls.button.util.ButtonListener;
import com.cpz.processing.controls.controls.button.view.ButtonView;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.Objects;

import processing.core.PApplet;

/**
 * Convenience facade for the button control.
 * <p>
 * Responsibilities:
 * - Compose the default MVVM pieces required for a simple button instance.
 * - Expose a small ergonomic API without changing the existing control layers.
 * <p>
 * Behavior:
 * - Delegates rendering to {@link ButtonView}.
 * - Delegates interaction state updates to {@link ButtonInputAdapter}.
 * <p>
 * Notes:
 * - This type is a facade over the existing MVVM architecture, not a replacement for it.
 *
 * @author CPZ
 */
public final class Button implements Control {
    private final ButtonModel model;
    private final ButtonViewModel viewModel;
    private final ButtonView view;
    private final ButtonInputAdapter inputAdapter;

    /**
     * Creates a button with the default internal MVVM composition.
     *
     * @param sketch sketch used by the view
     * @param code initial text
     * @param x x position
     * @param y y position
     * @param width width
     * @param height height
     */
    public Button(PApplet sketch, String code, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("button"), code, x, y, width, height);
    }

    public Button(PApplet sketch, String code, String text, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new ButtonModel(code, text);
        this.viewModel = new ButtonViewModel(this.model);
        this.view = new ButtonView(sketch, this.viewModel, x, y, width, height);
        this.inputAdapter = new ButtonInputAdapter(this.view, this.viewModel);
    }

    /**
     * Draws the button through its view.
     */
    public void draw() {
        this.view.draw();
    }

    /**
     * Handles a normalized pointer event.
     *
     * @param event pointer event to route into the button input adapter
     */
    public void handlePointerEvent(PointerEvent event) {
        if (event != null) {
            switch (event.getType()) {
                case MOVE:
                case DRAG:
                    this.inputAdapter.handleMouseMove(event.getX(), event.getY());
                    break;
                case PRESS:
                    this.inputAdapter.handleMousePress(event.getX(), event.getY());
                    break;
                case RELEASE:
                    this.inputAdapter.handleMouseRelease(event.getX(), event.getY());
            }
        }
    }

    public boolean canConsumePointerEvent(PointerEvent event) {
        return event != null
                && event.getType() != PointerEvent.Type.WHEEL
                && this.isVisible()
                && this.view.contains(event.getX(), event.getY());
    }

    // <editor-fold defaultstate="collapsed" desc="*** setter & getter ***">
    public String getCode() {
        return this.model.getCode();
    }

    public String getText() {
        return this.viewModel.getText();
    }

    public void setText(String text) {
        this.viewModel.setText(text);
    }

    public void setClickListener(ButtonListener listener) {
        this.viewModel.setClickListener(listener);
    }

    public boolean isEnabled() {
        return this.viewModel.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.viewModel.setEnabled(enabled);
    }

    public boolean isVisible() {
        return this.viewModel.isVisible();
    }

    public void setVisible(boolean visible) {
        this.viewModel.setVisible(visible);
    }

    public void setStyle(ButtonStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }
    // </editor-fold>
}

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
 */
public final class Button implements Control {
    private final ButtonModel model;
    private final ButtonViewModel viewModel;
    private final ButtonView view;
    private final ButtonInputAdapter inputAdapter;

    /**
     * Creates a button with the default internal MVVM composition.
     *
     * @param var1 sketch used by the view
     * @param var2 initial text
     * @param var3 x position
     * @param var4 y position
     * @param var5 width
     * @param var6 height
     */
    public Button(PApplet var1, String var2, float var3, float var4, float var5, float var6) {
        this(var1, ControlCode.auto("button"), var2, var3, var4, var5, var6);
    }

    public Button(PApplet var1, String var2, String var3, float var4, float var5, float var6, float var7) {
        Objects.requireNonNull(var1, "sketch");
        this.model = new ButtonModel(var2, var3);
        this.viewModel = new ButtonViewModel(this.model);
        this.view = new ButtonView(var1, this.viewModel, var4, var5, var6, var7);
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
     * @param var1 pointer event to route into the button input adapter
     */
    public void handlePointerEvent(PointerEvent var1) {
        if (var1 != null) {
            switch (var1.getType()) {
                case MOVE:
                case DRAG:
                    this.inputAdapter.handleMouseMove(var1.getX(), var1.getY());
                    break;
                case PRESS:
                    this.inputAdapter.handleMousePress(var1.getX(), var1.getY());
                    break;
                case RELEASE:
                    this.inputAdapter.handleMouseRelease(var1.getX(), var1.getY());
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="*** setter & getter ***">
    public String getCode() {
        return this.model.getCode();
    }

    public String getText() {
        return this.viewModel.getText();
    }

    public void setText(String var1) {
        this.viewModel.setText(var1);
    }

    public void setClickListener(ButtonListener var1) {
        this.viewModel.setClickListener(var1);
    }

    public boolean isEnabled() {
        return this.viewModel.isEnabled();
    }

    public void setEnabled(boolean var1) {
        this.viewModel.setEnabled(var1);
    }

    public boolean isVisible() {
        return this.viewModel.isVisible();
    }

    public void setVisible(boolean var1) {
        this.viewModel.setVisible(var1);
    }

    public void setStyle(ButtonStyle var1) {
        this.view.setStyle(var1);
    }

    public void setPosition(float var1, float var2) {
        this.view.setPosition(var1, var2);
    }

    public void setSize(float var1, float var2) {
        this.view.setSize(var1, var2);
    }
    // </editor-fold>
}

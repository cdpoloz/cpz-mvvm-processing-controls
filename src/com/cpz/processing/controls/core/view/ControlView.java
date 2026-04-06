package com.cpz.processing.controls.core.view;

import com.cpz.processing.controls.core.layout.LayoutConfig;

/**
 * View for control view.
 *
 * Responsibilities:
 * - Own layout, hit testing, and frame-state construction.
 * - Delegate visual resolution to styles and renderers.
 *
 * Behavior:
 * - Does not own business rules or persistent model state.
 *
 * Notes:
 * - This type belongs to the MVVM View layer.
 */
public interface ControlView {
   /**
    * Draws the current frame for the control.
    *
    * Behavior:
    * - Reads state from the ViewModel and delegates visual resolution to styles and renderers.
    */
   void draw();

   /**
    * Updates the view position.
    *
    * @param var1 horizontal anchor position in sketch coordinates
    * @param var2 vertical anchor position in sketch coordinates
    */
   void setPosition(float var1, float var2);

   /**
    * Applies a layout configuration when a view supports layout-driven placement.
    *
    * @param var1 layout configuration to apply
    */
   default void setLayoutConfig(LayoutConfig var1) {
   }
}

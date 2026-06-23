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
 *
 * @author CPZ
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
    * @param x horizontal anchor position in sketch coordinates
    * @param y vertical anchor position in sketch coordinates
    */
   void setPosition(float x, float y);

   /**
    * Applies a layout configuration when a view supports layout-driven placement.
    *
    * @param layout layout configuration to apply
    */
   default void setLayoutConfig(LayoutConfig layout) {
   }
}

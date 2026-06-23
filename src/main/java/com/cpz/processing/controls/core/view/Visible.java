package com.cpz.processing.controls.core.view;

/**
 * View for visible.
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
public interface Visible {
   /**
    * Returns whether the element is visible.
    *
    * @return {@code true} when the element should participate in rendering or interaction
    */
   boolean isVisible();

   /**
    * Updates visibility.
    *
    * @param visible new visibility flag
    */
   void setVisible(boolean visible);
}

package com.cpz.processing.controls.core.viewmodel;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.view.Visible;

/**
 * Base ViewModel for controls with shared availability state.
 *
 * Responsibilities:
 * - Bridge visibility and enabled state between infrastructure and the backing model.
 * - Provide a single availability change hook for subclasses.
 *
 * Behavior:
 * - Does not contain layout or rendering logic.
 * - Does not own pointer-specific interaction state.
 */
public abstract class AbstractControlViewModel implements Visible, Enableable {
   protected final Enableable model;
   private boolean visible = true;

   /**
    * Creates a ViewModel around the provided model.
    *
    * @param var1 backing model that stores enabled state
    */
   protected AbstractControlViewModel(Enableable var1) {
      this.model = var1;
   }

   /**
    * Returns whether visible.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public final boolean isVisible() {
      return this.visible;
   }

   /**
    * Updates visible.
    *
    * @param var1 new visible
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public final void setVisible(boolean var1) {
      this.visible = var1;
      this.onAvailabilityChanged();
   }

   /**
    * Returns whether enabled.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isEnabled() {
      return this.model.isEnabled();
   }

   /**
    * Updates enabled.
    *
    * @param var1 new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean var1) {
      this.model.setEnabled(var1);
      this.onAvailabilityChanged();
   }

   /**
    * Availability hook for subclasses.
    *
    * Behavior:
    * - Called after visibility or enabled state changes.
    */
   protected void onAvailabilityChanged() {
   }
}

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
 *
 * @author CPZ
 */
public abstract class AbstractControlViewModel implements Visible, Enableable {
   protected final Enableable model;
   private boolean visible = true;

   /**
    * Creates a ViewModel around the provided model.
    *
    * @param enableable backing model that stores enabled state
    */
   protected AbstractControlViewModel(Enableable enableable) {
      this.model = enableable;
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
    * @param visible new visible
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public final void setVisible(boolean visible) {
      this.visible = visible;
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
    * @param enabled new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean enabled) {
      this.model.setEnabled(enabled);
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

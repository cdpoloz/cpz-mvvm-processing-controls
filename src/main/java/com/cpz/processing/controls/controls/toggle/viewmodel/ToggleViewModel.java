package com.cpz.processing.controls.controls.toggle.viewmodel;

import com.cpz.processing.controls.controls.toggle.model.ToggleModel;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;

/**
 * ViewModel for toggle view model.
 *
 * Responsibilities:
 * - Expose control state to the view layer.
 * - Coordinate interaction and synchronize with the backing model.
 *
 * Behavior:
 * - Does not perform drawing directly.
 *
 * Notes:
 * - This type belongs to the MVVM ViewModel layer.
 *
 * @author CPZ
 */
public class ToggleViewModel extends AbstractInteractiveControlViewModel {
   /**
    * Creates a toggle view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ToggleViewModel(ToggleModel model) {
      super(model);
      this.setTotalStates(model.getTotalStates());
   }

   /**
    * Returns state.
    *
    * @return current state
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getState() {
      return ((ToggleModel)this.model).getState();
   }

   /**
    * Returns prev state.
    *
    * @return current prev state
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getPrevState() {
      return ((ToggleModel)this.model).getPrevState();
   }

   /**
    * Returns whether changed.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean hasChanged() {
      return ((ToggleModel)this.model).getState() != ((ToggleModel)this.model).getPrevState();
   }

   /**
    * Returns whether first state.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isFirstState() {
      return ((ToggleModel)this.model).getState() == 0;
   }

   /**
    * Updates total states.
    *
    * @param value new total states
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setTotalStates(int value) {
      int value2 = Math.max(1, value);
      ((ToggleModel)this.model).setTotalStates(value2);
      this.clampState(value2);
   }

   /**
    * Returns total states.
    *
    * @return current total states
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getTotalStates() {
      return ((ToggleModel)this.model).getTotalStates();
   }

   protected void activate() {
      int value = Math.max(1, ((ToggleModel)this.model).getTotalStates());
      int value2 = ((ToggleModel)this.model).getState();
      ((ToggleModel)this.model).setPrevState(value2);
      ((ToggleModel)this.model).setState((value2 + 1) % value);
   }

   private void clampState(int value) {
      if (((ToggleModel)this.model).getState() >= value) {
         ((ToggleModel)this.model).setPrevState(((ToggleModel)this.model).getState());
         ((ToggleModel)this.model).setState(value - 1);
      }

   }
}

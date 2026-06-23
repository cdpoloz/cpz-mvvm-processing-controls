package com.cpz.processing.controls.controls.checkbox.viewmodel;

import com.cpz.processing.controls.controls.checkbox.model.CheckboxModel;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;

/**
 * ViewModel for checkbox view model.
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
public final class CheckboxViewModel extends AbstractInteractiveControlViewModel {
   /**
    * Creates a checkbox view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CheckboxViewModel(CheckboxModel model) {
      super(model);
   }

   /**
    * Returns whether checked.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isChecked() {
      return ((CheckboxModel)this.model).isChecked();
   }

   /**
    * Updates checked.
    *
    * @param enabled new checked
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setChecked(boolean enabled) {
      ((CheckboxModel)this.model).setChecked(enabled);
   }

   protected void activate() {
      ((CheckboxModel)this.model).setChecked(!((CheckboxModel)this.model).isChecked());
   }
}

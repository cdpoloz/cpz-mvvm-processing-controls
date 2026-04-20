package com.cpz.processing.controls.controls.label.viewmodel;

import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;

/**
 * ViewModel for label view model.
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
public final class LabelViewModel extends AbstractControlViewModel {
   /**
    * Creates a label view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public LabelViewModel(LabelModel model) {
      super(model);
   }

   /**
    * Returns text.
    *
    * @return current text
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public String getText() {
      return ((LabelModel)this.model).getText();
   }

   /**
    * Updates text.
    *
    * @param text new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String text) {
      ((LabelModel)this.model).setText(text);
   }
}

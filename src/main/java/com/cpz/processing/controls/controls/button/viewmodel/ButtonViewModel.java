package com.cpz.processing.controls.controls.button.viewmodel;

import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.util.ButtonListener;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;
import java.util.Objects;

/**
 * ViewModel for button view model.
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
public final class ButtonViewModel extends AbstractInteractiveControlViewModel {
   private ButtonListener clickListener;
   private boolean showText = true;

   /**
    * Creates a button view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ButtonViewModel(ButtonModel model) {
      super(model);
   }

   /**
    * Updates click listener.
    *
    * @param listener new click listener
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setClickListener(ButtonListener listener) {
      this.clickListener = listener;
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
      return ((ButtonModel)this.model).getText();
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
      ((ButtonModel)this.model).setText(text);
   }

   /**
    * Returns whether show text.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isShowText() {
      return this.showText;
   }

   /**
    * Updates show text.
    *
    * @param enabled new show text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setShowText(boolean enabled) {
      this.showText = enabled;
   }

   /** @deprecated */
   /**
    * Updates on click.
    *
    * @param callback new on click
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   @Deprecated
   public void setOnClick(Runnable callback) {
      if (callback == null) {
         this.clickListener = null;
      } else {
         Objects.requireNonNull(callback);
         this.clickListener = callback::run;
      }

   }

   protected void activate() {
      if (this.clickListener != null) {
         this.clickListener.onClick();
      }

   }
}

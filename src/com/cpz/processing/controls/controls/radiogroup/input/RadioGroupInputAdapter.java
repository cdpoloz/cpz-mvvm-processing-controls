package com.cpz.processing.controls.controls.radiogroup.input;

import com.cpz.processing.controls.controls.radiogroup.view.RadioGroupView;
import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerInputAdapter;

/**
 * Input component for radio group input adapter.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public final class RadioGroupInputAdapter {
   private final RadioGroupView view;
   private final RadioGroupViewModel viewModel;
   private final FocusManager focusManager;
   private final PointerInputAdapter delegate;

   /**
    * Creates a radio group input adapter.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupInputAdapter(RadioGroupView var1, RadioGroupViewModel var2, FocusManager var3) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.delegate = new PointerInputAdapter(var1, var2);
      this.focusManager.register(var2);
   }

   /**
    * Handles mouse move.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseMove(float var1, float var2) {
      this.delegate.handleMouseMove(var1, var2);
      this.viewModel.setHoveredIndex(this.view.getOptionIndexAt(var1, var2));
   }

   /**
    * Handles mouse press.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float var1, float var2) {
      this.delegate.handleMousePress(var1, var2);
      int var3 = this.view.getOptionIndexAt(var1, var2);
      this.viewModel.onOptionPressed(var3);
      this.viewModel.setHoveredIndex(var3);
      if (var3 >= 0) {
         this.focusManager.requestFocus(this.viewModel);
      }

   }

   /**
    * Handles mouse release.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float var1, float var2) {
      this.delegate.handleMouseRelease(var1, var2);
      int var3 = this.view.getOptionIndexAt(var1, var2);
      this.viewModel.onOptionReleased(var3);
      this.viewModel.setHoveredIndex(var3);
   }
}

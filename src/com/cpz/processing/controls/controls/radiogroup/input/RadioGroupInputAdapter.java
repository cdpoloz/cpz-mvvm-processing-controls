package com.cpz.processing.controls.controls.radiogroup.input;

import com.cpz.processing.controls.controls.radiogroup.view.RadioGroupView;
import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerEvent;
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
 *
 * @author CPZ
 */
public final class RadioGroupInputAdapter {
   private final RadioGroupView view;
   private final RadioGroupViewModel viewModel;
   private final FocusManager focusManager;
   private final PointerInputAdapter delegate;

   /**
    * Creates a radio group input adapter.
    *
    * @param view parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param focusManager parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupInputAdapter(RadioGroupView view, RadioGroupViewModel viewModel, FocusManager focusManager) {
      this.view = view;
      this.viewModel = viewModel;
      this.focusManager = focusManager;
      this.delegate = new PointerInputAdapter(view, viewModel);
      this.focusManager.register(viewModel);
   }

   /**
    * Handles mouse move.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseMove(float mouseX, float mouseY) {
      this.delegate.handleMouseMove(mouseX, mouseY);
      this.viewModel.setHoveredIndex(this.view.getOptionIndexAt(mouseX, mouseY));
   }

   /**
    * Handles mouse press.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float mouseX, float mouseY) {
      this.delegate.handleMousePress(mouseX, mouseY);
      int value = this.view.getOptionIndexAt(mouseX, mouseY);
      this.viewModel.onOptionPressed(value);
      this.viewModel.setHoveredIndex(value);
      if (value >= 0) {
         this.focusManager.requestFocus(this.viewModel);
      }

   }

   /**
    * Handles mouse release.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float mouseX, float mouseY) {
      this.delegate.handleMouseRelease(mouseX, mouseY);
      int value = this.view.getOptionIndexAt(mouseX, mouseY);
      this.viewModel.onOptionReleased(value);
      this.viewModel.setHoveredIndex(value);
   }

   /**
    * Handles pointer event.
    *
    * @param event parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handlePointerEvent(PointerEvent event) {
      if (event != null) {
         switch (event.getType()) {
            case MOVE:
            case DRAG:
               this.handleMouseMove(event.getX(), event.getY());
               break;
            case PRESS:
               this.handleMousePress(event.getX(), event.getY());
               break;
            case RELEASE:
               this.handleMouseRelease(event.getX(), event.getY());
         }
      }
   }
}

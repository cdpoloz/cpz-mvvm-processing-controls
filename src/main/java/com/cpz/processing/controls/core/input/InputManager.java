package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.focus.FocusManagerAware;
import java.util.ArrayList;
import java.util.List;

/**
 * Dispatches normalized input events through ordered input layers.
 *
 * <p>The manager is source-agnostic. Host adapters create {@link PointerEvent}
 * and {@link KeyboardEvent} instances and pass them here; layers decide whether
 * to consume each event and forward it to concrete facade methods, overlays, or
 * sketch-level shortcuts.</p>
 *
 * <p>Each manager owns one {@link FocusManager}. Focus-aware layers attach to
 * that authority while registered, so focus is exclusive within this routing
 * scope and independent from every other {@code InputManager}.</p>
 *
 * @author CPZ
 */
public class InputManager {
   private final List<InputLayer> layers = new ArrayList<>();
   private final FocusManager focusManager = new FocusManager();

   /**
    * Registers a layer and keeps dispatch order sorted by descending priority.
    *
    * @param layer layer to register
    */
   public void registerLayer(InputLayer layer) {
      if (layer != null && !this.layers.contains(layer)) {
         if (layer instanceof FocusManagerAware) {
            ((FocusManagerAware)layer).attachFocusManager(this.focusManager);
         }
         this.layers.add(layer);
         this.sortLayers();
      }
   }

   /**
    * Removes a previously registered layer.
    *
    * @param layer layer to remove
    */
   public void unregisterLayer(InputLayer layer) {
      if (this.layers.remove(layer) && layer instanceof FocusManagerAware) {
         ((FocusManagerAware)layer).detachFocusManager(this.focusManager);
      }
   }

   /**
    * Returns the focus authority owned by this routing manager.
    *
    * <p>The returned instance has the same lifecycle and isolation boundary as
    * this {@code InputManager}. It can be supplied to other host-owned
    * infrastructure such as an {@code OverlayManager}.</p>
    *
    * @return this manager's focus authority
    */
   public FocusManager getFocusManager() {
      return this.focusManager;
   }

   private void sortLayers() {
      this.layers.sort((left, right) -> Integer.compare(right.getPriority(), left.getPriority()));
   }

   /**
    * Dispatches a pointer event until an active pointer-capturing layer consumes it.
    *
    * @param event normalized pointer event
    */
   public void dispatchPointer(PointerEvent event) {
      for(InputLayer layer : this.layers) {
         if (layer.isActive() && layer.isPointerCaptureEnabled() && layer.handlePointerEvent(event)) {
            break;
         }
      }

   }

   /**
    * Dispatches a keyboard event until an active keyboard-capturing layer consumes it.
    *
    * @param event normalized keyboard event
    */
   public void dispatchKeyboard(KeyboardEvent event) {
      for(InputLayer layer : this.layers) {
         if (layer.isActive() && layer.isKeyboardCaptureEnabled() && layer.handleKeyboardEvent(event)) {
            break;
         }
      }

   }
}

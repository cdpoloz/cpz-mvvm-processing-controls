package com.cpz.processing.controls.core.input;

/**
 * Base implementation for simple input layers.
 *
 * <p>The default layer is active, captures pointer and keyboard events, and
 * consumes nothing. Subclasses override only the event paths they need.</p>
 *
 * @author CPZ
 */
public class DefaultInputLayer implements InputLayer {
   private final int priority;
   private boolean active = true;

   /**
    * Creates a layer with the supplied dispatch priority.
    *
    * @param value priority; higher values are processed first
    */
   public DefaultInputLayer(int value) {
      this.priority = value;
   }

   /**
    * Returns this layer's dispatch priority.
    *
    * @return priority value
    */
   public int getPriority() {
      return this.priority;
   }

   /**
    * Returns whether this layer participates in dispatch.
    *
    * @return {@code true} when active
    */
   public boolean isActive() {
      return this.active;
   }

   /**
    * Returns whether pointer events should be offered to this layer.
    *
    * @return {@code true} by default
    */
   public boolean isPointerCaptureEnabled() {
      return true;
   }

   /**
    * Returns whether keyboard events should be offered to this layer.
    *
    * @return {@code true} by default
    */
   public boolean isKeyboardCaptureEnabled() {
      return true;
   }

   /**
    * Handles a pointer event.
    *
    * @param event normalized pointer event
    * @return {@code false}; subclasses consume when appropriate
    */
   public boolean handlePointerEvent(PointerEvent event) {
      return false;
   }

   /**
    * Handles a keyboard event.
    *
    * @param event normalized keyboard event
    * @return {@code false}; subclasses consume when appropriate
    */
   public boolean handleKeyboardEvent(KeyboardEvent event) {
      return false;
   }

   /**
    * Enables or disables this layer.
    *
    * @param enabled active state
    */
   public void setActive(boolean enabled) {
      this.active = enabled;
   }
}

package com.cpz.processing.controls.core.overlay;

import com.cpz.processing.controls.core.focus.Focusable;
import com.cpz.processing.controls.core.input.InputLayer;

/**
 * Overlay component for overlay entry.
 *
 * Responsibilities:
 * - Coordinate overlay-specific state or drawing flow.
 * - Keep overlay behavior isolated from base controls.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public class OverlayEntry {
   private final int zIndex;
   private final Runnable render;
   private final InputLayer inputLayer;
   private final Runnable onClose;
   private final Focusable focusTarget;

   /**
    * Creates a overlay entry.
    *
    * @param priority parameter used by this operation
    * @param render parameter used by this operation
    * @param inputLayer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayEntry(int priority, Runnable render, InputLayer inputLayer) {
      this(priority, render, inputLayer, (Runnable)null, (Focusable)null);
   }

   /**
    * Creates a overlay entry.
    *
    * @param priority parameter used by this operation
    * @param render parameter used by this operation
    * @param inputLayer parameter used by this operation
    * @param onClose parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayEntry(int priority, Runnable render, InputLayer inputLayer, Runnable onClose) {
      this(priority, render, inputLayer, onClose, (Focusable)null);
   }

   /**
    * Creates a overlay entry.
    *
    * @param priority parameter used by this operation
    * @param render parameter used by this operation
    * @param inputLayer parameter used by this operation
    * @param onClose parameter used by this operation
    * @param focusTarget parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayEntry(int priority, Runnable render, InputLayer inputLayer, Runnable onClose, Focusable focusTarget) {
      this.zIndex = priority;
      this.render = render;
      this.inputLayer = inputLayer;
      this.onClose = onClose;
      this.focusTarget = focusTarget;
   }

   /**
    * Returns z index.
    *
    * @return current z index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getZIndex() {
      return this.zIndex;
   }

   /**
    * Returns render.
    *
    * @return current render
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Runnable getRender() {
      return this.render;
   }

   /**
    * Returns input layer.
    *
    * @return current input layer
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public InputLayer getInputLayer() {
      return this.inputLayer;
   }

   /**
    * Returns on close.
    *
    * @return current on close
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Runnable getOnClose() {
      return this.onClose;
   }

   /**
    * Returns focus target.
    *
    * @return current focus target
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Focusable getFocusTarget() {
      return this.focusTarget;
   }
}

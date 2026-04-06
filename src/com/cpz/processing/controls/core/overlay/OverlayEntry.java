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
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayEntry(int var1, Runnable var2, InputLayer var3) {
      this(var1, var2, var3, (Runnable)null, (Focusable)null);
   }

   /**
    * Creates a overlay entry.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayEntry(int var1, Runnable var2, InputLayer var3, Runnable var4) {
      this(var1, var2, var3, var4, (Focusable)null);
   }

   /**
    * Creates a overlay entry.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayEntry(int var1, Runnable var2, InputLayer var3, Runnable var4, Focusable var5) {
      this.zIndex = var1;
      this.render = var2;
      this.inputLayer = var3;
      this.onClose = var4;
      this.focusTarget = var5;
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

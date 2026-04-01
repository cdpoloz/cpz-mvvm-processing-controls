package com.cpz.processing.controls.core.overlay;

import com.cpz.processing.controls.core.focus.Focusable;
import com.cpz.processing.controls.core.input.InputLayer;

public class OverlayEntry {
   private final int zIndex;
   private final Runnable render;
   private final InputLayer inputLayer;
   private final Runnable onClose;
   private final Focusable focusTarget;

   public OverlayEntry(int var1, Runnable var2, InputLayer var3) {
      this(var1, var2, var3, (Runnable)null, (Focusable)null);
   }

   public OverlayEntry(int var1, Runnable var2, InputLayer var3, Runnable var4) {
      this(var1, var2, var3, var4, (Focusable)null);
   }

   public OverlayEntry(int var1, Runnable var2, InputLayer var3, Runnable var4, Focusable var5) {
      this.zIndex = var1;
      this.render = var2;
      this.inputLayer = var3;
      this.onClose = var4;
      this.focusTarget = var5;
   }

   public int getZIndex() {
      return this.zIndex;
   }

   public Runnable getRender() {
      return this.render;
   }

   public InputLayer getInputLayer() {
      return this.inputLayer;
   }

   public Runnable getOnClose() {
      return this.onClose;
   }

   public Focusable getFocusTarget() {
      return this.focusTarget;
   }
}

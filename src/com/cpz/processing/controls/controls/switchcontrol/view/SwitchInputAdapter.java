package com.cpz.processing.controls.controls.switchcontrol.view;

import com.cpz.processing.controls.core.input.PointerInputAdapter;
import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.input.PointerInteractable;

public final class SwitchInputAdapter {
   private final PointerInputAdapter delegate;

   public SwitchInputAdapter(PointerInteractable var1, PointerInputTarget var2) {
      this.delegate = new PointerInputAdapter(var1, var2);
   }

   public void handleMouseMove(float var1, float var2) {
      this.delegate.handleMouseMove(var1, var2);
   }

   public void handleMousePress(float var1, float var2) {
      this.delegate.handleMousePress(var1, var2);
   }

   public void handleMouseRelease(float var1, float var2) {
      this.delegate.handleMouseRelease(var1, var2);
   }
}

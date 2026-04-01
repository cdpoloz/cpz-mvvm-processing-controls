package com.cpz.processing.controls.core.input.hit.interfaces;

public interface HitTest {
   boolean contains(float var1, float var2);

   default void onLayout(float var1, float var2, float var3, float var4) {
   }
}

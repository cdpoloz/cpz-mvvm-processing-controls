package com.cpz.processing.controls.core.view;

import com.cpz.processing.controls.core.layout.LayoutConfig;

public interface ControlView {
   void draw();

   void setPosition(float var1, float var2);

   default void setLayoutConfig(LayoutConfig var1) {
   }
}

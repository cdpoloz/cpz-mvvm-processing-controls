package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.view.Visible;

public interface PointerInputTarget extends Visible, Enableable {
   void onPointerMove(boolean var1);

   void onPointerPress(boolean var1);

   void onPointerRelease(boolean var1);
}

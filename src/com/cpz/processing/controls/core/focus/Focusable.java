package com.cpz.processing.controls.core.focus;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.view.Visible;

public interface Focusable extends Visible, Enableable {
   boolean isFocused();

   void setFocused(boolean var1);

   default void onFocusGained() {
   }

   default void onFocusLost() {
   }
}

package com.cpz.processing.controls.common.focus;

import com.cpz.processing.controls.common.Enableable;
import com.cpz.processing.controls.common.Visible;

public interface Focusable extends Visible, Enableable {

    boolean isFocused();

    void setFocused(boolean focused);

    default void onFocusGained() {
    }

    default void onFocusLost() {
    }
}

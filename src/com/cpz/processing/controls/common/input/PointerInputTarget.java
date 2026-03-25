package com.cpz.processing.controls.common.input;

import com.cpz.processing.controls.common.Enableable;
import com.cpz.processing.controls.common.Visible;

public interface PointerInputTarget extends Visible, Enableable {

    void onPointerMove(boolean inside);

    void onPointerPress(boolean inside);

    void onPointerRelease(boolean inside);
}

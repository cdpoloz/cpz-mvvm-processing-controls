package com.cpz.processing.controls.util;

import com.cpz.processing.controls.controls.Control;

import java.util.Map;

/**
 * @author CPZ
 */
public class Util {

    public static <T extends Control> T getControl(Map<String, Control> controls, String code, Class<T> type) {
        String s = "Controls not loaded or null";
        if (controls == null) throw new IllegalStateException(s);
        Control control = controls.get(code);
        s = "Missing required control in example config: " + code;
        if (control == null) throw new IllegalStateException(s);
        if (!type.isInstance(control)) {
            s = "Invalid control type for code '" + code + "'. Expected " + type.getSimpleName() + " but got " + control.getClass().getSimpleName() + ".";
            throw new IllegalStateException(s);
        }
        return type.cast(control);
    }

}

package com.cpz.processing.controls.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Loads one or more public control facades from a structural JSON document.
 *
 * <p>The loader expects the current multi-control format with a root
 * {@code controls[]} array. Each entry must declare {@code type} and
 * {@code code}. The result is a {@link LinkedHashMap} preserving JSON order and
 * exposing controls through the minimal {@link Control} contract.</p>
 *
 * <p>JSON remains structural. This loader does not create listeners, binding,
 * shortcuts, or sketch behavior.</p>
 *
 * @author CPZ
 */
public final class ControlConfigLoader {
    private final PApplet sketch;
    private final ControlFactoryRegistry registry;

    /**
     * Creates a loader for controls that do not need overlay infrastructure.
     *
     * @param sketch Processing sketch used by concrete factories
     */
    public ControlConfigLoader(PApplet sketch) {
        this(sketch, null, null);
    }

    /**
     * Creates a loader with optional overlay/input infrastructure for controls
     * such as {@code DropDown}.
     *
     * @param sketch Processing sketch used by concrete factories
     * @param overlayManager overlay manager required by dropdown controls
     * @param inputManager input manager required by dropdown controls
     */
    public ControlConfigLoader(PApplet sketch, OverlayManager overlayManager, InputManager inputManager) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.registry = new ControlFactoryRegistry(sketch, overlayManager, inputManager);
    }

    /**
     * Loads the JSON document and returns public facades keyed by control code.
     *
     * @param path path to a JSON document with a root {@code controls[]} array
     * @return ordered map of public controls
     * @throws IllegalArgumentException when structure, type, code, or factory output is invalid
     */
    public Map<String, Control> load(String path) {
        Objects.requireNonNull(path, "path");

        JSONObject root = JsonConfigSupport.loadRequiredObject(this.sketch, path, "control");
        JSONArray controls = JsonConfigSupport.getRequiredArray(root, "controls", path);

        Map<String, Control> result = new LinkedHashMap<>();
        for (int i = 0; i < controls.size(); i++) {
            Object rawEntry = controls.get(i);
            if (!(rawEntry instanceof JSONObject)) {
                throw new IllegalArgumentException(
                        "Invalid 'controls[" + i + "]' value in " + path + ": expected an object."
                );
            }

            JSONObject controlJson = (JSONObject) rawEntry;
            String controlPath = path + " -> controls[" + i + "]";
            String type = JsonConfigSupport.getRequiredNonBlankString(controlJson, "type", controlPath, "control");
            String code = JsonConfigSupport.getRequiredNonBlankString(controlJson, "code", controlPath, "control");
            if (result.containsKey(code)) {
                throw new IllegalArgumentException(
                        "Duplicate control code in " + path + ": '" + code + "'. Each control code must be unique."
                );
            }

            Control control = this.registry.create(type, controlJson, controlPath);
            if (control == null) {
                throw new IllegalArgumentException(
                        "Control factory returned null in " + controlPath + " for code '" + code + "'."
                );
            }
            String actualCode = control.getCode();
            if (!code.equals(actualCode)) {
                throw new IllegalArgumentException(
                        "Control code mismatch in " + controlPath + ": expected JSON code '" + code
                                + "' but the created facade returned '" + actualCode + "'."
                );
            }
            result.put(code, control);
        }

        return result;
    }
}

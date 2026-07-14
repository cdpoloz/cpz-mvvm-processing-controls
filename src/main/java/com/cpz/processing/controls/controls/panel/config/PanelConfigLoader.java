package com.cpz.processing.controls.controls.panel.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Objects;

/**
 * Loads panel config from JSON.
 *
 * <p>The loader validates supported panel fields and the optional nested
 * {@code style} block. It does not create controls, resolve theme fallbacks,
 * register input, or create child relationships.</p>
 *
 * @author CPZ
 */
public final class PanelConfigLoader {
    private final PApplet sketch;

    public PanelConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public PanelConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "panel"),
                path,
                "panel",
                "panel"
        );
        return this.loadFromJson(root, path);
    }

    public PanelConfig loadFromJson(JSONObject root, String path) {
        return new PanelConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "panel"),
                JsonConfigSupport.getControlBounds(root, path, "panel"),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private PanelStyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        Object rawStyle = root.get("style");
        if (!(rawStyle instanceof JSONObject)) {
            throw new IllegalArgumentException(
                    "Invalid 'style' value in " + path + " for panel: expected an object."
            );
        }

        JSONObject style = (JSONObject) rawStyle;
        String stylePath = path + " -> style";
        return new PanelStyleConfig(
                this.readOptionalBoolean(style, "backgroundVisible", stylePath),
                JsonConfigSupport.getOptionalColor(style, "backgroundColor", stylePath),
                this.readOptionalBoolean(style, "strokeVisible", stylePath),
                JsonConfigSupport.getOptionalColor(style, "strokeColor", stylePath),
                this.readOptionalFiniteFloat(style, "strokeWeight", stylePath),
                this.readOptionalFiniteFloat(style, "cornerRadius", stylePath)
        );
    }

    private Boolean readOptionalBoolean(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }

        Object raw = json.get(key);
        if (!(raw instanceof Boolean)) {
            throw new IllegalArgumentException(
                    "Invalid '" + key + "' value in " + path + " for panel style: expected a boolean."
            );
        }
        return (Boolean) raw;
    }

    private Float readOptionalFiniteFloat(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }

        Object raw = json.get(key);
        if (!(raw instanceof Number)) {
            throw new IllegalArgumentException(
                    "Invalid '" + key + "' value in " + path + " for panel style: expected a number."
            );
        }

        float value = ((Number) raw).floatValue();
        if (!Float.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Invalid '" + key + "' value in " + path + " for panel style: expected a finite number."
            );
        }
        return value;
    }
}

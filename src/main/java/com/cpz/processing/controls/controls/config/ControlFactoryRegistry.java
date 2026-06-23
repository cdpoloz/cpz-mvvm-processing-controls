package com.cpz.processing.controls.controls.config;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.checkbox.CheckboxFactory;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxConfigLoader;
import com.cpz.processing.controls.controls.dropdown.DropDownFactory;
import com.cpz.processing.controls.controls.dropdown.config.DropDownConfigLoader;
import com.cpz.processing.controls.controls.label.LabelFactory;
import com.cpz.processing.controls.controls.label.config.LabelConfigLoader;
import com.cpz.processing.controls.controls.numericfield.NumericFieldFactory;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfigLoader;
import com.cpz.processing.controls.controls.radiogroup.RadioGroupFactory;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfigLoader;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.textfield.TextFieldFactory;
import com.cpz.processing.controls.controls.textfield.config.TextFieldConfigLoader;
import com.cpz.processing.controls.controls.toggle.ToggleFactory;
import com.cpz.processing.controls.controls.toggle.config.ToggleConfigLoader;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Internal registry that maps JSON {@code type} values to public facade factories.
 *
 * <p>The registry is used only by {@link ControlConfigLoader}. It centralizes
 * supported control types while keeping the public JSON result at
 * {@code Map<String, Control>}.</p>
 *
 * @author CPZ
 */
final class ControlFactoryRegistry {
    private final Map<String, ControlEntryFactory> factories;

    /**
     * Creates a registry with the currently supported control types.
     */
    ControlFactoryRegistry(PApplet sketch, OverlayManager overlayManager, InputManager inputManager) {
        Objects.requireNonNull(sketch, "sketch");

        ButtonConfigLoader buttonLoader = new ButtonConfigLoader(sketch);
        CheckboxConfigLoader checkboxLoader = new CheckboxConfigLoader(sketch);
        ToggleConfigLoader toggleLoader = new ToggleConfigLoader(sketch);
        SliderConfigLoader sliderLoader = new SliderConfigLoader(sketch);
        LabelConfigLoader labelLoader = new LabelConfigLoader(sketch);
        RadioGroupConfigLoader radioGroupLoader = new RadioGroupConfigLoader(sketch);
        TextFieldConfigLoader textFieldLoader = new TextFieldConfigLoader(sketch);
        NumericFieldConfigLoader numericFieldLoader = new NumericFieldConfigLoader(sketch);
        DropDownConfigLoader dropDownLoader = new DropDownConfigLoader(sketch);

        Map<String, ControlEntryFactory> entries = new LinkedHashMap<>();
        entries.put("button", (json, path) -> ButtonFactory.create(sketch, buttonLoader.loadFromJson(json, path)));
        entries.put("checkbox", (json, path) -> CheckboxFactory.create(sketch, checkboxLoader.loadFromJson(json, path)));
        entries.put("toggle", (json, path) -> ToggleFactory.create(sketch, toggleLoader.loadFromJson(json, path)));
        entries.put("slider", (json, path) -> SliderFactory.create(sketch, sliderLoader.loadFromJson(json, path)));
        entries.put("label", (json, path) -> LabelFactory.create(sketch, labelLoader.loadFromJson(json, path)));
        entries.put("radiogroup", (json, path) -> RadioGroupFactory.create(sketch, radioGroupLoader.loadFromJson(json, path)));
        entries.put("textfield", (json, path) -> TextFieldFactory.create(sketch, textFieldLoader.loadFromJson(json, path)));
        entries.put("numericfield", (json, path) -> NumericFieldFactory.create(sketch, numericFieldLoader.loadFromJson(json, path)));
        entries.put("dropdown", (json, path) -> {
            if (overlayManager == null || inputManager == null) {
                throw new IllegalArgumentException(
                        "Control type 'dropdown' in " + path
                                + " requires OverlayManager and InputManager in ControlConfigLoader."
                );
            }
            return DropDownFactory.create(sketch, overlayManager, inputManager, dropDownLoader.loadFromJson(json, path));
        });
        this.factories = Collections.unmodifiableMap(entries);
    }

    /**
     * Creates a control facade for one normalized JSON entry.
     *
     * @param type JSON control type
     * @param json JSON object for the control entry
     * @param path diagnostic path used in validation errors
     * @return public control facade
     */
    Control create(String type, JSONObject json, String path) {
        String normalizedType = JsonConfigSupport.normalizeControlType(type);
        ControlEntryFactory factory = this.factories.get(normalizedType);
        if (factory == null) {
            throw new IllegalArgumentException(
                    "Unsupported control type in " + path + ": " + type
                            + ". Supported values: " + String.join(", ", this.factories.keySet()) + "."
            );
        }
        return factory.create(json, path);
    }

    @FunctionalInterface
    private interface ControlEntryFactory {
        Control create(JSONObject json, String path);
    }
}

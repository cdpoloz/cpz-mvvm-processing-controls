# JSON Configuration

This document describes the JSON configuration layer for the public control facades.

JSON configuration is an optional layer on top of the existing public API. It does not replace direct control creation, it does not change the internal MVVM architecture, and it does not define binding or behavior orchestration.

---

## Overview

The JSON layer supports one or more controls in the same document through a single root array:

```json
{
  "controls": [
    {
      "type": "<control-type>",
      "code": "<unique-code>"
    }
  ]
}
```

Rules:

- `controls` is required
- each entry represents one independent public control facade
- `type` is required
- `code` is required
- all other fields remain control-specific
- JSON does not define listeners
- JSON does not define binding

Binding remains sketch-side code.

---

## Public Result

The main JSON loader returns:

```java
Map<String, Control>
```

Where:

- the key is the control `code`
- the value is the concrete public facade implementing `Control`
- insertion order is preserved

This keeps the result explicit and lightweight while staying aligned with the closed facade model of the framework.

---

## Main Loading Flow

The primary multi-control flow is:

```text
JSON -> ControlConfigLoader -> ControlFactoryRegistry -> concrete control factories -> Map<String, Control>
```

Responsibilities:

- `ControlConfigLoader` reads the JSON document, validates the root structure, validates duplicate `code` values, and builds the public result map
- `ControlFactoryRegistry` resolves `type` to the corresponding control-specific loader and factory
- control-specific loaders validate the supported properties for their own control type
- control-specific factories create the concrete public facades

The JSON layer still ends at public facades. It does not expose `View`, `ViewModel`, input adapters, or other MVVM internals.

---

## Supported Control Types

The current registry supports:

- `button`
- `checkbox`
- `toggle`
- `slider`
- `label`
- `radiogroup`
- `textfield`
- `numericfield`
- `dropdown`

`dropdown` requires `OverlayManager` and `InputManager` when the main loader is used, because those are already required by the public `DropDown` facade.

---

## Minimal Example

Single wrapped control:

```json
{
  "controls": [
    {
      "type": "button",
      "code": "btnJsonTest",
      "text": "JSON Button",
      "x": 300.0,
      "y": 125.0,
      "width": 220.0,
      "height": 60.0,
      "enabled": true,
      "visible": true
    }
  ]
}
```

Java:

```java
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;

import java.util.Map;

ControlConfigLoader loader = new ControlConfigLoader(this);
Map<String, Control> controls = loader.load("data/config/button-test.json");
Button button = (Button) controls.get("btnJsonTest");
```

Multi-control document:

```json
{
  "controls": [
    {
      "type": "label",
      "code": "lblTitle",
      "text": "Configuration Title",
      "x": 120.0,
      "y": 50.0,
      "width": 320.0,
      "height": 60.0
    },
    {
      "type": "button",
      "code": "btnPrimary",
      "text": "Run",
      "x": 120.0,
      "y": 140.0,
      "width": 180.0,
      "height": 56.0
    }
  ]
}
```

The document defines structure only. Any listener wiring or binding still belongs to the sketch.

Canonical examples:

- `JsonMultiControlUnidirectionalBindingTest`
- `JsonMultiControlBindingTest`
- `data/config/json-multicontrol-binding-test.json`

Those examples keep the scope intentionally small:

- `Label` for title
- `Label` for help text
- `Slider`
- `NumericField`
- `Label` for the current value

They demonstrate:

- loading multiple controls from one JSON document
- retrieving them from `Map<String, Control>`
- binding `Slider` and `NumericField` in the sketch, not in JSON
- updating derived `Label` controls programmatically
- keeping all visible text in `Label` instead of `text()`

The unidirectional sketch is the base pattern: one slider listener updates the numeric field and label. The bidirectional sketch keeps the same JSON and composition, then adds one numeric-field listener, one extra sync routine, and a local anti-loop guard.

Those examples are intentionally explicit:

- JSON defines the controls, layout, style, and base text
- the sketch performs binding and derived-state updates
- visible UI text is rendered through `Label`, not through `text()`

---

## Validation

The main loader fails fast with clear errors when:

- `controls` is missing
- `controls` is not an array
- an entry is not an object
- `type` is missing
- `code` is missing
- a `code` is duplicated
- `type` is unknown
- a control-specific property is invalid
- the JSON document cannot be loaded

Examples of control-specific validation that remain delegated to the specific loaders:

- `Slider`: range, step, value, orientation, and snap mode
- `RadioGroup`: options and `selectedIndex`
- `NumericField`: numeric text grammar
- `DropDown`: items and `selectedIndex`

There is no silent fallback and no implicit autocorrection.

---

## Control-Specific Loaders

The framework still includes control-specific loaders such as:

- `ButtonConfigLoader`
- `CheckboxConfigLoader`
- `ToggleConfigLoader`
- `SliderConfigLoader`
- `LabelConfigLoader`
- `RadioGroupConfigLoader`
- `TextFieldConfigLoader`
- `NumericFieldConfigLoader`
- `DropDownConfigLoader`

They remain useful for simple single-control examples.

Current behavior:

- they accept the new wrapped document format when the document contains exactly one matching control entry
- they keep accepting the historical single-root-object format as a legacy path
- they are not the main public representation for multi-control loading

Legacy note:

- the historical single-root-object format is legacy
- the recommended document format is the root `controls` array
- the main `ControlConfigLoader` expects the new multi-control format

---

## Style Blocks

Style remains nested under each control entry:

```json
{
  "controls": [
    {
      "type": "slider",
      "code": "sldJsonTest",
      "min": 0.0,
      "max": 1.0,
      "step": 0.05,
      "value": 0.35,
      "x": 300.0,
      "y": 130.0,
      "width": 320.0,
      "height": 72.0,
      "style": {
        "trackColor": "#3E4856",
        "thumbColor": "#FFFFFF",
        "showValueText": true
      }
    }
  ]
}
```

The style block still affects appearance only. It does not define behavior, listeners, input routing, or binding.

SVG renderer configuration also remains local to the control style block:

```json
"renderer": {
  "type": "svg",
  "path": "data/img/test.svg"
}
```

---

## Binding Boundary

JSON does not define binding.

That boundary is intentional:

- JSON describes control structure and style
- the sketch creates listeners and wiring
- any binding between controls happens in Java code

This keeps the configuration layer explicit and aligned with the closed facade model of the framework.

---

## Scope

The current JSON layer supports:

- multiple controls in one document
- strict validation
- concrete public facade creation
- type-based dispatch through a central registry

It intentionally does not support:

- binding in JSON
- declarative listeners in JSON
- layout orchestration language
- MVVM internals in the public result

---

## See Also

- [Control](control.md)
- [Architecture](architecture.md)
- [README](../README.md)
- [Button](button.md)
- [Checkbox](checkbox.md)
- [Toggle](toggle.md)
- [Slider](slider.md)
- [Label](label.md)
- [RadioGroup](radiogroup.md)
- [TextField](textfield.md)
- [NumericField](numericfield.md)
- [Dropdown](dropdown.md)

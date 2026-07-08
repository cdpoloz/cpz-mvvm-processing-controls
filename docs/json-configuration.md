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
- a control may include an optional `tooltip` block

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
- `indicator`
- `progressbar`

`dropdown` requires `OverlayManager` and `InputManager` when the main loader is used, because those are already required by the public `DropDown` facade.

`Notification` is intentionally not listed here. Notifications are
programmatic runtime UI events, not durable control facades. They are not
loaded from `controls[]`, are not created by `ControlConfigLoader`, and are not
registered in `ControlFactoryRegistry`.

`progressbar` is part of the current `main` branch development for `0.8.0`.
The latest stable Maven Central release remains `0.7.1`.

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
      "height": 60.0,
      "style": {
        "textSize": 24.0,
        "font": "data/font/JetBrainsMono.ttf"
      }
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
- `IndicatorConfigLoader`
- `ProgressBarConfigLoader`

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

Every control that currently renders text accepts an optional `font` path in
its style:

- `button`
- `label`
- `slider`, for the value text
- `radiogroup`
- `textfield`
- `numericfield`
- `dropdown`, for both the collapsed value and expanded items

`checkbox` and `toggle` do not render text and therefore do not accept this
typography property in their own `style` block. They can still use
`tooltip.style.font`, because tooltip text belongs to the reusable tooltip
overlay, not to the control renderer.

`indicator` does not render text. Its status colors are accepted as top-level
`onColor` and `offColor` fields, and also as `style.onColor` and
`style.offColor` aliases for consistency with the style-oriented JSON shape.
When both are present, the top-level fields take precedence.

Indicator properties:

- `type`: must be `"indicator"`
- `code`: unique control code
- `x`, `y`, `width`, `height`: legacy absolute bounds
- `bounds`: explicit `absolute` or `relative` bounds object
- `on`: optional boolean state, default `false`
- `onColor`: optional active color
- `offColor`: optional inactive color
- `tooltip`: optional tooltip object or text shorthand
- `style.onColor` / `style.offColor`: optional color aliases
- `style.strokeColor`: optional visual border color
- `style.strokeWeight`: optional visual border width; `0` disables the stroke
- `style.strokeWidth`: optional alias used only when `strokeWeight` is absent
- `style.renderer`: optional renderer object; SVG uses
  `{"type":"svg","path":"..."}`

Minimal indicator entry:

```json
{
  "type": "indicator",
  "code": "indStatus",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.1,
    "width": 0.05,
    "height": 0.05
  },
  "on": true,
  "onColor": "#FF2ECC71",
  "offColor": "#FF30343A",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 2.0
  },
  "tooltip": "Status"
}
```

SVG indicator entry:

```json
{
  "type": "indicator",
  "code": "indSvg",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.1,
    "width": 0.08,
    "height": 0.08
  },
  "on": true,
  "onColor": "#FF2ECC71",
  "offColor": "#FF30343A",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 2.0,
    "renderer": {
      "type": "svg",
      "path": "data/img/test.svg"
    }
  },
  "tooltip": "SVG indicator"
}
```

`on` defaults to `false`. `onColor` defaults to green and `offColor` defaults
to dark gray when omitted. These visual values are applied to the control's
`IndicatorStyle`. `style.strokeColor` defaults to the indicator border color
and `style.strokeWeight` defaults to `1.0`. `style.strokeWeight` is the
canonical border-width property; `style.strokeWidth` is accepted only as an
alias for Toggle terminology. `borderColor` is not an Indicator visual border
property; it belongs to tooltip style blocks. Relative `bounds` follows the same explicit
`absolute` / `relative` mode rules as the other controls; `bounds` takes
precedence over legacy `x` / `y` / `width` / `height`. SVG uses the same
`style.renderer` object as Button and Toggle and is represented by
`IndicatorStyle`. Supported renderer values in this iteration are
`{"type":"svg","path":"..."}`.

`progressbar` does not render text and is non-interactive. It displays a
determinate value horizontally or vertically according to `fillDirection`. Its
visual colors and fill direction are applied to the control's
`ProgressBarStyle` and are accepted as top-level
`trackColor` and `fillColor` fields, and also as `style.trackColor` and
`style.fillColor` aliases for consistency with the style-oriented JSON shape.
When both are present, the top-level fields take precedence.

ProgressBar properties:

- `type`: must be `"progressbar"`
- `code`: unique control code
- `x`, `y`, `width`, `height`: legacy absolute bounds
- `bounds`: explicit `absolute` or `relative` bounds object
- `min`: optional minimum, default `0.0`
- `max`: optional maximum, default `1.0`
- `value`: optional value, default `0.0`
- `trackColor`: optional track color
- `fillColor`: optional fill color
- `fillDirection`: optional top-level fill direction alias
- `tooltip`: optional tooltip object or text shorthand
- `style.trackColor` / `style.fillColor`: optional color aliases
- `style.strokeColor`: optional outer stroke color
- `style.strokeWeight`: optional outer stroke width; `0` disables the stroke
- `style.fillDirection`: optional fill direction, default `"left-to-right"`

Minimal progress bar entry:

```json
{
  "type": "progressbar",
  "code": "pbLoad",
  "bounds": {
    "mode": "relative",
    "x": 0.1,
    "y": 0.1,
    "width": 0.5,
    "height": 0.04
  },
  "min": 0.0,
  "max": 100.0,
  "value": 35.0,
  "trackColor": "#FF30343A",
  "fillColor": "#FF2F80ED",
  "style": {
    "strokeColor": "#FFFFFFFF",
    "strokeWeight": 1.5,
    "fillDirection": "left-to-right"
  },
  "tooltip": "Loading"
}
```

`value` is clamped to `[min, max]`. If `min > max`, the loader stores the
sorted range. If `min == max`, normalized progress avoids division by zero and
reports full progress after normal clamping. `style.strokeWeight` is the
canonical border-width property. `borderColor` is not a ProgressBar visual
border property; it belongs to tooltip style blocks. Relative `bounds` follows
the same explicit `absolute` / `relative` mode rules as the other controls;
`bounds` takes precedence over legacy `x` / `y` / `width` / `height`.
Supported fill direction values are `left-to-right`, `right-to-left`,
`bottom-to-top`, and `top-to-bottom`. Values are trimmed and case-insensitive;
hyphen, underscore, and space separators are accepted. Invalid values use the
default `LEFT_TO_RIGHT` direction.

```json
{
  "type": "label",
  "code": "lblJsonTest",
  "text": "Label facade\nJSON example",
  "x": 120.0,
  "y": 70.0,
  "width": 360.0,
  "height": 100.0,
  "style": {
    "textSize": 24.0,
    "font": "data/font/JetBrainsMono.ttf",
    "textColor": "#D2E4FF",
    "lineSpacingMultiplier": 1.2,
    "alignX": "center",
    "alignY": "center",
    "disabledAlpha": 80
  }
}
```

The control-specific loader validates and stores the path. The corresponding
factory then creates the `PFont` through the shared font loader. Loading occurs
once while the control is created, never during `draw()`.

`font` may be omitted or set to JSON `null`. Both mean that the control does
not impose a font; rendering falls back to the font currently active in
Processing/PGraphics. That fallback is not a fixed font owned by this library.
An empty or whitespace-only string is invalid and produces an error containing
the control, the `font` property, the JSON source path when available, and the
cause.

Existing default text sizes remain unchanged:

- `Label`: `12.0f`
- `RadioGroup`, `TextField`, `NumericField`, and `DropDown`: `16.0f`

`Button` and `Slider` preserve their historical ambient size behavior by
making `textSize` optional:

| `font` | `textSize` | Result |
| --- | --- | --- |
| omitted or `null` | omitted or `null` | Preserve the active Processing font and size |
| omitted or `null` | value | Preserve the active font and apply the configured size |
| path | value | Apply the loaded font and configured size |
| path | omitted or `null` | Apply the loaded font at `16.0f` |

The font file must be accessible to the Processing sketch, for example
`data/font/JetBrainsMono.ttf`. Invalid, unreadable or unsupported font files
fail control creation with an `IllegalArgumentException`; they do not fail
later from the render loop.

---

## Tooltip Blocks

Multi-control documents may define reusable tooltip styles at the root:

```json
{
  "tooltipStyles": {
    "dark": {
      "backgroundColor": "#E61B1F26",
      "textColor": "#FFFFFFFF",
      "borderColor": "#668A94A6",
      "font": "data/font/JetBrainsMono.ttf",
      "textSize": 14.0,
      "textPadding": 10.0,
      "offset": 10.0,
      "cornerRadius": 8.0,
      "strokeWeight": 1.0
    }
  },
  "controls": []
}
```

Every supported control type accepts an optional top-level `tooltip` block:

```json
{
  "type": "button",
  "code": "btnSave",
  "text": "Save",
  "x": 120.0,
  "y": 80.0,
  "width": 160.0,
  "height": 44.0,
  "tooltip": {
    "text": "Guardar cambios",
    "enabled": true,
    "styleRef": "dark",
    "style": {
      "backgroundColor": "#E61B1F26",
      "textColor": "#FFFFFFFF",
      "borderColor": "#668A94A6",
      "font": "data/font/JetBrainsMono.ttf",
      "textSize": 14.0,
      "textPadding": 10.0,
      "offset": 10.0,
      "cornerRadius": 8.0,
      "strokeWeight": 1.0
    }
  }
}
```

For simple text-only tooltips, the shorthand form is also accepted:

```json
{
  "type": "indicator",
  "code": "indStatus",
  "x": 40.0,
  "y": 40.0,
  "width": 24.0,
  "height": 24.0,
  "tooltip": "Status"
}
```

Tooltip colors use the same color parsing as control styles: integer values,
`#RRGGBB`, or `#AARRGGBB`. A missing or `null` tooltip leaves the created
control unchanged.

`tooltip.styleRef` references an entry from root `tooltipStyles`. The local
`tooltip.style` block remains supported and, when combined with `styleRef`,
overrides only the fields it defines. Unknown references fail config loading
with an `IllegalArgumentException`. `textPadding` and `padding` are accepted as
aliases for tooltip text padding.

The JSON loader assigns tooltips to controls, but the sketch still owns overlay
registration. Register controls with a `TooltipOverlayController` and route
pointer motion through `TooltipInputLayer`.

Fonts declared in `tooltipStyles` are loaded once when the JSON document is
loaded and then copied into each tooltip. Tooltip fonts are not loaded from
`draw()`.

The control-level tooltip block is distinct from standalone tooltip loading:

```java
Tooltip tooltip = TooltipFactory.loadFromJson(this, "data/config/server-tooltip.json");
TooltipArea serverArea = new TooltipArea(520, 230, 190, 92).setTooltip(tooltip);
```

Use the control `tooltip` block when the tooltip belongs to a control in
`controls[]`. Use `TooltipFactory.loadFromJson(...)` when the sketch owns the
target, such as a `TooltipArea`, `PImage` region, icon, or manually rendered
shape. In both cases, JSON only configures tooltip data; the sketch still
registers the control or area with `TooltipOverlayController`.

The dedicated visual example is
`src/main/java/com/cpz/processing/controls/examples/tooltip/TooltipVisualJsonTest.java`.
It uses `data/config/tooltip-visual-test.json` for control tooltips and
`data/config/server-tooltip.json` for a standalone tooltip assigned to a
manual `TooltipArea`.

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
- [Indicator](indicator.md)
- [ProgressBar](progressbar.md)
- [Tooltip](tooltip.md)
- [Notification](notification.md)

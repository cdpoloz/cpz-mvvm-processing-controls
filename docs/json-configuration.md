# JSON Configuration

This document shows how to create controls from JSON configuration files.

In the current iteration, the framework supports a single `Button`, a single `Checkbox`, a single `Toggle`, or a single `Slider` created from JSON, including optional SVG renderer setup configured through the style block.

JSON configuration is an additional layer on top of the existing public API. It does not replace direct control creation with `Button`, `Checkbox`, `Toggle`, or `Slider`, and it does not change the internal MVVM architecture of the framework.

---

## Overview

JSON configuration introduces an optional config-driven layer that solves a specific problem:

- it moves geometry and visual setup out of Java code
- it makes simple control setup easier to externalize
- it keeps behavior and input wiring in the sketch

The integration model is intentionally small:

- JSON describes configuration
- the loader parses JSON into a config DTO
- the factory creates the public control facade
- the sketch still owns input and behavior

Internally, the framework still uses the same MVVM-based control implementations. JSON is only a configuration layer in front of that flow.

---

## Architecture

The config-driven flows are:

```text
JSON → ButtonConfigLoader → ButtonConfig → ButtonFactory → Button facade → MVVM internals
JSON → CheckboxConfigLoader → CheckboxConfig → CheckboxFactory → Checkbox facade → MVVM internals
JSON → ToggleConfigLoader → ToggleConfig → ToggleFactory → Toggle facade → MVVM internals
JSON → SliderConfigLoader → SliderConfig → SliderFactory → Slider facade → MVVM internals
```

Responsibilities:

- `ButtonConfig`, `CheckboxConfig`, `ToggleConfig`, and `SliderConfig` store the parsed control data
- `ButtonConfigLoader`, `CheckboxConfigLoader`, `ToggleConfigLoader`, and `SliderConfigLoader` read the JSON file and validate the supported fields
- `ButtonFactory`, `CheckboxFactory`, `ToggleFactory`, and `SliderFactory` create the public facade and apply state and style
- `Button`, `Checkbox`, `Toggle`, and `Slider` remain the public facades used by the sketch

This means the external setup becomes config-driven, but the runtime control pipeline remains the same.

---

## Minimal examples

### 1. Button

Minimal JSON:

```json
{
  "code": "btnJsonTest",
  "text": "JSON Button",
  "x": 300.0,
  "y": 125.0,
  "width": 220.0,
  "height": 60.0,
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String BUTTON_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "button-test.json";

private InputManager inputManager;
private Button button;

public void setup() {
    ButtonConfigLoader loader = new ButtonConfigLoader(this);
    ButtonConfig config = loader.load(BUTTON_CONFIG_PATH);
    button = ButtonFactory.create(this, config);

    button.setClickListener(() -> {
        System.out.println("You clicked the JSON button!");
    });

    inputManager = new InputManager();
    inputManager.registerLayer(new ButtonInputLayer(0, button));
}
```

### 2. Checkbox

Minimal JSON:

```json
{
  "code": "chkJsonTest",
  "checked": true,
  "x": 300.0,
  "y": 125.0,
  "width": 42.0,
  "height": 42.0,
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String CHECKBOX_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "checkbox-test.json";

private InputManager inputManager;
private Checkbox checkbox;

public void setup() {
    CheckboxConfigLoader loader = new CheckboxConfigLoader(this);
    CheckboxConfig config = loader.load(CHECKBOX_CONFIG_PATH);
    checkbox = CheckboxFactory.create(this, config);

    checkbox.setChangeListener(value -> {
        System.out.println("Checkbox checked = " + value);
    });

    inputManager = new InputManager();
    inputManager.registerLayer(new CheckboxInputLayer(0, checkbox));
}
```

### 3. Toggle

Minimal JSON:

```json
{
  "code": "tglJsonTest",
  "state": 0,
  "totalStates": 3,
  "x": 300.0,
  "y": 130.0,
  "width": 96.0,
  "height": 96.0,
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String TOGGLE_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "toggle-test.json";

private InputManager inputManager;
private Toggle toggle;

public void setup() {
    ToggleConfigLoader loader = new ToggleConfigLoader(this);
    ToggleConfig config = loader.load(TOGGLE_CONFIG_PATH);
    toggle = ToggleFactory.create(this, config);

    toggle.setChangeListener(value -> {
        System.out.println("Toggle state = " + value);
    });

    inputManager = new InputManager();
    inputManager.registerLayer(new ToggleInputLayer(0, toggle));
}
```

### 4. Slider

Minimal JSON:

```json
{
  "code": "sldJsonTest",
  "min": 0.0,
  "max": 1.0,
  "step": 0.05,
  "value": 0.35,
  "x": 300.0,
  "y": 130.0,
  "width": 320.0,
  "height": 72.0,
  "orientation": "horizontal",
  "snapMode": "always",
  "enabled": true,
  "visible": true
}
```

Minimal Java sketch flow:

```java
private static final String SLIDER_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "slider-test.json";

private InputManager inputManager;
private Slider slider;

public void setup() {
    SliderConfigLoader loader = new SliderConfigLoader(this);
    SliderConfig config = loader.load(SLIDER_CONFIG_PATH);
    slider = SliderFactory.create(this, config);

    slider.setChangeListener(value -> {
        System.out.println("Slider value = " + value);
    });

    inputManager = new InputManager();
    inputManager.registerLayer(new SliderInputLayer(0, slider));
}
```

The important part is unchanged from the regular control flow:

- the sketch still assigns listeners in Java
- the sketch still creates the `InputManager`
- the sketch still registers the control-specific input layer
- the JSON provides the stable control identity through the required `code` field

The configuration only defines structure and appearance. Behavior remains defined in Java.

For `Toggle`, the JSON route is stricter than the direct Java API:

- invalid `totalStates` or `state` values fail fast during loading
- the loader throws a clear exception instead of coercing the values

For `Slider`, the JSON route also validates configuration strictly before the control is created:

- `min` must be lower than `max`
- `step` must be greater than `0`
- `value` must already be inside the configured range
- `orientation`, `snapMode`, and `svgColorMode` only accept the documented values

---

## Style configuration

The optional `style` block maps JSON values to the style config of the selected control.

### 1. Button style

Example:

```json
"style": {
  "baseColor": "#3062DB",
  "textColor": "#FFFFFF",
  "strokeColor": "#FFFFFF",
  "strokeWeight": 2.0,
  "strokeWeightHover": 4.0,
  "cornerRadius": 18.0,
  "disabledAlpha": 90,
  "hoverBlendWithWhite": 0.12,
  "pressedBlendWithBlack": 0.25
}
```

Supported button style fields in the current iteration:

- `baseColor`
- `textColor`
- `strokeColor`
- `strokeWeight`
- `strokeWeightHover`
- `cornerRadius`
- `disabledAlpha`
- `hoverBlendWithWhite`
- `pressedBlendWithBlack`

### 2. Checkbox style

Example:

```json
"style": {
  "boxColor": "#3062DB",
  "boxHoverColor": "#4A7AEA",
  "boxPressedColor": "#224DB8",
  "checkColor": "#FFFFFF",
  "borderColor": "#FFFFFF",
  "borderWidth": 2.0,
  "borderWidthHover": 4.0,
  "cornerRadius": 10.0,
  "disabledAlpha": 90,
  "checkInset": 0.20
}
```

Supported checkbox style fields in the current iteration:

- `checkedFillOverride`
- `uncheckedFillOverride`
- `hoverFillOverride`
- `pressedFillOverride`
- `checkOverride`
- `strokeOverride`
- `boxColor`
- `boxHoverColor`
- `boxPressedColor`
- `checkColor`
- `borderColor`
- `borderWidth`
- `borderWidthHover`
- `cornerRadius`
- `disabledAlpha`
- `checkInset`

### 3. Toggle style

Example:

```json
"style": {
  "stateColors": ["#464646", "#E89B2C", "#20BCB0"],
  "strokeColor": "#FFFFFF",
  "strokeWidth": 2.0,
  "strokeWidthHover": 4.0,
  "hoverBlendWithWhite": 0.18,
  "pressedBlendWithBlack": 0.20,
  "disabledAlpha": 70
}
```

Supported toggle style fields in the current iteration:

- `offFillOverride`
- `onFillOverride`
- `hoverFillOverride`
- `pressedFillOverride`
- `strokeOverride`
- `stateColors`
- `strokeColor`
- `strokeWidth`
- `strokeWidthHover`
- `hoverBlendWithWhite`
- `pressedBlendWithBlack`
- `disabledAlpha`

### 4. Slider style

Example:

```json
"style": {
  "trackColor": "#3E4856",
  "trackHoverColor": "#566274",
  "trackPressedColor": "#2C3440",
  "trackStrokeColor": "#DCDCDC",
  "trackStrokeWeight": 1.5,
  "trackStrokeWeightHover": 2.5,
  "trackThickness": 10.0,
  "activeTrackColor": "#389FE8",
  "activeTrackHoverColor": "#62B8F4",
  "activeTrackPressedColor": "#267CB8",
  "thumbColor": "#FFFFFF",
  "thumbHoverColor": "#C6EAFF",
  "thumbPressedColor": "#AADCFF",
  "thumbStrokeColor": "#204E78",
  "thumbStrokeWeight": 2.0,
  "thumbStrokeWeightHover": 3.0,
  "thumbSize": 28.0,
  "textColor": "#F5F5F5",
  "disabledAlpha": 90,
  "showValueText": true
}
```

Supported slider style fields in the current iteration:

- `trackOverride`
- `trackHoverOverride`
- `trackPressedOverride`
- `progressOverride`
- `progressHoverOverride`
- `progressPressedOverride`
- `thumbOverride`
- `thumbHoverOverride`
- `thumbPressedOverride`
- `trackStrokeOverride`
- `thumbStrokeOverride`
- `textOverride`
- `trackColor`
- `trackHoverColor`
- `trackPressedColor`
- `trackStrokeColor`
- `trackStrokeWeight`
- `trackStrokeWeightHover`
- `trackThickness`
- `activeTrackColor`
- `activeTrackHoverColor`
- `activeTrackPressedColor`
- `thumbColor`
- `thumbHoverColor`
- `thumbPressedColor`
- `thumbStrokeColor`
- `thumbStrokeWeight`
- `thumbStrokeWeightHover`
- `thumbSize`
- `textColor`
- `disabledAlpha`
- `showValueText`
- `svgColorMode`

The style block only controls appearance. It does not define listeners, input routing, or business behavior.

---

## SVG renderer

SVG support is configured through an optional `renderer` block inside `style`. The renderer configuration extends the style block without changing the control structure.

Example:

```json
"renderer": {
  "type": "svg",
  "path": "data/img/test.svg"
}
```

Notes:

- the block is optional
- only `svg` is supported in this iteration
- the factory creates the corresponding SVG rendering setup internally when this block is present
- `Button` uses `SvgButtonRenderer`
- `Checkbox` uses `SvgCheckboxRenderer`
- `Toggle` uses `SvgShapeRenderer`
- `Slider` uses the same internal `SliderStyle` and `SliderRenderer` path, with the SVG asset applied to the thumb

This keeps the external JSON declarative while reusing the same rendering mechanism already used by the direct Java API.

For `Slider`, the optional `svgColorMode` field controls how the thumb SVG is colored:

- `use_render_style`
- `use_svg_original`

---

## Full examples

### 1. Normal slider from JSON

JSON:

```json
{
  "code": "sldJsonTest",
  "min": 0.0,
  "max": 1.0,
  "step": 0.05,
  "value": 0.35,
  "x": 300.0,
  "y": 130.0,
  "width": 320.0,
  "height": 72.0,
  "orientation": "horizontal",
  "snapMode": "always",
  "enabled": true,
  "visible": true,
  "style": {
    "trackColor": "#3E4856",
    "trackHoverColor": "#566274",
    "trackPressedColor": "#2C3440",
    "trackStrokeColor": "#DCDCDC",
    "trackStrokeWeight": 1.5,
    "trackStrokeWeightHover": 2.5,
    "trackThickness": 10.0,
    "activeTrackColor": "#389FE8",
    "activeTrackHoverColor": "#62B8F4",
    "activeTrackPressedColor": "#267CB8",
    "thumbColor": "#FFFFFF",
    "thumbHoverColor": "#C6EAFF",
    "thumbPressedColor": "#AADCFF",
    "thumbStrokeColor": "#204E78",
    "thumbStrokeWeight": 2.0,
    "thumbStrokeWeightHover": 3.0,
    "thumbSize": 28.0,
    "textColor": "#F5F5F5",
    "disabledAlpha": 90,
    "showValueText": true
  }
}
```

Java:

```java
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfig;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.io.File;

public class SliderJsonTest extends PApplet {
    private static final String SLIDER_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "slider-test.json";

    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        SliderConfigLoader loader = new SliderConfigLoader(this);
        SliderConfig config = loader.load(SLIDER_CONFIG_PATH);
        slider = SliderFactory.create(this, config);
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, slider));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        slider.draw();
        text(slider.getCode() + " | value = " + currentValue, 300, 240);
        text("config-driven slider using SliderConfigLoader and SliderFactory", 300, 275);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }
}
```

---

### 2. SVG slider from JSON

JSON:

```json
{
  "code": "sldSvgJsonTest",
  "min": 0.0,
  "max": 1.0,
  "step": 0.05,
  "value": 0.35,
  "x": 300.0,
  "y": 130.0,
  "width": 320.0,
  "height": 72.0,
  "orientation": "horizontal",
  "snapMode": "always",
  "enabled": true,
  "visible": true,
  "style": {
    "trackColor": "#3E4856",
    "trackHoverColor": "#566274",
    "trackPressedColor": "#2C3440",
    "trackStrokeColor": "#DCDCDC",
    "trackStrokeWeight": 1.5,
    "trackStrokeWeightHover": 2.5,
    "trackThickness": 10.0,
    "activeTrackColor": "#389FE8",
    "activeTrackHoverColor": "#62B8F4",
    "activeTrackPressedColor": "#267CB8",
    "thumbColor": "#FFFFFF",
    "thumbHoverColor": "#C6EAFF",
    "thumbPressedColor": "#AADCFF",
    "thumbStrokeColor": "#204E78",
    "thumbStrokeWeight": 2.0,
    "thumbStrokeWeightHover": 3.0,
    "thumbSize": 28.0,
    "textColor": "#F5F5F5",
    "disabledAlpha": 90,
    "showValueText": true,
    "svgColorMode": "use_render_style",
    "renderer": {
      "type": "svg",
      "path": "data/img/test.svg"
    }
  }
}
```

Java:

```java
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfig;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.io.File;

public class SliderSvgJsonTest extends PApplet {
    private static final String SLIDER_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "slider-svg-test.json";

    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        SliderConfigLoader loader = new SliderConfigLoader(this);
        SliderConfig config = loader.load(SLIDER_CONFIG_PATH);
        slider = SliderFactory.create(this, config);
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, slider));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        slider.draw();
        text(slider.getCode() + " | value = " + currentValue, 300, 240);
        text("SVG thumb loaded from JSON through the same slider style path", 300, 275);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }
}
```

---

## Scope notes

The config-driven layer remains intentionally small in this iteration:

- one control per JSON file
- no listeners in JSON
- no binding in JSON
- no multi-control document
- no layout system in JSON
- no generic cross-control factory

This keeps the external layer explicit and aligned with the current public facade model.

---

## See also

- [Button](button.md)
- [Checkbox](checkbox.md)
- [Toggle](toggle.md)
- [Slider](slider.md)
- [Slider (SVG)](slider-svg.md)
